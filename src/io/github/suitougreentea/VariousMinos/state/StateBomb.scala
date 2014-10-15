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
import scala.collection.mutable.HashSet

class StateBomb(@BeanProperty val ID: Int) extends BasicGameState with CommonRenderer {
  
  var field = new Field()
  field.generateMino = () => {
    var id = Math.random() * 29 toInt
    var num = MinoList.numBlocks(id)
    var array = Array.fill(num)(new Block(1))
    array(Math.random() * num toInt) = new Block(64)
    new Mino(id, 0, array)
  }
  
  val bombSize = Array ((3, 0), (3, 1), (3, 2), (3, 3), (4, 4), (4, 4), (5, 5), (5, 5), (6, 6), (6, 6), (7, 7), (7, 7), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8))
  
  val phaseMoving : Phase = new Phase {
    val id = 0
    val beforeTime = 0
    val afterTime = 0
    override def handleAfterBefore(executer: PhaseExecuter) {
      fallCounter = 0
			softDropCounter = 0
			lockdownTimer = 0
			forceLockdownTimer = 0
			lastLines = field.filledLines.length
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
        executer.enterPhase(if(field.filledLines.length != lastLines) phaseCounting else this, true)
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
          executer.enterPhase(if(field.filledLines.length != lastLines) phaseCounting else this, true)
        }
        lockdownTimer += 1
        forceLockdownTimer += 1
      }
    }
  }

  val phaseCounting : Phase = new Phase {
    val id = 1
    val beforeTime = 10
    val afterTime = 10
    def procedureWorking(executer: PhaseExecuter) {
      executer.enterPhase(phaseErasing, true)
    }
  }
  
  var bombList: HashSet[(Int, Int, Boolean)] = HashSet.empty
  var bombTimer = 0
  var bombTimerMiddle = 30
  var bombTimerMax = 40
  
  val phaseErasing : Phase = new Phase {
    val id = 2
    val beforeTime = 0
    val afterTime = 0
    var bombListNew: HashSet[(Int, Int, Boolean)] = HashSet.empty
    override def handleAfterBefore(executer: PhaseExecuter) {
      bombList = HashSet.empty
      lastLines = field.filledLines.length
      for(iy <- field.filledLines; ix <- 0 until 10){
        if(field(ix, iy).id == 64) bombList += Tuple3(ix, iy, false)
      }
    }
    def procedureWorking(executer: PhaseExecuter) {
      if(bombTimer == bombTimerMiddle){
        for(e <- bombList) {
          var (width, height) = bombSize(lastLines - 1)
          var (x, y, big) = e
          for(iy <- (y - height) to (y + height); ix <- (x - width) to (x + width)) {
            if(field(ix, iy).id == 64 && !bombList.contains(Tuple3(ix, iy, false)) && !bombList.contains(Tuple3(ix, iy, true))){
              bombListNew += Tuple3(ix, iy, false)
            } else {
              field(ix, iy) = new Block(0)
            }
          }
        }
      }
      if(bombTimer == bombTimerMax) {
        bombTimer = 0
        if(bombListNew.size == 0){
          executer.enterPhase(phaseFalling, true)
        } else {
          bombList = bombListNew
          bombListNew = HashSet.empty
        }
      }
      bombTimer += 1
    }
  }
  val phaseFalling : Phase = new Phase {
    val id = 3
    val beforeTime = 0
    val afterTime = 0
    def procedureWorking(executer: PhaseExecuter) {
      executer.enterPhase(phaseMakingBigBomb, true)
    }
  }
  val phaseMakingBigBomb : Phase = new Phase {
    val id = 4
    val beforeTime = 0
    val afterTime = 0
    def procedureWorking(executer: PhaseExecuter) {
      executer.enterPhase(phaseMoving, true)
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
  
  private var lastLines = 0
  
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
    if(executer.currentPhase.id == 2) {
      for(e <- bombList){
        var width = bombSize(lastLines - 1)._1 toFloat
        var height = bombSize(lastLines - 1)._2 toFloat
        var multiplier = 0f
        if(bombTimer < bombTimerMiddle){
          var x = bombTimer / (bombTimerMiddle toFloat)
          multiplier = -(Math.pow((x - 1), 2) - 1) toFloat
        } else if(bombTimer < bombTimerMax){
          var x = (bombTimer - bombTimerMiddle) / (bombTimerMax - bombTimerMiddle toFloat)
          multiplier = -(Math.pow(x, 2) - 1) toFloat
        } else multiplier = 0
        width *= multiplier
        height *= multiplier
        var (x, y, big) = e
        var topLeftX = (x - width) * 16
        var topLeftY = -(y + height + 1) * 16
        var renderWidth = (width * 2 + 1) * 16
        var renderHeight = (height * 2 + 1) * 16
        Resource.bomb.draw(topLeftX, topLeftY, renderWidth, renderHeight)
      }
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
    g.drawString("PhaseID: %d\nPosition: %s\nTimer: %d\nFall: %f\nSoft: %f\nLock: %d\nForce: %d\nDirection: %d\nFirstMove: %d\nMove: %f\nLines: %d\nBomb: %d".
        format(executer.currentPhase.id,
            executer.currentPosition.toString(),
            executer.timer,
            fallCounter,
            softDropCounter,
            lockdownTimer,
            forceLockdownTimer,
            moveDirection,
            firstMoveTimer,
            moveCounter,
            lastLines,
            bombTimer),
            472, 160)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    var i = gc.getInput()

    executer.exec()
    
    i.clearKeyPressedRecord()
  }
}