/*
 * This file is part of lamp, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the seconds
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package org.lushplugins.placeholderhandler.util.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class MethodHandlesCallerFactory implements MethodCallerFactory {

    public static final MethodHandlesCallerFactory INSTANCE = new MethodHandlesCallerFactory();

    @Override
    public @NotNull MethodCaller createFor(@NotNull Method method) throws Throwable {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        MethodHandle handle = MethodHandles.lookup().unreflect(method);
        String methodString = method.toString();
        boolean isStatic = Modifier.isStatic(method.getModifiers());

        return new MethodCaller() {

            @Override
            public Object call(@Nullable Object instance, Object... arguments) {
                if (!isStatic) {
                    List<Object> args = new ArrayList<>();
                    args.add(instance);
                    Collections.addAll(args, arguments);
                    try {
                        return handle.invokeWithArguments(args);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    return handle.invokeWithArguments(arguments);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }

            @Override public String toString() {
                return "MethodHandlesCaller(" + methodString + ")";
            }
        };
    }

    @Override public String toString() {
        return "MethodHandlesCallerFactory";
    }
}
