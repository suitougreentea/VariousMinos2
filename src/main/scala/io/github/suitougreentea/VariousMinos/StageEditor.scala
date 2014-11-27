package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.AppGameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import javax.swing.JFileChooser
import io.github.suitougreentea.VariousMinos.state.StateEditor
import io.github.suitougreentea.VariousMinos.state.StateEditorMenu
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import java.io.File
import io.github.suitougreentea.VariousMinos.editor.Editor

object StageEditor {
  def main(args: Array[String]) {
    val game = new GameStageEditor("StageEditor")
    val app = new AppGameContainer(game)
    app.setDisplayMode(800, 600, false)
    app.setTargetFrameRate(60)
    app.start()
  }
}

class GameStageEditor(val name: String) extends StateBasedGame(name) {
  var editor: Editor[_] = _
  this.addState(new StateEditorMenu(0))
  this.addState(new StateEditor(1))
  this.enterState(0)
  
  def initStatesList(gc: GameContainer) {
    this.getState(0).init(gc, this)
    this.getState(1).init(gc, this)
  }
}