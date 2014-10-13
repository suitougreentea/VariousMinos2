package io.github.suitougreentea.VariousMinos.state

import scala.beans.BeanProperty
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.Field
import io.github.suitougreentea.VariousMinos.Block
import io.github.suitougreentea.VariousMinos.CommonRenderer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.Input

class StateTetra(@BeanProperty val ID: Int) extends BasicGameState with CommonRenderer {
  var f = new Field()
  f(1,0) = new Block(1)
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    //Resource.design.draw()
    Resource.frame.draw(152, 144)
    g.translate(168, 512)
    drawField(g)(f)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    var i = gc.getInput()
    if(i.isKeyPressed(Input.KEY_LEFT)) f.moveMinoLeft()
    if(i.isKeyPressed(Input.KEY_RIGHT)) f.moveMinoRight()
    if(i.isKeyPressed(Input.KEY_DOWN)) f.moveMinoDown()
    if(i.isKeyPressed(Input.KEY_UP)) f.moveMinoUp()
    if(i.isKeyPressed(Input.KEY_Z)) f.rotateMinoCCW()
    if(i.isKeyPressed(Input.KEY_X)) f.rotateMinoCW()
  }
}