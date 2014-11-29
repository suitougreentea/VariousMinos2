package io.github.suitougreentea.VariousMinos

import io.github.suitougreentea.VariousMinos.game.{HandlerBomb, Game}
import org.newdawn.slick.{Graphics, Input, Color}

/**
 * Created by suitougreentea on 14/11/29.
 */
trait ModeMenuItem {
  val name: String
  //val description: String = ""
  val color: Color
  val height: Int

  def updateDetailedMenu (c: Control): Unit

  def renderDetailedMenu (g: Graphics): Unit

  def handler: HandlerBomb
}

abstract class ModeMenuItemWithDifficulty(val name: String, val color: Color) extends ModeMenuItem {
  var cursor = 0

  val height = 32

  val difficultyList: List[String]

  override def updateDetailedMenu(c: Control): Unit = {
    if(c.pressed(Buttons.LEFT) && cursor > 0) cursor -= 1
    if(c.pressed(Buttons.RIGHT) && cursor < difficultyList.length - 1) cursor += 1
  }

  override def renderDetailedMenu(g: Graphics): Unit = {
    Resource.boldfont.drawString("- " + difficultyList(cursor), 16, 16, color = new Color(0.8f, 0.8f, 0.8f))
  }
}

class ModeMenuItemWithNothing(val name: String, val color: Color, _handler: => HandlerBomb) extends ModeMenuItem {
  val height = 0

  def updateDetailedMenu (c: Control): Unit = {}

  def renderDetailedMenu (g: Graphics): Unit = {}

  lazy val handler = _handler
}
