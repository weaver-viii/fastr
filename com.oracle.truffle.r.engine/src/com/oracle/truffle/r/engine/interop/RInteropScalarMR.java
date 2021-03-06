/*
 * Copyright (c) 2016, 2018, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.engine.interop;

import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.ValueProfile;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.data.RInteropScalar;

@MessageResolution(receiverType = RInteropScalar.class)
public class RInteropScalarMR {

    @Resolve(message = "IS_BOXED")
    public abstract static class RInteropScalarIsBoxedNode extends Node {
        protected Object access(@SuppressWarnings("unused") RInteropScalar receiver) {
            return true;
        }
    }

    @Resolve(message = "UNBOX")
    public abstract static class RInteropScalarUnboxNode extends Node {
        private final ValueProfile classProfile = ValueProfile.createClassProfile();

        protected Object access(RInteropScalar receiver) {
            RInteropScalar t = classProfile.profile(receiver);
            if (t instanceof RInteropScalar.RInteropByte) {
                return ((RInteropScalar.RInteropByte) t).getValue();
            } else if (t instanceof RInteropScalar.RInteropChar) {
                return ((RInteropScalar.RInteropChar) t).getValue();
            } else if (t instanceof RInteropScalar.RInteropFloat) {
                return ((RInteropScalar.RInteropFloat) t).getValue();
            } else if (t instanceof RInteropScalar.RInteropLong) {
                return ((RInteropScalar.RInteropLong) t).getValue();
            } else if (t instanceof RInteropScalar.RInteropShort) {
                return ((RInteropScalar.RInteropShort) t).getValue();
            }
            throw RInternalError.unimplemented("missing RInteropScalar : " + receiver.getClass().getName());
        }
    }

    @Resolve(message = "KEY_INFO")
    public abstract static class RInteropScalarKeyInfoNode extends Node {
        protected Object access(@SuppressWarnings("unused") TruffleObject receiver, @SuppressWarnings("unused") Object identifier) {
            return 0;
        }
    }

    @CanResolve
    public abstract static class RInteropScalarCheck extends Node {
        protected static boolean test(TruffleObject receiver) {
            return receiver instanceof RInteropScalar;
        }
    }
}
