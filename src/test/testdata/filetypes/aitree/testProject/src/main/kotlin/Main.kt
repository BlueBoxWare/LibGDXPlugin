import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import org.example.Dog
import java.io.File

fun main() {

  File("data").listFiles { file -> file.extension == "tree" }!!.forEach { file ->
    val parser = BehaviorTreeParser<Dog>(BehaviorTreeParser.DEBUG_HIGH)
    println()
    println(file.name)
    println()
    val tree = parser.parse(file.inputStream(), Dog("Buddy"))
  }

}
