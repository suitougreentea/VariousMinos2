

/**
 * @author suitougreentea
 */
package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.AppGameContainer

object VariousMinos {
  def main(args: Array[String]) {
    val game = new GameVariousMinos("VariousMinos2")
    val app = new AppGameContainer(game)
    app.setDisplayMode(800, 600, false)
    app.setTargetFrameRate(60)
    app.start()
  }
}
