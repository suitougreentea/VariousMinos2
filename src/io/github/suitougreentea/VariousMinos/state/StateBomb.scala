package io.github.suitougreentea.VariousMinos.state

import scala.beans.BeanProperty
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import io.github.suitougreentea.VariousMinos.Field
import io.github.suitougreentea.VariousMinos.CommonRenderer
import io.github.suitougreentea.VariousMinos.Resource
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input

class StateBomb(@BeanProperty val ID: Int) extends BasicGameState with CommonRenderer {
  
  var field = new Field()
  
  object Phase extends Enumeration {
    /*
     * WAITFORSPAWN<---------
     *      V          |    |
     * CONTROLLING------>MAKINGLARGEBOMB
     *      V          |    ^
     * -->WAITFORCOUNT |    |
     * |    V          |    |
     * |  COUNTING-----------
     * |      V             |
     * |  WAITFORERASE      |
     * |      V             |
     * |  ERASING           |
     * |      V             |
     * |  WAITFORFALL       |
     * |      V             |
     * |  FALLING           |
     * |      V             |
     * ---AFTERFALLING-------
     */
    val WAITFORSPAWN, CONTROLLING, WAITFORCOUNT, COUNTING, WAITFORERASE, ERASING, WAITFORFALL, FALLING, AFTERFALLING, MAKINGLARGEBOMB = Value
  }
  var phase = Phase.CONTROLLING
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    Resource.frame.draw(152, 144)
    g.translate(168, 512)
    drawField(g)(field)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    var i = gc.getInput()
    if(i.isKeyPressed(Input.KEY_LEFT)) field.moveMinoLeft()
    if(i.isKeyPressed(Input.KEY_RIGHT)) field.moveMinoRight()
    if(i.isKeyPressed(Input.KEY_DOWN)) field.moveMinoDown()
    if(i.isKeyPressed(Input.KEY_UP)) field.moveMinoUp()
    if(i.isKeyPressed(Input.KEY_Z)) field.rotateMinoCCW()
    if(i.isKeyPressed(Input.KEY_X)) field.rotateMinoCW()
    
    /* TODO: this will be replaced by Phase / PhaseExecuter */
    
    if(phase == Phase.WAITFORSPAWN) {
      if(/* Timer */false) phase = Phase.CONTROLLING
    }
    if(phase == Phase.CONTROLLING) {
      if(/* AdditionalLines */false) phase = Phase.WAITFORCOUNT
      else {
        if(/* LargeBomb */false) phase = Phase.MAKINGLARGEBOMB
        else phase = Phase.WAITFORSPAWN
      }
    }
    if(phase == Phase.WAITFORCOUNT) {
      if(/* Timer */false) phase = Phase.COUNTING
    }
    if(phase == Phase.COUNTING) {
      if(/* Timer */false) {
        if(/* Explodable */false) phase = Phase.WAITFORERASE
        else {
          if(/* LargeBomb */false) phase = Phase.MAKINGLARGEBOMB
          else phase = Phase.WAITFORSPAWN
        }
      }
    }
    if(phase == Phase.WAITFORERASE) {
      if(/* Timer */false) phase = Phase.ERASING
    }
    if(phase == Phase.ERASING) {
      if(/* Done */false) phase = Phase.WAITFORFALL
    }
  }
}