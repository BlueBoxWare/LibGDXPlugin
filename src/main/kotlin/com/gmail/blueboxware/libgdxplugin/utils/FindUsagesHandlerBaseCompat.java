package com.gmail.blueboxware.libgdxplugin.utils;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

/*
 * Copyright 2020 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// https://github.com/JetBrains/intellij-community/commit/0ac6e724c94bb94ccba51a378d89a4f86af2b8d5
abstract public class FindUsagesHandlerBaseCompat extends FindUsagesHandler {

    protected FindUsagesHandlerBaseCompat(@NotNull PsiElement psiElement) {
        super(psiElement);
    }

    @Override
    final public boolean processElementUsages(@NotNull PsiElement element, @NotNull Processor processor, @NotNull FindUsagesOptions options) {
        return doProcessElementUsages(element, processor, options);
    }

    public abstract boolean doProcessElementUsages(@NotNull PsiElement element, @NotNull Processor<UsageInfo> processor, @NotNull FindUsagesOptions options);

}
