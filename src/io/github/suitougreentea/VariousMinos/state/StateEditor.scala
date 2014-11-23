package io.github.suitougreentea.VariousMinos.state

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import scala.beans.BeanProperty
import org.newdawn.slick.Graphics
import javax.swing.JFileChooser
import io.github.suitougreentea.VariousMinos.Editor
import io.github.suitougreentea.VariousMinos.GameStageEditor

class StateEditor(@BeanProperty val ID: Int) extends BasicGameState {
  var editor: Editor = _
  var loaded = false
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    //loaded = false
    editor = sbg.asInstanceOf[GameStageEditor].editor
    //loaded = true
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    editor.render(g)  
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    editor.update(gc.getInput())
    gc.getInput().clearKeyPressedRecord()
  }
}