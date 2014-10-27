package io.github.suitougreentea.VariousMinos.game

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
import io.github.suitougreentea.VariousMinos.Buttons
import io.github.suitougreentea.VariousMinos.DefaultSettingBomb

class GameBomb(val wrapper: GameWrapper, defaultSetting: DefaultSettingBomb) extends Game with CommonRenderer {
  val _this = this
  val rule = defaultSetting.rule
  val handler = defaultSetting.handler
  var field = new Field(rule)
  
  if(defaultSetting.field != null) {
    for(iy <- 0 until defaultSetting.field.size; ix <- 0 until 10){
      field(ix, iy) = new Block(defaultSetting.field(iy)(ix))
    }
  }

  rule.randomizer.minoSet = HashSet(1, 3, 5, 7, 9)
  
  field.generateMino = () => {
    var id = rule.randomizer.next()
    var num = MinoList.numBlocks(id)
    var array = Array.fill(num)(new Block(rule.color.get(id)))
    //var array = Array.fill(num)(new Block(73))
    array(Math.random() * num toInt) = new Block(64)
    new Mino(id, rule.spawn.getRotation(id), array)
  }
  
  val bombSize = Array ((3, 0), (3, 1), (3, 2), (3, 3), (4, 4), (4, 4), (5, 5), (5, 5), (6, 6), (6, 6), (7, 7), (7, 7), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8))
  
  val phaseReady : Phase = new Phase {
    val id = -1
    val beforeTime = 10
    val afterTime = 30
    
    override def handleBeforeBefore(executer: PhaseExecuter){
      searchBigBomb()
    }
    
    override def procedureBefore(executer: PhaseExecuter){
      if(makingBigBombSet.size == 0) executer.moveToWorking()
      else super.procedureBefore(executer)
    }
    
    override def handleAfterBefore(executer: PhaseExecuter){
      makeBigBomb()
    }
    
    def procedureWorking(executer: PhaseExecuter){
      val c = wrapper.control
      if(c.pressed(Buttons.A)) executer.enterPhase(phaseMoving, true)
    }
  }
  
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
      if(field.checkHit()) handler.stuck(_this)
    }
    def procedureWorking(executer: PhaseExecuter) {
      val c = wrapper.control
      
      fallCounter += fallCounterDelta
      while(fallCounter >= 1) {
        field.moveMinoDown()
        fallCounter -= 1
      }
      
      if(field.currentMinoY == field.ghostY) {
        if(lockdownTimer == lockdownTimerMax || forceLockdownTimer == forceLockdownTimerMax) {
          field.hardDrop()
          executer.enterPhase(if(field.filledLines.length != lastLines) phaseCounting else phaseMakingBigBomb, true)
        }
        lockdownTimer += 1
        forceLockdownTimer += 1
      }
      
      if(c.pressed(Buttons.LEFT)){  
        if(field.moveMinoLeft()) lockdownTimer = 0
        moveDirection = -1
        firstMoveTimer = 0
        moveCounter = 0
      }
      if(c.pressed(Buttons.RIGHT)){
        if(field.moveMinoRight()) lockdownTimer = 0
        moveDirection = 1
        firstMoveTimer = 0
        moveCounter = 0
      }
	    if(c.down(Buttons.LEFT)) {
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
	    if(c.down(Buttons.RIGHT)) {
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
      
      if(c.down(Buttons.DOWN)){
        softDropCounter += softDropCounterDelta
        while(softDropCounter >= 1) {
          if(field.currentMinoY == field.ghostY && rule.downKeyLock){
            field.hardDrop()
            executer.enterPhase(if(field.filledLines.length != lastLines) phaseCounting else phaseMakingBigBomb, true)
          } else {
            field.moveMinoDown()
          }
          softDropCounter -= 1          
        }
      }
      if(c.pressed(Buttons.UP)) {
        if(rule.enableUpKey){
          if(rule.upKeyLock){
            field.hardDrop()
            executer.enterPhase(if(field.filledLines.length != lastLines) phaseCounting else phaseMakingBigBomb, true)
          } else {
            field.currentMinoY = field.ghostY
          }
        }
      }
      if(c.pressed(Buttons.A)){
        field.rotateMinoCCW()
        lockdownTimer = 0
      }
      if(c.pressed(Buttons.B)){
        field.rotateMinoCW()
        lockdownTimer = 0 
      }
      if(c.pressed(Buttons.C)){
			  field.hold()
			  fallCounter = 0
			  softDropCounter = 0
			  lockdownTimer = 0
      }
    }
  }

  val phaseCounting : Phase = new Phase {
    val id = 1
    val beforeTime = 10
    val afterTime = 10
    def procedureWorking(executer: PhaseExecuter) {
      if(existBombLine){
        chain += 1
        executer.enterPhase(phaseErasing, true)
      }
      else executer.enterPhase(phaseMakingBigBomb, true)
    }
  }
  
  var bombList: HashSet[(Int, Int, Boolean)] = HashSet.empty
  var bombTimer = 0
  var bombTimerMiddle = 8
  var bombTimerMax = 30
  
  val phaseErasing : Phase = new Phase {
    val id = 2
    val beforeTime = 0
    val afterTime = 0
    var bombListNew: HashSet[(Int, Int, Boolean)] = HashSet.empty
    override def handleAfterBefore(executer: PhaseExecuter) {
      bombList = HashSet.empty
      lastLines = field.filledLines.length
      for(iy <- field.filledLines; ix <- 0 until 10){
        field(ix, iy).id match {
          case 64 => bombList += Tuple3(ix, iy, false)
          case 65 if(!bombList.contains(Tuple3(ix, iy - 1, true))) => bombList += Tuple3(ix, iy - 1, true)
          case 67 => bombList += Tuple3(ix, iy, true)
          case _ =>
        }
      }
    }
    def procedureWorking(executer: PhaseExecuter) {
      if(bombTimer == bombTimerMiddle){
        for(e <- bombList) {
          var (width, height) = bombSize(lastLines + chain - 1 - 1)
          var (x, y, big) = e
          var yr, xr: Range = null
          if(big) {
            yr = (y - 4) to (y + 5)
            xr = (x - 4) to (x + 5)
          }else{
            yr = (y - height) to (y + height)
            xr = (x - width) to (x + width)
          }
          for(iy <- yr; ix <- xr) {
            var id = field(ix, iy).id
            if(id == 64 && !bombList.contains(Tuple3(ix, iy, false))){
              bombListNew += Tuple3(ix, iy, false)
            } else if(id == 65 && !bombList.contains(Tuple3(ix, iy - 1, true))) {
              bombListNew += Tuple3(ix, iy - 1, true)
            } else if(id == 66 && !bombList.contains(Tuple3(ix - 1, iy - 1, true))) {
              bombListNew += Tuple3(ix - 1, iy - 1, true)
            } else if(id == 67 && !bombList.contains(Tuple3(ix, iy, true))) {
              bombListNew += Tuple3(ix, iy, true)
            } else if(id == 68 && !bombList.contains(Tuple3(ix - 1, iy, true))) {
              bombListNew += Tuple3(ix - 1, iy, true)
            } else if((70 <= id && id <= 73) || (75 <= id && id <= 78)){
              field(ix, iy).id -= 1
            } else if(id == 79 || id == 80){
            } else {
              field(ix, iy) = new Block(0)
            }
          }
        }
      }
      if(bombTimer == bombTimerMax) {
        bombTimer = 0
        if(bombListNew.size == 0){
          field.makeFallingPieces()
          if(field.fallingPieceSet.size == 0){
            chain = 0
            executer.enterPhase(phaseMakingBigBomb, true)
          } else {
            executer.enterPhase(phaseFalling, true)
          }
        } else {
          bombList = bombListNew
          bombListNew = HashSet.empty
        }
      }
      bombTimer += 1
    }
  }
  
  var fallingPieceCounter = 0f
  var fallingPieceCounterDelta = 1f
  
  val phaseFalling : Phase = new Phase {
    val id = 3
    val beforeTime = 0
    val afterTime = 0
    def procedureWorking(executer: PhaseExecuter) {
      fallingPieceCounter += fallingPieceCounterDelta
      while (fallingPieceCounter >= 1){
        for(e <- field.fallingPieceSet){
          e.y -= 1
          if(field.checkHitFallingPiece(piece = e)) {
            e.y += 1
            field.setFallingPiece(e)
            field.fallingPieceSet -= e
          }
        }
        fallingPieceCounter -= 1
      }
      if(field.fallingPieceSet.size == 0){
        if(field.filledLines.length > 0 && existBombLine) executer.enterPhase(phaseCounting, true)
        else {
          chain = 0
          executer.enterPhase(phaseMakingBigBomb, true)
        }
      }
    }
  }
  var makingBigBombSet: HashSet[(Int, Int)] = HashSet.empty
  def searchBigBomb() {
      // 上が優先される
      for(iy <- field.height - 1 to 0 by -1; ix <- 0 until 10){
        if(field(ix, iy).id == 64){
          if(!makingBigBombSet.contains(Tuple2(ix - 1, iy)) && !makingBigBombSet.contains(Tuple2(ix, iy + 1)) && !makingBigBombSet.contains(Tuple2(ix - 1, iy + 1))){
            if(field(ix + 1, iy).id == 64 && field(ix, iy - 1).id == 64 && field(ix + 1, iy - 1).id == 64){
              makingBigBombSet += Tuple2(ix, iy)
            }
          }
        }
      }
  }
  def makeBigBomb() {
      for(e <- makingBigBombSet){
        var (x, y) = e
        field(x, y) = new Block(65)
        field(x + 1, y) = new Block(66)
        field(x, y - 1) = new Block(67)
        field(x + 1, y - 1) = new Block(68)
      }
      makingBigBombSet = HashSet.empty
  }
  val phaseMakingBigBomb : Phase = new Phase {
    val id = 4
    val beforeTime = 10
    val afterTime = 10
    override def handleBeforeBefore(executer: PhaseExecuter){
      var flag = false
      for(iy <- 0 until field.height; ix <- 0 until 10){
        if(field(ix, iy).id != 0) flag = true
      }
      if(flag){
        searchBigBomb()
      } else {
        handler.allClear(_this)
      }
    }
    override def procedureBefore(executer: PhaseExecuter){
      if(makingBigBombSet.size == 0) executer.enterPhase(phaseMoving, false)
      else super.procedureBefore(executer)
    }
    def procedureWorking(executer: PhaseExecuter) {
      makeBigBomb()
      executer.enterPhase(phaseMoving, true)
    }
  }
  
  def existBombLine: Boolean = {
    for(iy <- field.filledLines; ix <- 0 until 10){
      field(ix, iy).id match {
        case 64 | 65 | 66 | 67 | 68 => return true
        case _ =>
      }
    }
    return false
  }
 
  var executer: PhaseExecuter = new PhaseExecuter(phaseReady)
  field.init()
  
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
  private var chain = 0
  
  def update() {
        executer.exec()
  }
  def render(g: Graphics) {
    //Resource.design.draw()
    g.setBackground(Color.darkGray)
    g.clear()
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
        var x = e._1 toFloat
        var y = e._2 toFloat
        var big = e._3
        var width, height = 0f
        if(big){
          x += 0.5f
          y += 0.5f
          width = 4.5f
          height = 4.5f
        } else {          
          width = bombSize(lastLines + chain - 1 - 1)._1
          height = bombSize(lastLines + chain - 1 - 1)._2
        }
        var multiplier = 0f
        var transparency = 1f
        if(bombTimer < bombTimerMiddle){
          var t = bombTimer / (bombTimerMiddle toFloat)
          multiplier = -(Math.pow((t - 1), 2) - 1) toFloat
        } else {
          transparency = 1f - (bombTimer - bombTimerMiddle) / (bombTimerMax - bombTimerMiddle toFloat)
          multiplier = 1
        }
        width *= multiplier
        height *= multiplier
        var topLeftX = (x - width) * 16
        var topLeftY = -(y + height + 1) * 16
        var renderWidth = (width * 2 + 1) * 16
        var renderHeight = (height * 2 + 1) * 16
        Resource.bomb.draw(topLeftX, topLeftY, renderWidth, renderHeight, new Color(1f, 1f, 1f, transparency))
      }
    }
    for(e <- field.fallingPieceSet) {
      drawFallingPiece(g)(e)
    }
    g.popTransform()
    
    g.pushTransform()
    g.translate(0, 80)
    for(i <- 0 until rule.numNext) {
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
    Resource.boldfont.drawString("PhaseID: %d\nPosition: %s\nTimer: %d\nFall: %f\nSoft: %f\nLock: %d\nForce: %d\nDirection: %d\nFirstMove: %d\nMove: %f\nLines: %d\nBomb: %d\nFallPiece: %f\nChain: %d".
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
            bombTimer,
            fallingPieceCounter,
            chain),
            472, 160)
  }
    
  override def graphicId(block: Block): Int = {
    var id = block.id
    if(0 <= id && id < 64) id
    else if(id == 64) 128
    else if(id == 65) 129
    else if(id == 66) 130
    else if(id == 67) 193
    else if(id == 68) 194
    else if(69 <= id && id <= 80) (id - 69) + 133
    else id
  }
}