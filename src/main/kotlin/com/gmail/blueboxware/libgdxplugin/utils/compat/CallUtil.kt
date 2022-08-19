package com.gmail.blueboxware.libgdxplugin.utils.compat

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingContext.CALL
import org.jetbrains.kotlin.resolve.BindingContext.RESOLVED_CALL
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.VariableAsFunctionResolvedCall
import org.jetbrains.kotlin.types.KotlinType

// TODO: Remove when Android Studio has Kotlin 1.6.20

/*
 * Taken from https://github.com/JetBrains/kotlin/blob/ee728b6902ee669a36406afe720131a3d9a10d21/compiler/frontend/src/org/jetbrains/kotlin/resolve/calls/util/callUtil.kt
 *
 * Copyright 2010-2017 JetBrains s.r.o.
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
fun KtElement?.getCalleeExpressionIfAny(): KtExpression? {
    return when (val element = if (this is KtExpression) KtPsiUtil.deparenthesize(this) else this) {
        is KtSimpleNameExpression -> element
        is KtCallElement -> element.calleeExpression
        is KtQualifiedExpression -> element.selectorExpression.getCalleeExpressionIfAny()
        is KtOperationExpression -> element.operationReference
        else -> null
    }
}

fun KtElement.getCall(context: BindingContext): Call? {
    val element = (if (this is KtExpression) KtPsiUtil.deparenthesize(this) else this) ?: return null

    // Do not use Call bound to outer call expression (if any) to prevent stack overflow during analysis
    if (element is KtCallElement && element.calleeExpression == null) return null

    if (element is KtWhenExpression) {
        val subjectVariable = element.subjectVariable
        if (subjectVariable != null) {
            return subjectVariable.getCall(context)
        }
    }

    val reference: KtExpression? = when (val parent = element.parent) {
        is KtInstanceExpressionWithLabel -> parent
        is KtUserType -> parent.parent.parent as? KtConstructorCalleeExpression
        else -> element.getCalleeExpressionIfAny()
    }
    if (reference != null) {
        return context[CALL, reference]
    }
    return context[CALL, element]
}

fun Call?.getResolvedCall(context: BindingContext): ResolvedCall<out CallableDescriptor>? {
    return context[RESOLVED_CALL, this]
}

fun KtExpression.getType(context: BindingContext): KotlinType? {
    val type = context.getType(this)
    if (type != null) return type
    val resolvedCall = this.getResolvedCall(context)
    if (resolvedCall is VariableAsFunctionResolvedCall) {
        return resolvedCall.variableCall.resultingDescriptor.type
    }
    return null
}

fun KtElement?.getResolvedCall(context: BindingContext): ResolvedCall<out CallableDescriptor>? {
    return this?.getCall(context)?.getResolvedCall(context)
}
