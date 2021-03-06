package io.github.suitougreentea.VariousMinos

import scala.beans.BeanProperty
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import io.github.suitougreentea.VariousMinos.state._

class GameVariousMinos(val name: String) extends StateBasedGame(name) {
  this.addState(new StateDebug(-1))
  this.addState(new StateLoading(0))
  this.addState(new StatePlaying(1))
  this.enterState(1)

  def initStatesList(gc: GameContainer) = {
    this.getState(-1).init(gc, this)   
    this.getState(0).init(gc, this)   
    this.getState(1).init(gc, this)   
  }
}

class StateLoading(@BeanProperty val ID: Int) extends BasicGameState {
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    Resource.load()
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
  }
}