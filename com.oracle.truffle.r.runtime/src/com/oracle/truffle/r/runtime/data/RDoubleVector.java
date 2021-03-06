/*
 * Copyright (c) 2013, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.runtime.data;

import java.util.Arrays;

import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.Utils;
import com.oracle.truffle.r.runtime.data.closures.RClosures;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.data.model.RAbstractDoubleVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.data.nodes.FastPathVectorAccess.FastPathFromDoubleAccess;
import com.oracle.truffle.r.runtime.data.nodes.SlowPathVectorAccess.SlowPathFromDoubleAccess;
import com.oracle.truffle.r.runtime.data.nodes.VectorAccess;
import com.oracle.truffle.r.runtime.ops.na.NACheck;

public final class RDoubleVector extends RVector<double[]> implements RAbstractDoubleVector {

    private double[] data;

    RDoubleVector(double[] data, boolean complete) {
        super(complete);
        this.data = data;
        assert RAbstractVector.verify(this);
    }

    RDoubleVector(double[] data, boolean complete, int[] dims, RStringVector names, RList dimNames) {
        this(data, complete);
        initDimsNamesDimNames(dims, names, dimNames);
    }

    private RDoubleVector() {
        super(false);
    }

    static RDoubleVector fromNative(long address, int length) {
        RDoubleVector result = new RDoubleVector();
        NativeDataAccess.asPointer(result);
        NativeDataAccess.setNativeContents(result, address, length);
        assert result.data == null;
        return result;
    }

    @Override
    public RAbstractVector castSafe(RType type, ConditionProfile isNAProfile, boolean keepAttributes) {
        switch (type) {
            case Integer:
                return RClosures.createToIntVector(this, keepAttributes);
            case Double:
                return this;
            case Complex:
                return RClosures.createToComplexVector(this, keepAttributes);
            case Character:
                return RClosures.createToStringVector(this, keepAttributes);
            case List:
                return RClosures.createToListVector(this, keepAttributes);
            default:
                return null;
        }
    }

    @Override
    protected RDoubleVector internalCopy() {
        return new RDoubleVector(getDataCopy(), this.isComplete());
    }

    @Override
    public double[] getInternalManagedData() {
        return data;
    }

    @Override
    public double[] getInternalStore() {
        return data;
    }

    @Override
    public void setDataAt(Object store, int index, double value) {
        assert data == store;
        NativeDataAccess.setData(this, (double[]) store, index, value);
    }

    @Override
    public double getDataAt(Object store, int index) {
        assert data == store;
        return NativeDataAccess.getData(this, (double[]) store, index);
    }

    public RDoubleVector copyResetData(double[] newData) {
        boolean isComplete = true;
        for (int i = 0; i < newData.length; i++) {
            if (RRuntime.isNA(newData[i])) {
                isComplete = false;
                break;
            }
        }
        RDoubleVector result = new RDoubleVector(newData, isComplete);
        setAttributes(result);
        return result;
    }

    @Override
    public int getLength() {
        return NativeDataAccess.getDataLength(this, data);
    }

    @Override
    public void setLength(int l) {
        try {
            NativeDataAccess.setDataLength(this, data, l);
        } finally {
            data = null;
            complete = false;
        }
    }

    @Override
    public int getTrueLength() {
        return NativeDataAccess.getTrueDataLength(this);
    }

    @Override
    public void setTrueLength(int l) {
        NativeDataAccess.setTrueDataLength(this, l);
    }

    @Override
    public double getDataAt(int index) {
        return NativeDataAccess.getData(this, data, index);
    }

    @Override
    public double[] getDataCopy() {
        if (data != null) {
            return Arrays.copyOf(data, data.length);
        } else {
            return NativeDataAccess.copyDoubleNativeData(getNativeMirror());
        }
    }

    @Override
    public double[] getReadonlyData() {
        if (data != null) {
            return data;
        } else {
            return NativeDataAccess.copyDoubleNativeData(getNativeMirror());
        }
    }

    @Override
    public RDoubleVector copyWithNewDimensions(int[] newDimensions) {
        return RDataFactory.createDoubleVector(getReadonlyData(), isComplete(), newDimensions);
    }

    private RDoubleVector updateDataAt(int index, double value, NACheck valueNACheck) {
        assert !this.isShared();
        NativeDataAccess.setData(this, data, index, value);
        if (valueNACheck.check(value)) {
            complete = false;
        }
        assert !isComplete() || !RRuntime.isNA(value);
        return this;
    }

    @Override
    public RDoubleVector updateDataAtAsObject(int i, Object o, NACheck naCheck) {
        return updateDataAt(i, (Double) o, naCheck);

    }

    static double[] resizeData(double[] newData, double[] oldData, int oldDataLength, boolean fillNA) {
        if (newData.length > oldDataLength) {
            if (fillNA) {
                for (int i = oldDataLength; i < newData.length; i++) {
                    newData[i] = RRuntime.DOUBLE_NA;
                }
            } else {
                assert oldData.length > 0 : "cannot call resize on empty vector if fillNA == false";
                for (int i = oldDataLength, j = 0; i < newData.length; ++i, j = Utils.incMod(j, oldDataLength)) {
                    newData[i] = oldData[j];
                }
            }
        }
        return newData;
    }

    private double[] copyResizedData(int size, boolean fillNA) {
        double[] localData = getReadonlyData();
        double[] newData = Arrays.copyOf(localData, size);
        return resizeData(newData, localData, localData.length, fillNA);
    }

    @Override
    protected RDoubleVector internalCopyResized(int size, boolean fillNA, int[] dimensions) {
        boolean isComplete = isComplete() && ((getLength() >= size) || !fillNA);
        return RDataFactory.createDoubleVector(copyResizedData(size, fillNA), isComplete, dimensions);
    }

    @Override
    public RDoubleVector materialize() {
        return this;
    }

    @Override
    public RDoubleVector createEmptySameType(int newLength, boolean newIsComplete) {
        return RDataFactory.createDoubleVector(new double[newLength], newIsComplete);
    }

    @Override
    public void transferElementSameType(int toIndex, RAbstractVector fromVector, int fromIndex) {
        NativeDataAccess.setData(this, data, toIndex, ((RAbstractDoubleVector) fromVector).getDataAt(fromIndex));
    }

    @Override
    public Object getDataAtAsObject(int index) {
        return getDataAt(index);
    }

    public long allocateNativeContents() {
        try {
            return NativeDataAccess.allocateNativeContents(this, data, getLength());
        } finally {
            data = null;
            complete = false;
        }
    }

    private static final class FastPathAccess extends FastPathFromDoubleAccess {

        FastPathAccess(RAbstractContainer value) {
            super(value);
        }

        @Override
        protected double getDoubleImpl(AccessIterator accessIter, int index) {
            return hasStore ? ((double[]) accessIter.getStore())[index] : NativeDataAccess.getDoubleNativeMirrorData(accessIter.getStore(), index);
        }

        @Override
        protected void setDoubleImpl(AccessIterator accessIter, int index, double value) {
            if (hasStore) {
                ((double[]) accessIter.getStore())[index] = value;
            } else {
                NativeDataAccess.setNativeMirrorDoubleData(accessIter.getStore(), index, value);
            }
        }
    }

    @Override
    public VectorAccess access() {
        return new FastPathAccess(this);
    }

    private static final SlowPathFromDoubleAccess SLOW_PATH_ACCESS = new SlowPathFromDoubleAccess() {
        @Override
        protected double getDoubleImpl(AccessIterator accessIter, int index) {
            RDoubleVector vector = (RDoubleVector) accessIter.getStore();
            return NativeDataAccess.getData(vector, vector.data, index);
        }

        @Override
        protected void setDoubleImpl(AccessIterator accessIter, int index, double value) {
            RDoubleVector vector = (RDoubleVector) accessIter.getStore();
            NativeDataAccess.setData(vector, vector.data, index, value);
        }
    };

    @Override
    public VectorAccess slowPathAccess() {
        return SLOW_PATH_ACCESS;
    }
}
