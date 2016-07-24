import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.graphics.profiling.GLProfiler as GLP
import com.badlogic.gdx.utils.PerformanceCounter
import com.badlogic.gdx.utils.PerformanceCounters

private class Clazz {
  val p = FPSLogger()
}

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
fun test1() {
  <warning>FPSLogger().log()</warning>

  val p = FPSLogger()

  <warning>p.log()</warning>

  p.let {
    <warning>it.log()</warning>
    <warning>GLProfiler.enable()</warning>
  }

  <warning>Clazz().p.log()</warning>

}

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
fun test2() {

  val pc = PerformanceCounter("no")

  <warning>PerformanceCounter("yes").start()</warning>

  val f = {
    <warning>pc.tick(3.0f)</warning>
  }

  pc.let { abc ->
    <warning>abc.reset()</warning>
  }


}

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
fun test3(p: PerformanceCounter) {
  <warning>p.stop()</warning>

  <warning>PerformanceCounters().tick()</warning>

  <warning>PerformanceCounters().counters[0].stop()</warning>

  <warning>GLP.enable()</warning>

}

