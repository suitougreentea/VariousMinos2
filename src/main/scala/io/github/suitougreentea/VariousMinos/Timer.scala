package io.github.suitougreentea.VariousMinos

/**
 * Created by suitougreentea on 14/12/04.
 */
class Timer {
  var stackTime: Long = 0
  var startTime: Long = -1
  var stopped = true
  def start(): Unit = {
    startTime = System.currentTimeMillis()
    stopped = false
  }
  def stop(): Unit = {
    if(startTime != -1) stackTime += System.currentTimeMillis() - startTime
    stopped = true
  }
  def time = if(stopped) stackTime else stackTime + System.currentTimeMillis() - startTime

  def mkString(formatter: String) = formatter.format(time / (60 * 1000), time % (60 * 1000) / 1000, time % 1000 / 10, time % 1000)
}
