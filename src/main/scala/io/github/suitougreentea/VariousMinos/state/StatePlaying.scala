package io.github.suitougreentea.VariousMinos.state

import scala.beans.BeanProperty

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame

import io.github.suitougreentea.VariousMinos.Control
import io.github.suitougreentea.VariousMinos.game.GameWrapper

class StatePlaying(@BeanProperty val ID: Int) extends BasicGameState {
  var wrapper1p: GameWrapper = _
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    wrapper1p = new GameWrapper(0, new Control(gc.getInput()))
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    wrapper1p.render(g)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    wrapper1p.update()
    
    gc.getInput().clearKeyPressedRecord()
  }
}