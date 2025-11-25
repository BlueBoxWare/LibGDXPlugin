package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiElement


@Suppress("unused")
internal fun PsiElement.isInGradleKotlinBuildFile() =
    FileUtilRt.extensionEquals(containingFile.name, "gradle.kts") ||
            (FileUtilRt.extensionEquals(containingFile.name, "gradle.kt")
                    && ApplicationManager.getApplication().isUnitTestMode
                    )

internal fun PsiElement.isInGradlePropertiesFile() =
    containingFile.name == "gradle.properties"
