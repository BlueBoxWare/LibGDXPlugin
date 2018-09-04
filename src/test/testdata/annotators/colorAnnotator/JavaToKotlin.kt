import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors

val abc1 = <weak_warning descr="#708090ff">Color.SLATE</weak_warning>
val abcc2 = Color(<weak_warning descr="#708090ff">abc1</weak_warning>)
val abc3 =Color(Color(Color(<weak_warning descr="#ff0000ff">Color.valueOf(JavaToKotlin.c1)</weak_warning>)));
val abc4 = <weak_warning descr="#00ff00ff">JavaToKotlin.c2</weak_warning>;

val ab2 = <weak_warning descr="#00ff00ff">JavaToKotlin.c2</weak_warning>
val ab3 = <weak_warning descr="#0000ffff">JavaToKotlin.cf3</weak_warning>
val ab4 = <weak_warning descr="#00ff00ff">JavaToKotlin.c4</weak_warning>
val ab5 = <weak_warning descr="#0000ffff">JavaToKotlin.c5</weak_warning>
val ab6 = <weak_warning descr="#ff0000ff">JavaToKotlin.c6</weak_warning>
val ab7 = <weak_warning descr="#00001168">JavaToKotlin.c7</weak_warning>
val ab8 = <weak_warning descr="#ff0000ff">JavaToKotlin.c8</weak_warning>
val ab9 = <weak_warning descr="#ff00ffff">JavaToKotlin.c9</weak_warning>
val ab10 = <weak_warning descr="#ffff00ff">JavaToKotlin.c10</weak_warning>
val ab11 = <weak_warning descr="#ff0000ff">JavaToKotlin.c11</weak_warning>

val abffffk8 = <weak_warning descr="#ff0000ff">Color.valueOf(JavaToKotlin.C.c1)</weak_warning>;
val dffd = <weak_warning descr="#ffff00ff">Color(Color.RED.r, Color.RED.r, Color.RED.b, 1f)</weak_warning>