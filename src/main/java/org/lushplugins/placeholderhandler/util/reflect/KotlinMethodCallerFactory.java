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
import org.lushplugins.placeholderhandler.util.reflect.ktx.KotlinConstants;
import org.lushplugins.placeholderhandler.util.reflect.ktx.KotlinFunction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class KotlinMethodCallerFactory implements MethodCallerFactory {

    public static final KotlinMethodCallerFactory INSTANCE = new KotlinMethodCallerFactory();

    @Override public @NotNull MethodCaller createFor(@NotNull Method method) {
        KotlinFunction function = KotlinFunction.wrap(method);
        return (instance, arguments) -> {
            List<Object> list = new ArrayList<>();
            Collections.addAll(list, arguments);
            list.replaceAll(v -> v == KotlinConstants.ABSENT_VALUE ? null : v);
            return function.call(
                instance,
                list,
                parameter -> false
            );
        };
    }
}
