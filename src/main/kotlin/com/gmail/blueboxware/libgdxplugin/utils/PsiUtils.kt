package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.impl.LibraryScopeCache
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.InheritanceUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.TypeConversionUtil
import com.intellij.util.PathUtil
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.idea.base.psi.deleteSingle
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.intentions.calleeName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import org.jetbrains.kotlin.psi.psiUtil.isPlainWithEscapes
import org.jetbrains.kotlin.psi.psiUtil.plainContent
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.getAllSuperclassesWithoutAny
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrCommandArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrString
import org.jetbrains.plugins.groovy.lang.psi.impl.statements.expressions.literals.GrLiteralImpl

/*
 * Copyright 2017 Blue Box Ware
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
private const val ARRAY_CLASS_NAME = "com.badlogic.gdx.utils.Array"
private const val QUEUE_CLASS_NAME = "com.badlogic.gdx.utils.Queue"
private const val COLLECTION_CLASS_NAME = "java.util.Collection"

private val CONTAINER_CLASS_NAMES = listOf(
    ARRAY_CLASS_NAME,
    QUEUE_CLASS_NAME,
    COLLECTION_CLASS_NAME
)

internal fun Project.psiFacade() = JavaPsiFacade.getInstance(this)

@Suppress("UnusedReceiverParameter")
internal fun PsiElement.removeChild(child: PsiElement) = child.deleteSingle()

internal fun PsiElement.isFollowByNewLine() = node.treeNext?.isNewline() ?: false

internal fun PsiElement.isPrecededByNewline() = node.treePrev?.isNewline() ?: false

internal fun ASTNode.isNewline() =
    elementType == TokenType.WHITE_SPACE && text.contains('\n')

internal fun PsiElement.isLeaf(types: TokenSet): Boolean =
    (this as? LeafPsiElement)?.elementType in types

internal fun PsiElement.isLeaf(vararg types: IElementType): Boolean =
    (this as? LeafPsiElement)?.elementType in types

@Suppress("unused")
internal inline fun <reified T : PsiElement> PsiElement.contextOfType(): T? =
    PsiTreeUtil.getContextOfType(this, T::class.java)

internal inline fun <reified T : PsiElement> PsiElement.childOfType(): T? =
    PsiTreeUtil.findChildOfType(this, T::class.java)

internal inline fun <reified T : PsiElement> PsiElement.childrenOfType(): Collection<T> =
    PsiTreeUtil.findChildrenOfType(this, T::class.java)

internal inline fun <reified T : PsiElement> PsiElement.firstParent(): T? =
    firstParent { it is T } as? T

internal fun PsiElement.firstParent(condition: (PsiElement) -> Boolean) =
    firstParent(true, condition)

internal fun PsiElement.firstParent(includeSelf: Boolean, condition: (PsiElement) -> Boolean): PsiElement? {

    var element = if (includeSelf) this else this.parent

    while (element != null) {
        if (condition(element)) {
            return element
        }
        element = element.parent
    }

    return null
}

internal fun PsiElement.lastChild(condition: (PsiElement) -> Boolean): PsiElement? {

    var node = node.lastChildNode

    while (node != null) {
        if (condition(node.psi)) {
            return node.psi
        }
        node = node.treePrev
    }

    return null

}

internal fun PsiClass.supersAndThis() =
    InheritanceUtil.getSuperClasses(this) + this

internal fun PsiClass.getType() =
    PsiElementFactory.getInstance(project).createType(this, PsiSubstitutor.EMPTY)

internal fun PsiType.componentType(project: Project): PsiType? {
    if (this is PsiArrayType) {
        return componentType
    } else if (this is PsiClassReferenceType) {
        val scope = LibraryScopeCache.getInstance(project).librariesOnlyScope
        for (fqName in CONTAINER_CLASS_NAMES) {
            project.psiFacade().findClasses(fqName, scope).forEach { clazz ->
                if (TypeConversionUtil.isAssignable(clazz.getType(), this.rawType())) {
                    return this.parameters.firstOrNull()
                }
            }
        }
    }
    return null
}

internal fun ClassDescriptor.supersAndThis() =
    getAllSuperclassesWithoutAny() + this

internal fun PsiElement.findParentWhichIsChildOf(childOf: PsiElement): PsiElement? {
    var element: PsiElement? = this
    while (element != null && element.parent != childOf) {
        element = element.parent
    }
    return element
}

internal inline fun <reified T : PsiElement, reified U : PsiElement> PsiElement.parentWithParent(includeSelf: Boolean = true): T? {
    var element: PsiElement? = this

    if (!includeSelf) {
        element = element?.parent
    }

    while (element != null) {
        if (element is T && element.parent is U) {
            return element
        }
        element = element.parent
    }
    return null
}

internal fun GrCommandArgumentList.getNamedArgument(name: String): GrExpression? =
    namedArguments.find { trimQuotes(it.labelName) == name }?.expression

internal fun KtValueArgumentList.getNamedArgument(name: String): KtExpression? =
    arguments.find { it.getArgumentName()?.asName?.asString() == name }?.getArgumentExpression()

internal fun PsiLiteralExpression.asString(): String? =
    (value as? String)?.toString()

internal fun KtStringTemplateExpression.asPlainString(): String? =
    if (isPlainWithEscapes()) plainContent else null

internal fun GrLiteral.asString(): String? =
    takeIf { (it as? GrLiteralImpl)?.isStringLiteral == true }?.value as? String
        ?: takeIf { (it as? GrString)?.isPlainString == true }?.text?.let(::trimQuotes)


internal fun PsiType?.isStringType(element: PsiElement) =
    this == PsiType.getJavaLangString(element.manager, element.resolveScope)

internal fun KtElement.analyzePartial() = analyze(BodyResolveMode.PARTIAL)

internal fun Project.getPsiFile(filename: String): PsiFile? {

    getProjectBaseDir()?.findFileByRelativePath(PathUtil.toSystemIndependentName(filename))?.let { virtualFile ->
        return PsiManager.getInstance(this).findFile(virtualFile)
    }

    return null

}

internal fun PsiClass.findAllInnerClasses(): List<PsiClass> {

    val result = mutableListOf(this)

    for (innerClass in innerClasses) {
        if (innerClass !is KtLightClass || innerClass.kotlinOrigin !is KtObjectDeclaration) {
            result.addAll(innerClass.findAllInnerClasses())
        }
    }

    return result

}

internal fun PsiClass.findAllStaticInnerClasses(): List<PsiClass> {
    val result = mutableListOf(this)

    for (innerClass in innerClasses) {
        if (innerClass.hasModifierProperty(PsiModifier.STATIC)
            && (innerClass !is KtLightClass
                    || innerClass.kotlinOrigin !is KtObjectDeclaration
                    )
        ) {
            result.addAll(innerClass.findAllStaticInnerClasses())
        }
    }

    return result
}

internal fun PsiMethodCallExpression.resolveCall(): Pair<PsiClass, PsiMethod>? {

    methodExpression.qualifierExpression?.let { qualifierExpression ->

        (qualifierExpression.type as? PsiClassType)?.resolve().let {

            (it ?: (qualifierExpression as? PsiReference)?.resolve() as? PsiClass)?.let { clazz ->

                resolveMethod()?.let { method ->
                    return Pair(clazz, method)
                }

            }

        }

    }

    return null

}

internal fun PsiMethodCallExpression.resolveCallToStrings(): Pair<String, String>? =
    resolveCall()?.let {
        it.first.qualifiedName?.let { className -> Pair(className, it.second.name) }
    }

internal fun KtQualifiedExpression.resolveCall(): Pair<DeclarationDescriptor, String>? {

    var receiverType: DeclarationDescriptor? = analyze().getType(receiverExpression)?.constructor?.declarationDescriptor

    if (receiverType == null) {
        // static method call?
        receiverType = receiverExpression.getReferenceTargets(analyze()).firstOrNull() ?: return null
    }

    val methodName = calleeName ?: return null

    return Pair(receiverType, methodName)

}

internal fun KtQualifiedExpression.resolveCallToStrings(): Pair<String, String>? =
    resolveCall()?.let {
        Pair(it.first.fqNameSafe.asString(), it.second)
    }

internal fun KtCallExpression.resolveCall(): Pair<ClassDescriptor, KtNameReferenceExpression>? {

    (context as? KtQualifiedExpression)?.let { dotExpression ->

        val bindingContext = dotExpression.analyze()
        val type = bindingContext.getType(dotExpression.receiverExpression)
        var receiverType: ClassDescriptor? = type?.constructor?.declarationDescriptor as? ClassDescriptor

        if (receiverType == null) {
            // static method call?
            receiverType = dotExpression
                .receiverExpression
                .getReferenceTargets(bindingContext)
                .firstOrNull() as? ClassDescriptor
                ?: return null
        }

        val methodName = calleeExpression as? KtNameReferenceExpression ?: return null

        return Pair(receiverType, methodName)

    }

    return null

}

internal fun KtCallExpression.resolveCallToStrings(): Pair<String, String>? =
    resolveCall()?.let {
        Pair(it.first.fqNameSafe.asString(), it.second.getReferencedName())
    }

internal fun KotlinType.fqName() = constructor.declarationDescriptor?.fqNameSafe?.asString()

internal fun PsiElement.findLeaf(elementType: IElementType): LeafPsiElement? =
    allChildren.firstOrNull { it.isLeaf(elementType) } as? LeafPsiElement

@Suppress("unused")
internal fun PsiElement.findElement(condition: (PsiElement) -> Boolean): PsiElement? {

    val processor = object : PsiElementProcessor.FindElement<PsiElement>() {
        override fun execute(element: PsiElement): Boolean {
            return if (condition(element)) {
                setFound(element)
                false
            } else {
                true
            }
        }
    }
    PsiTreeUtil.processElements(this, processor)
    return processor.foundElement

}

internal inline fun <reified T : PsiElement> PsiElement.getParentOfType(strict: Boolean = true): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, strict)

internal fun terminatedOnCurrentLine(editor: Editor, element: PsiElement): Boolean {
    val document = editor.document
    val caretOffset = editor.caretModel.currentCaret.offset
    val elementEndOffset = element.textRange.endOffset
    if (document.getLineNumber(elementEndOffset) != document.getLineNumber(caretOffset)) {
        return false
    }
    val nextLeaf = PsiTreeUtil.nextLeaf(element, true)
    return nextLeaf == null || (nextLeaf is PsiWhiteSpace && nextLeaf.text.contains("\n"))
}

internal fun isFollowedByTerminal(element: PsiElement, type: IElementType): Boolean {
    val nextLeaf = PsiTreeUtil.nextVisibleLeaf(element)
    return nextLeaf != null && nextLeaf.node.elementType == type
}
