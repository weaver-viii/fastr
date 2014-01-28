/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
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

import java.util.*;

import com.oracle.truffle.api.CompilerDirectives.SlowPath;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;

@RBuiltin(value = "commandArgs")
public abstract class CommandArgs extends RBuiltinNode {

    @Specialization
    public RStringVector commandArgs(byte trailingOnly) {
        return getCommandArgs(trailingOnly == RRuntime.LOGICAL_TRUE);
    }

    @SlowPath
    private static RStringVector getCommandArgs(boolean trailingOnly) {
        String[] s = RContext.getInstance().getCommandArgs();
        if (trailingOnly) {
            int index = s.length;
            for (int i = 0; i < s.length; ++i) {
                if (s.equals("--args")) {
                    index = i;
                    break;
                }
            }
            s = Arrays.copyOfRange(s, index, s.length);
        }
        return RDataFactory.createStringVector(s, RDataFactory.COMPLETE_VECTOR);
    }

    @SuppressWarnings("unused")
    @Specialization
    public RStringVector commandArgs(RNull vector) {
        return getCommandArgs(false);
    }

}
