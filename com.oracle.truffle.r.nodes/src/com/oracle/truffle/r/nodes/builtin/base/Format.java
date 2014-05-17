/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.builtin.base;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.SlowPath;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.nodes.unary.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.model.*;

@RBuiltin(".Internal.format")
public abstract class Format extends RBuiltinNode {

    @Child private CastIntegerNode castInteger;

    private RAbstractIntVector castInteger(VirtualFrame frame, Object operand) {
        if (castInteger == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            castInteger = insert(CastIntegerNodeFactory.create(null, true, false, false));
        }
        return (RAbstractIntVector) castInteger.executeCast(frame, operand);
    }

    private static final Object[] PARAMETER_NAMES = new Object[]{"x", "trim", "digits", "nsmall", "width", "justify", "na.encode", "scientific"};

    @Override
    public Object[] getParameterNames() {
        return PARAMETER_NAMES;
    }

    @CreateCast("arguments")
    public RNode[] createCastValue(RNode[] children) {
        if (children.length != PARAMETER_NAMES.length) {
            throw RError.getArgumentsPassed(getEncapsulatingSourceSection(), children.length, ".Internal(format)", PARAMETER_NAMES.length);
        }
        RNode[] newChildren = new RNode[children.length];
        // cast to vector as appropriate to eliminate NULL values
        newChildren[0] = children[0];
        newChildren[1] = CastLogicalNodeFactory.create(CastToVectorNodeFactory.create(children[1], false, false, false, false), false, false, false);
        newChildren[2] = CastIntegerNodeFactory.create(CastToVectorNodeFactory.create(children[2], false, false, false, false), false, false, false);
        newChildren[3] = CastIntegerNodeFactory.create(CastToVectorNodeFactory.create(children[3], false, false, false, false), false, false, false);
        newChildren[4] = CastIntegerNodeFactory.create(CastToVectorNodeFactory.create(children[4], false, false, false, false), false, false, false);
        newChildren[5] = CastIntegerNodeFactory.create(CastToVectorNodeFactory.create(children[5], false, false, false, false), false, false, false);
        newChildren[6] = CastLogicalNodeFactory.create(CastToVectorNodeFactory.create(children[6], false, false, false, false), false, false, false);
        newChildren[7] = CastToVectorNodeFactory.create(children[7], false, false, false, false);
        return newChildren;
    }

    @SuppressWarnings("unused")
    @Specialization(order = 1, guards = "wrongArgsObject")
    String formatWrongArgs(Object value, RLogicalVector trimVec, RIntVector digitsVec, RIntVector nsmallVec, RIntVector widthVec, RIntVector justifyVec, RLogicalVector naEncodeVec,
                    RLogicalVector sciVec) {
        return null;
    }

    // TODO: handling of logical values resembles what happens in GNU R, with handling of other
    // types following suit at some point for compliance

    @SlowPath
    RStringVector convertToString(RAbstractLogicalVector value) {
        int width = formatLogical(value);
        String[] data = new String[value.getLength()];
        for (int i = 0; i < data.length; i++) {
            data[i] = PrettyPrinterNode.prettyPrint(value.getDataAt(i), width);
        }
        // vector is complete because strings are created by string builder
        return RDataFactory.createStringVector(data, RDataFactory.COMPLETE_VECTOR);
    }

    @SuppressWarnings("unused")
    @Specialization(order = 10, guards = "!wrongArgs")
    RStringVector format(VirtualFrame frame, RAbstractLogicalVector value, RLogicalVector trimVec, RIntVector digitsVec, RIntVector nsmallVec, RIntVector widthVec, RIntVector justifyVec,
                    RLogicalVector naEncodeVec, RAbstractVector sciVec) {
        if (value.getLength() == 0) {
            return RDataFactory.createEmptyStringVector();
        } else {
            return convertToString(value);
        }
    }

    int formatLogical(RAbstractLogicalVector value) {
        int width = 1;
        for (int i = 0; i < value.getLength(); i++) {
            byte val = value.getDataAt(i);
            if (RRuntime.isNA(val)) {
                width = Print.getRPrint().naWidth;
            } else if (val != RRuntime.LOGICAL_FALSE && width < 4) {
                width = 4;
            } else if (val == RRuntime.LOGICAL_FALSE && width < 5) {
                width = 5;
                break;
            }
        }
        return width;
    }

    // TODO: handling of other types re-uses our current formatting code in PrettyPrinterNode, which
    // should be sufficient for the time being but is likely not 100% compliant

    private static void addSpaces(String[] data, int width) {
        for (int i = 0; i < data.length; i++) {
            StringBuilder sb = new StringBuilder();
            data[i] = PrettyPrinterNode.spaces(sb, width - data[i].length()).append(data[i]).toString();
        }
    }

    @SlowPath
    RStringVector convertToString(RAbstractIntVector value) {
        String[] data = new String[value.getLength()];
        int width = 0;
        int widthChanges = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = PrettyPrinterNode.prettyPrint(value.getDataAt(i));
            if (width < data[i].length()) {
                width = data[i].length();
                widthChanges++;
            }
        }
        if (widthChanges > 1) {
            addSpaces(data, width);
        }
        // vector is complete because strings are created by string builder
        return RDataFactory.createStringVector(data, RDataFactory.COMPLETE_VECTOR);
    }

    @SuppressWarnings("unused")
    @Specialization(order = 20, guards = "!wrongArgs")
    RStringVector format(VirtualFrame frame, RAbstractIntVector value, RLogicalVector trimVec, RIntVector digitsVec, RIntVector nsmallVec, RIntVector widthVec, RIntVector justifyVec,
                    RLogicalVector naEncodeVec, RAbstractVector sciVec) {
        if (value.getLength() == 0) {
            return RDataFactory.createEmptyStringVector();
        } else {
            return convertToString(value);
        }
    }

    // TODO: even though format's arguments are not used at this point, their processing mirrors
    // what GNU R does

    private int computeSciArg(VirtualFrame frame, RAbstractVector sciVec) {
        assert sciVec.getLength() > 0;
        int tmp = castInteger(frame, sciVec).getDataAt(0);
        int ret;
        if (sciVec.getElementClass() == RLogical.class) {
            if (RRuntime.isNA(tmp)) {
                ret = tmp;
            } else {
                ret = tmp > 0 ? -100 : 100;
            }
        } else {
            ret = tmp;
        }
        if (!RRuntime.isNA(ret)) {
            Print.getRPrint().scipen = ret;
        }
        return ret;
    }

    @SlowPath
    RStringVector convertToString(RAbstractDoubleVector value) {
        String[] data = new String[value.getLength()];
        int width = 0;
        int widthChanges = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = PrettyPrinterNode.prettyPrint(value.getDataAt(i));
        }
        PrettyPrinterNode.padTrailingDecimalPointAndZeroesIfRequired(data);
        for (int i = 0; i < data.length; i++) {
            if (width < data[i].length()) {
                width = data[i].length();
                widthChanges++;
            }
        }
        if (widthChanges > 1) {
            addSpaces(data, width);
        }
        // vector is complete because strings are created by string builder
        return RDataFactory.createStringVector(data, RDataFactory.COMPLETE_VECTOR);
    }

    @SuppressWarnings("unused")
    @Specialization(order = 30, guards = "!wrongArgs")
    RStringVector format(VirtualFrame frame, RAbstractDoubleVector value, RLogicalVector trimVec, RIntVector digitsVec, RIntVector nsmallVec, RIntVector widthVec, RIntVector justifyVec,
                    RLogicalVector naEncodeVec, RAbstractVector sciVec) {
        byte trim = trimVec.getLength() > 0 ? trimVec.getDataAt(0) : RRuntime.LOGICAL_NA;
        int digits = digitsVec.getLength() > 0 ? digitsVec.getDataAt(0) : RRuntime.INT_NA;
        Print.getRPrint().digits = digits;
        int nsmall = nsmallVec.getLength() > 0 ? nsmallVec.getDataAt(0) : RRuntime.INT_NA;
        int width = widthVec.getLength() > 0 ? widthVec.getDataAt(0) : 0;
        int justify = justifyVec.getLength() > 0 ? justifyVec.getDataAt(0) : RRuntime.INT_NA;
        byte naEncode = naEncodeVec.getLength() > 0 ? naEncodeVec.getDataAt(0) : RRuntime.LOGICAL_NA;
        int sci = computeSciArg(frame, sciVec);
        if (value.getLength() == 0) {
            return RDataFactory.createEmptyStringVector();
        } else {
            return convertToString(value);
        }
    }

    // TruffleDSL bug - should not need multiple guards here
    protected boolean wrongArgsObject(@SuppressWarnings("unused") Object value, RLogicalVector trimVec, RIntVector digitsVec, RIntVector nsmallVec, RIntVector widthVec, RIntVector justifyVec,
                    RLogicalVector naEncodeVec, RAbstractVector sciVec) {
        if (trimVec.getLength() > 0 && RRuntime.isNA(trimVec.getDataAt(0))) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "trim");
        }
        if (digitsVec.getLength() > 0 && (RRuntime.isNA(digitsVec.getDataAt(0)) || digitsVec.getDataAt(0) < Print.R_MIN_DIGITS_OPT || digitsVec.getDataAt(0) > Print.R_MAX_DIGITS_OPT)) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "digits");
        }
        if (nsmallVec.getLength() > 0 && (RRuntime.isNA(nsmallVec.getDataAt(0)) || nsmallVec.getDataAt(0) < 0 || nsmallVec.getDataAt(0) > 20)) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "nsmall");
        }
        if (widthVec.getLength() > 0 && RRuntime.isNA(widthVec.getDataAt(0))) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "width");
        }
        if (justifyVec.getLength() > 0 && (RRuntime.isNA(justifyVec.getDataAt(0)) || justifyVec.getDataAt(0) < 0 || nsmallVec.getDataAt(0) > 3)) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "justify");
        }
        if (naEncodeVec.getLength() > 0 && RRuntime.isNA(naEncodeVec.getDataAt(0))) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "na.encode");
        }
        if (sciVec.getLength() != 1 || (sciVec.getElementClass() != RLogical.class && sciVec.getElementClass() != RInt.class && sciVec.getElementClass() != RDouble.class)) {
            throw RError.getInvalidArgument(getEncapsulatingSourceSection(), "scientific");
        }
        return false;
    }

    protected boolean wrongArgs(RAbstractVector value, RLogicalVector trimVec, RIntVector digitsVec, RIntVector nsmallVec, RIntVector widthVec, RIntVector justifyVec, RLogicalVector naEncodeVec,
                    RAbstractVector sciVec) {
        return wrongArgsObject(value, trimVec, digitsVec, nsmallVec, widthVec, justifyVec, naEncodeVec, sciVec);
    }
}
