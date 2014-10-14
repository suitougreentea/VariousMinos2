package io.github.suitougreentea.VariousMinos.state

import scala.beans.BeanProperty
import io.github.suitougreentea.VariousMinos.Phase
import io.github.suitougreentea.VariousMinos.PhaseExecuter
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import io.github.suitougreentea.VariousMinos.Field
import io.github.suitougreentea.VariousMinos.CommonRenderer
import io.github.suitougreentea.VariousMinos.Resource
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.Color

class StateBomb(@BeanProperty val ID: Int) extends BasicGameState with CommonRenderer {
  
  var field = new Field()
  
  val phaseMoving = new Phase {
    val id = 0
    val beforeTime = 30
    val afterTime = 30
    def procedureWorking(executer: PhaseExecuter) {
      var i = executer.game.getContainer().getInput()
      if(i.isKeyPressed(Input.KEY_LEFT)) field.moveMinoLeft()
      if(i.isKeyPressed(Input.KEY_RIGHT)) field.moveMinoRight()
      if(i.isKeyPressed(Input.KEY_DOWN)) field.moveMinoDown()
      if(i.isKeyPressed(Input.KEY_UP)) field.moveMinoUp()
      if(i.isKeyPressed(Input.KEY_Z)) field.rotateMinoCCW()
      if(i.isKeyPressed(Input.KEY_X)) field.rotateMinoCW()
      if(i.isKeyPressed(Input.KEY_A)) executer.enterPhase(this)
    }
  }

  val phaseCounting = new Phase {
    val id = 0
    val beforeTime = 30
    val afterTime = 30
    def procedureWorking(executer: PhaseExecuter) {
    }
  }
  
  var executer: PhaseExecuter = _
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    executer = new PhaseExecuter(sbg)
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    executer.enterPhase(phaseMoving) 
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    Resource.design.draw()
    
    Resource.frame.draw(152, 144)
    g.pushTransform()
    g.translate(168, 512)
    drawField(g)(field)
    g.popTransform()
    
    Resource.frame.draw(456, 144)
    
    g.setColor(new Color(1f, 1f, 1f))
    g.drawString("PhaseID: %d\nPosition: %s\nTimer: %d".format(executer.currentPhase.id, executer.currentPosition.toString(), executer.timer), 472, 160)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    var i = gc.getInput()

    executer.exec()
    
    i.clearKeyPressedRecord()
  }
}