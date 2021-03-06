package io.github.suitougreentea.VariousMinos.state

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import scala.beans.BeanProperty
import org.newdawn.slick.Graphics
import javax.swing.JFileChooser
import io.github.suitougreentea.VariousMinos.GameStageEditor
import io.github.suitougreentea.VariousMinos.editor.Editor

class StateEditor(@BeanProperty val ID: Int) extends BasicGameState {
  var editor: Editor[_] = _
  var loaded = false
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    editor = sbg.asInstanceOf[GameStageEditor].editor
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    editor.render(g)  
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    editor.update(gc.getInput())
    gc.getInput().clearKeyPressedRecord()
  }
}