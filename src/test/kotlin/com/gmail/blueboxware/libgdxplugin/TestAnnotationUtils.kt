package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.utils.getAnnotation
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import junit.framework.TestCase
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtCallExpression


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
class TestAnnotationUtils: LibGDXCodeInsightFixtureTestCase() {

  data class Test(val content: String, val annotationParameter: String, val expectedResult: List<String>?)

  private val javaTests = listOf(
          //
          // Java to Java
          //
          Test("""
            @MyAnnotation
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "arg", listOf("")),
          Test("""
            void m() {
              @MyAnnotation(arg = "4")
              String s;
              s.char<caret>At(0);
            }
            """, "arg", listOf("4")),
          Test("""
            @MyAnnotation
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "args", listOf("")),
          Test("""
            @MyAnnotation
            String s;

            void m() {
              (s).ch<caret>arAt(3);
            }
            """, "args", listOf("")),
          Test("""
            @MyAnnotation
            String s;

            void m() {
              ((String)s).ch<caret>arAt(3);
            }
            """, "args", listOf("")),
          Test("""
            @MyAnnotation
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "argNoDefault", listOf()),
          Test("""
            @MyAnnotation
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "argsNoDefault", listOf()),
          Test("""
            @MyAnnotation(arg = "Foo")
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "arg", listOf("Foo")),
          Test("""
            @MyAnnotation(args = {"Foo", "Bar"})
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "args", listOf("Foo", "Bar")),
          Test("""
            @MyAnnotation(arg = {"Foo", "Bar"})
            String s;

            void m() {
              s.ch<caret>arAt(3);
            }
            """, "arg", listOf("Foo", "Bar")),
          Test("""
            @MyAnnotation(args = {"Foo", "Bar"})
            String s;

            void m() {
              this.s.ch<caret>arAt(3);
            }
            """, "args", listOf("Foo", "Bar")),
          Test("""
            void m() {
              JavaClass.INSTANCE.m<caret>();
            }
            """, "args", listOf("Foo", "Bar")),
          Test("""
            void m() {
              JavaClass.INSTANCE.m<caret>();
            }
            """, "argNoDefault", listOf("")),
          Test("""
            void m() {
              new JavaClass().javaClass.m<caret>();
            }
            """, "argsNoDefault", listOf()),
          Test("""
            void m() {
              new JavaClass().javaClass.m<caret>();
            }
            """, "args", listOf("one", "two")),
          Test("""
            void m() {
              ((JavaClass)((JavaClass)new JavaClass().javaClass).javaClass).m<caret>();
            }
            """, "args", listOf("one", "two")),
          Test("""
            void m() {
              new JavaClass().annotated<caret>Method();
            }
            """, "args", null),
          Test("""
            void m() {
              new JavaClass().annoSubClass.m<caret>();
            }
            """, "args", listOf("sub")),
          Test("""
            void m() {
              new JavaClass().f().t.charAt<caret>();
            }
            """, "arg", listOf("t")),
          Test("""
            void m() {
              JavaClass.g().t.charAt<caret>();
            }
            """, "arg", listOf("t")),

          //
          // Java to Kotlin
          //
          Test("""
            void m() {
              KotlinClassKt.getString().char<caret>At(1);
            }
            """, "argsNoDefault", listOf("foo", "oof")),
          Test("""
            void m() {
              KotlinClassKt.getString().char<caret>At(1);
            }
            """, "arg", listOf("bar")),
          Test("""
            void m() {
              KotlinClassKt.getString().char<caret>At(1);
            }
            """, "args", listOf()),
//          TODO: Make defaults available in Kotlin code
//          Test("""
//            void m() {
//              KotlinClassKt.getString().char<caret>At(1);
//            }
//            """, "arg", listOf(""))
          Test("""
            void m() {
              KotlinClassKt.f().annotatedMethod<caret>();
            }
            """, "args", null),
          Test("""
            void m() {
              KotlinObject.INSTANCE.annotatedMethod<caret>();
            }
            """, "args", null),
          Test("""
            void m() {
              new KotlinClass().getString().char<caret>At(1);
            }
            """, "args", listOf("a", "b")),
          Test("""
            void m() {
              new KotlinClass().getKotlinClass().m<caret>();
            }
            """, "args", listOf("a", "b")),
          Test("""
            void m() {
              KotlinClass.Companion.getS().char<caret>At(1);
            }
            """, "argsNoDefault", listOf("xyz")),
          Test("""
            void m() {
              KotlinClass.Companion.f().getString().char<caret>At(0);
            }
            """, "args", listOf("a", "b")),
          Test("""
            void m() {
              KotlinObject.getS().char<caret>At(0);
            }
            """, "argNoDefault", null),
          Test("""
            void m() {
              KotlinObject.getT().char<caret>At(0);
            }
            """, "arg", listOf("xyz")),
          Test("""
            void m() {
              new SubClass().getString().char<caret>At(0);
            }
            """, "args", listOf("a", "b"))
  )

  private val kotlinTests = listOf(
          //
          // Kotlin to Kotlin
          //

          Test("""
              @MyAnnotation(args = arrayOf("var"))
              val s = ""
              s?.char<caret>At(0)
            """, "args", listOf("var")),
          Test("""
              @MyAnnotation(args = arrayOf("var"))
              val s = ""
              s.char<caret>At(0)
            """, "args", listOf("var")),
          Test("""
              @MyAnnotation(args = arrayOf("var"))
              val s = ""
              (s as String).char<caret>At(0)
            """, "args", listOf("var")),
          Test("""
              @MyAnnotation(args = arrayOf("var"))
              val s = ""
              (((s) as String) as String).char<caret>At(0)
            """, "args", listOf("var")),
          Test("""
              String.char<caret>At(0)
            """, "argsNoDefault", listOf("foo", "oof")),
          Test("""
              String?.char<caret>At(0)
            """, "argsNoDefault", listOf("foo", "oof")),
          Test("""
              String!!.char<caret>At(0)
            """, "argsNoDefault", listOf("foo", "oof")),
          Test("""
              KotlinObject.m<caret>()
            """, "argNoDefault", null),
          Test("""
              KotlinObject.annotatedMethod<caret>()
            """, "argNoDefault", null),
          Test("""
              KotlinObject.s.char<caret>At(0)
            """, "argNoDefault", null),
          Test("""
              KotlinObject.t.char<caret>At(0)
            """, "arg", listOf("xyz")),
          Test("""
              KotlinObject?.t?.char<caret>At(0)
            """, "arg", listOf("xyz")),
          Test("""
              KotlinClass().string.char<caret>At(0)
            """, "args", listOf("a", "b")),
          Test("""
              KotlinObject.f().string.char<caret>At(0)
            """, "args", listOf("a", "b")),
          Test("""
              KotlinClass.s.char<caret>At(0)
            """, "argsNoDefault", listOf("xyz")),
          Test("""
              KotlinClass.f().string.char<caret>At(0)
            """, "args", listOf("a", "b")),
          Test("""
              KotlinClass.f().kotlinClass.m<caret>(0)
            """, "args", listOf("a", "b")),
          Test("""
              KotlinClass().kotlinClass.m<caret>(0)
            """, "args", listOf("a", "b")),
          Test("""
              KotlinClass().kotlinClass.kotlinClassNA.kotlinClass.kotlinClass.m<caret>()
            """, "args", listOf("a", "b")),
          Test("""
              KotlinClass().kotlinClass?.kotlinClassNA?.kotlinClass?.kotlinClass?.m<caret>()
            """, "args", listOf("a", "b")),
          Test("""
              KotlinClass()!!.kotlinClass!!.kotlinClassNA!!.kotlinClass!!.kotlinClass!!.m<caret>()
            """, "args", listOf("a", "b")),
          Test("""
              (((KotlinClass()!!.kotlinClass as? KotlinClass)!!.kotlinClassNA!!.kotlinClass!!) as (KotlinClass)).kotlinClass!!.m<caret>()
            """, "args", listOf("a", "b")),
          Test("""
              SubClass().kotlinClass.m<caret>()
            """, "args", listOf("a", "b")),

          //
          // Kotlin to Java
          //
          Test("""
              JavaClass().javaClass.m<caret>()
            """, "args", listOf("one", "two")),
          Test("""
              JavaClass.INSTANCE.m<caret>()
            """, "args", listOf("Foo", "Bar")),
          Test("""
              JavaClass().javaClass.annoSubClass.t.charAt<caret>()
            """, "arg", listOf("t")),
          Test("""
              JavaClass().f().t.charAt<caret>()
            """, "arg", listOf("t")),
          Test("""
              JavaClass.INSTANCE.m()
            """, "arg", listOf("arg")),
          Test("""
              JavaClass.INSTANCE.subClass.t.charAt<caret>()
            """, "arg", listOf("t")),
          Test("""
              JavaClass.INSTANCE?.subClass?.t?.charAt<caret>()
            """, "arg", listOf("t")),
          Test("""
              JavaClass.g().annoSubClass.m<caret>()
            """, "args", listOf("sub")),
          Test("""
              JavaClass.g()?.annoSubClass?.m<caret>()
            """, "args", listOf("sub")),
          Test("""
              JavaClass.g()!!.annoSubClass!!.m<caret>()
            """, "args", listOf("sub"))
  )

  fun testKotlin() {
    for ((content1, annotationParameter, expectedResult) in kotlinTests) {
      val content = """
      fun f() {
        $content1
      }
      """
      myFixture.configureByText(KotlinFileType.INSTANCE, content)
      val msg = "Parameter: $annotationParameter, Contents:\n$content"
      doTest(expectedResult, msg, annotationParameter)
    }
  }

  fun testJava() {

    for ((content1, annotationParameter, expectedResult) in javaTests) {
      val content = """
        class Test {
          $content1
        }
      """
      myFixture.configureByText(JavaFileType.INSTANCE, content)
      val msg = "Parameter: $annotationParameter, Contents:\n$content"
      doTest(expectedResult, msg, annotationParameter)
    }

  }

  fun doTest(expectedResult: List<String>?, msg: String, parameter: String) {
    val element = myFixture.file.findElementAt(myFixture.caretOffset)?.let {
      PsiTreeUtil.findFirstParent(it, { it is PsiMethodCallExpression || it is KtCallExpression })
    } ?: throw AssertionError(msg)
    val annotationClass = JavaPsiFacade.getInstance(project).findClass("MyAnnotation", GlobalSearchScope.allScope(project)) ?: throw AssertionError()
    val annotation = (element as? PsiMethodCallExpression)?.getAnnotation(annotationClass) ?: (element as? KtCallExpression)?.getAnnotation(annotationClass)
    if (annotation == null) {
      if (expectedResult != null) {
        TestCase.fail("Annotation not found. $msg")
      }
    } else {
      val result = annotation.getValue(parameter)
      TestCase.assertEquals(msg, expectedResult, result)
    }
  }

  override fun setUp() {
    super.setUp()

    addKotlin()

    myFixture.copyFileToProject("JavaClass.java")
    myFixture.copyFileToProject("KotlinClass.kt")
    myFixture.copyFileToProject("MyAnnotation.java")
  }

  override fun getBasePath() = "annotationUtils/"

}