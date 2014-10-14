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
import io.github.suitougreentea.VariousMinos.Position
import io.github.suitougreentea.VariousMinos.MinoList
import io.github.suitougreentea.VariousMinos.Block
import io.github.suitougreentea.VariousMinos.Mino

class StateBomb(@BeanProperty val ID: Int) extends BasicGameState with CommonRenderer {
  
  var field = new Field()
  field.generateMino = () => {
    var id = Math.random() * 29 toInt
    var num = MinoList.numBlocks(id)
    var array = Array.fill(num)(new Block(1))
    array(Math.random() * num toInt) = new Block(64)
    new Mino(id, 0, array)
  }
  
  val phaseMoving = new Phase {
    val id = 0
    val beforeTime = 10
    val afterTime = 10
    override def handleAfterBefore(executer: PhaseExecuter) {
      fallCounter = 0
			softDropCounter = 0
			lockdownTimer = 0
			forceLockdownTimer = 0
      field.newMino()
    }
    def procedureWorking(executer: PhaseExecuter) {
      var i = executer.game.getContainer().getInput()
      if(i.isKeyPressed(Input.KEY_LEFT)){  
        if(field.moveMinoLeft()) lockdownTimer = 0
        moveDirection = -1
        firstMoveTimer = 0
        moveCounter = 0
      }
      if(i.isKeyPressed(Input.KEY_RIGHT)){
        if(field.moveMinoRight()) lockdownTimer = 0
        moveDirection = 1
        firstMoveTimer = 0
        moveCounter = 0
      }
	    if(i.isKeyDown(Input.KEY_LEFT)) {
    		if(moveDirection == -1) {
  		    if(firstMoveTimer == firstMoveTimerMax){
    		  	moveCounter += moveCounterDelta
    			  while(moveCounter >= 1) {
    			    if(field.moveMinoLeft()) lockdownTimer = 0
    			    moveCounter -= 1
    			  }
  		    } else {
    			  firstMoveTimer += 1
  		    }
    		}
	    }
	    if(i.isKeyDown(Input.KEY_RIGHT)) {
    		if(moveDirection == 1) {
  		    if(firstMoveTimer == firstMoveTimerMax){
      			moveCounter += moveCounterDelta
      			while(moveCounter >= 1) {
      			    if(field.moveMinoRight()) lockdownTimer = 0
      			    moveCounter -= 1
      			}
  		    } else {
    			  firstMoveTimer += 1
  		    }
    		}
	    }
      
      if(i.isKeyDown(Input.KEY_DOWN)){
        softDropCounter += softDropCounterDelta
        while(softDropCounter >= 1) {
          field.moveMinoDown()
          softDropCounter -= 1          
        }
      }
      if(i.isKeyPressed(Input.KEY_UP)) {
        field.hardDrop()
        executer.enterPhase(if(/*AdditionalLine*/false) phaseCounting else this)
      }
      if(i.isKeyPressed(Input.KEY_Z)){
        field.rotateMinoCCW()
        lockdownTimer = 0
      }
      if(i.isKeyPressed(Input.KEY_X)){
        field.rotateMinoCW()
        lockdownTimer = 0 
      }
      if(i.isKeyPressed(Input.KEY_C)){
			  field.hold()
			  fallCounter = 0
			  softDropCounter = 0
			  lockdownTimer = 0
      }
      if(i.isKeyPressed(Input.KEY_A)) executer.enterPhase(this)
      
      fallCounter += fallCounterDelta
      while(fallCounter >= 1) {
        field.moveMinoDown()
        fallCounter -= 1
      }
      
      if(field.currentMinoY == field.ghostY) {
        if(lockdownTimer == lockdownTimerMax || forceLockdownTimer == forceLockdownTimerMax) {
          field.hardDrop()
          executer.enterPhase(if(/*AdditionalLine*/false) phaseCounting else this)
        }
        lockdownTimer += 1
        forceLockdownTimer += 1
      }
    }
  }

  val phaseCounting = new Phase {
    val id = 0
    val beforeTime = 10
    val afterTime = 10
    def procedureWorking(executer: PhaseExecuter) {
    }
  }
  
  var executer: PhaseExecuter = _
  
  private var fallCounter = 0f
  private var fallCounterDelta = 1/60f
  private var softDropCounter = 0f
  private var softDropCounterDelta = 1f
  private var lockdownTimer = 0
  private var lockdownTimerMax = 60
  private var forceLockdownTimer = 0
  private var forceLockdownTimerMax = 180
  
  private var moveDirection = 0
  private var firstMoveTimer = 0
  private var firstMoveTimerMax = 5
  private var moveCounter = 0f
  private var moveCounterDelta = 0.5f
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    executer = new PhaseExecuter(sbg)
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    field.init()
    executer.enterPhase(phaseMoving) 
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    //Resource.design.draw()
    
    Resource.frame.draw(152, 144)
    g.pushTransform()
    g.translate(168, 512)
    drawField(g)(field)
    if(executer.currentPhase.id == 0 && executer.currentPosition == Position.WORKING) {
      drawFieldMino(g)(field)
      drawFieldMinoGhost(g)(field)
    }
    g.popTransform()
    
    g.pushTransform()
    g.translate(0, 80)
    for(i <- 0 until 7) {
      drawMino(g)(field.nextMino(i))
      g.translate(96, 0)
    }
    g.popTransform()
    
    g.pushTransform()
    g.translate(720, 80)
    if(field.holdMino != null) drawMino(g)(field.holdMino)
    g.popTransform()
    
    Resource.frame.draw(456, 144)
    
    g.setColor(new Color(1f, 1f, 1f))
    g.drawString("PhaseID: %d\nPosition: %s\nTimer: %d\nFall: %f\nSoft: %f\nLock: %d\nForce: %d\nDirection: %d\nFirstMove: %d\nMove: %f".
        format(executer.currentPhase.id,
            executer.currentPosition.toString(),
            executer.timer,
            fallCounter,
            softDropCounter,
            lockdownTimer,
            forceLockdownTimer,
            moveDirection,
            firstMoveTimer,
            moveCounter),
            472, 160)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    var i = gc.getInput()

    executer.exec()
    
    i.clearKeyPressedRecord()
  }
}