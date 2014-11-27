package io.github.suitougreentea.VariousMinos.state

import scala.beans.BeanProperty
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.Field
import io.github.suitougreentea.VariousMinos.Block
import io.github.suitougreentea.VariousMinos.Mino
import io.github.suitougreentea.VariousMinos.CommonRenderer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.Input

class StateDebug(@BeanProperty val ID: Int) extends BasicGameState with CommonRenderer { 
  var currentMinoId = 0
  var mino = new Mino(currentMinoId, 0)
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    g.translate(0, 80)
    drawMino(g)(mino)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    var i = gc.getInput()
    if(i.isKeyPressed(Input.KEY_RIGHT)) {
      currentMinoId += 1
      mino = new Mino(currentMinoId, 0, Array(new Block(1), new Block(2), new Block(64), new Block(1), new Block(1)))
    }
    if(i.isKeyPressed(Input.KEY_LEFT)) {
      currentMinoId -= 1
      mino = new Mino(currentMinoId, 0, Array(new Block(1), new Block(2), new Block(64), new Block(1), new Block(1)))
    }
  }
  
}