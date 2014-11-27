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
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombInfinite
import io.github.suitougreentea.VariousMinos.rule.Rule
import io.github.suitougreentea.VariousMinos.CommonRendererBomb
import io.github.suitougreentea.util.TextAlign

class GameBomb(val wrapper: GameWrapper, val handler: HandlerBomb, val rule: Rule) extends Game with CommonRendererBomb {
  val _this = this
  var field = new Field(rule)
  
  val bombSize = Array ((3, 0), (3, 1), (3, 2), (3, 3), (4, 4), (4, 4), (5, 5), (5, 5), (6, 6), (6, 6), (7, 7), (7, 7), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8), (8, 8))
  
  val phaseReady : Phase = new Phase {
    val id = -1
    var beforeTime = 10
    var afterTime = 30
    
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
      if(c.pressed(Buttons.A)) {
        nextMinoDisplayCursor = 0
        executer.enterPhase(phaseMoving, true)
      }
      if(nextMinoDisplayType == 1) {
        if(c.pressed(Buttons.UP) && nextMinoDisplayCursor > 0) nextMinoDisplayCursor -= 1
        if(c.pressed(Buttons.DOWN) && nextMinoDisplayCursor + 6 < field.generator.size) nextMinoDisplayCursor += 1 
      }
    }
    
    override def procedureAfter(executer: PhaseExecuter) {
      super.procedureAfter(executer)
      val c = wrapper.control
      if(c.down(Buttons.LEFT)) {
        moveDirection = -1
    	  if(firstMoveTimer != firstMoveTimerMax) firstMoveTimer += 1	
	    }
	    if(c.down(Buttons.RIGHT)) {
	      moveDirection = 1
    		if(firstMoveTimer != firstMoveTimerMax) firstMoveTimer += 1
	    }
    }
  }
  
  val phaseMoving : Phase = new Phase {
    val id = 0
    var beforeTime = 10
    var afterTime = 0
    override def handleAfterBefore(executer: PhaseExecuter) {
      val c = wrapper.control
      handler.beforeNewMino(_this)
      fallCounter = 0
			softDropCounter = 0
			lockdownTimer = 0
			forceLockdownTimer = 0
			lastLines = field.filledLines.length
      if(field.generator.size == 0 && !field.generator.infinite){
        handler.noNewMino(_this)
      } else {
        field.newMino()
        if(nextAllBombFlag) {
          field.currentMino = new Mino(field.currentMino.minoId, field.rule.spawn.getRotation(field.currentMino.minoId), new Block(64))
        }
        handler.newMino(_this)
      }
      
      if(c.down(Buttons.C) && !c.pressed(Buttons.C) && rule.enableInitialHold){
        if(field.hold()) {
          if(nextAllBombFlag) {
            field.currentMino = new Mino(field.currentMino.minoId, field.rule.spawn.getRotation(field.currentMino.minoId), new Block(64))
          }
        }
      }
      if(c.down(Buttons.LEFT) && !c.pressed(Buttons.LEFT) && rule.enableInitialMove){
        field.moveMinoLeft()
      }
      if(c.down(Buttons.RIGHT) && !c.pressed(Buttons.RIGHT) && rule.enableInitialMove){
        field.moveMinoRight()
      }
      if(c.down(Buttons.A) && !c.pressed(Buttons.A) && rule.enableInitialRotate){
        field.rotateMinoCCW()
      }
      if(c.down(Buttons.B) && !c.pressed(Buttons.B) && rule.enableInitialRotate){
        field.rotateMinoCW()
      }
      
      if(field.checkHit()) handler.stuck(_this)
      
      fallCounter += fallCounterDelta
      while(fallCounter >= 1) {
        field.moveMinoDown()
        fallCounter -= 1
      }
    }
    override def procedureBefore(executer: PhaseExecuter) {
      super.procedureBefore(executer)
      val c = wrapper.control
      
      if(c.down(Buttons.LEFT)) {
        moveDirection = -1
    	  if(firstMoveTimer != firstMoveTimerMax) firstMoveTimer += 1	
	    }
	    if(c.down(Buttons.RIGHT)) {
	      moveDirection = 1
    		if(firstMoveTimer != firstMoveTimerMax) firstMoveTimer += 1
	    }
    }
    def procedureWorking(executer: PhaseExecuter) {
      val c = wrapper.control
      
      if(c.pressed(Buttons.A)){
        if(field.rotateMinoCCW() && rule.resetByRotating) lockdownTimer = 0
      }
      if(c.pressed(Buttons.B)){
        if(field.rotateMinoCW() && rule.resetByRotating) lockdownTimer = 0 
      }
      if(c.pressed(Buttons.LEFT)){  
        if(field.moveMinoLeft() && rule.resetByMoving) lockdownTimer = 0
        moveDirection = -1
        firstMoveTimer = 0
        moveCounter = 0
      }
      if(c.pressed(Buttons.RIGHT)){
        if(field.moveMinoRight() && rule.resetByMoving) lockdownTimer = 0
        moveDirection = 1
        firstMoveTimer = 0
        moveCounter = 0
      }
	    if(c.down(Buttons.LEFT)) {
    		if(moveDirection == -1) {
  		    if(firstMoveTimer == firstMoveTimerMax){
    		  	moveCounter += moveCounterDelta
    			  while(moveCounter >= 1) {
    			    if(field.moveMinoLeft() && rule.resetByMoving) lockdownTimer = 0
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
      			    if(field.moveMinoRight() && rule.resetByMoving) lockdownTimer = 0
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
            executer.enterPhase(phaseCounting, true)
          } else {
            if(field.moveMinoDown() && rule.resetByFalling) lockdownTimer = 0
          }
          softDropCounter -= 1          
        }
      }
      if(c.pressed(Buttons.UP)) {
        if(rule.enableUpKey){
          if(rule.upKeyLock){
            field.hardDrop()
            executer.enterPhase(phaseCounting, true)
          } else {
            field.currentMinoY = field.ghostY
          }
        }
      }

      if(c.pressed(Buttons.C)){
			  if(field.hold()) {
          if(nextAllBombFlag) {
            field.currentMino = new Mino(field.currentMino.minoId, field.rule.spawn.getRotation(field.currentMino.minoId), new Block(64))
          }   
        }
			  fallCounter = 0
			  softDropCounter = 0
			  lockdownTimer = 0
      }
      
      fallCounter += fallCounterDelta
      while(fallCounter >= 1) {
        if(field.moveMinoDown() && rule.resetByFalling) lockdownTimer = 0
        fallCounter -= 1
      }
      if(field.currentMinoY == field.ghostY) {
        if(lockdownTimer == lockdownTimerMax || forceLockdownTimer == forceLockdownTimerMax) {
          field.hardDrop()
          executer.enterPhase(phaseCounting, true)
        }
        lockdownTimer += 1
        forceLockdownTimer += 1
      }
    }
    override def handleBeforeAfter(executer: PhaseExecuter) {
      nextAllBombFlag = false
    }
  }

  val phaseCounting : Phase = new Phase {
    val id = 1
    var beforeTime = 0
    var afterTime = 0
    override def procedureBefore(executer: PhaseExecuter) {
      if(field.filledLines.length == lastLines) executer.enterPhase(phaseFalling, false)
      else super.procedureBefore(executer)
    }
    def procedureWorking(executer: PhaseExecuter) {
      if(existBombLine){
        chain += 1
        handler.fillLine(_this, field.filledLines.length + chain - 1, chain, false)
        executer.enterPhase(phaseErasing, true)
      } else {
        handler.fillLine(_this, field.filledLines.length + chain, chain, true)
        executer.enterPhase(phaseFalling, true) 
      }
    }
  }
  
  var bombList: HashSet[(Int, Int, Boolean)] = HashSet.empty
  var bombTimer = 0
  var bombTimerMiddle = 8
  var bombTimerMax = 30
  
  val phaseErasing : Phase = new Phase {
    val id = 2
    var beforeTime = 20
    var afterTime = 0
    var bombListNew: HashSet[(Int, Int, Boolean)] = HashSet.empty
    var erasedBlocksList = IndexedSeq.fill(field.height)(Array.fill(10)(false))
    var erasedBlocks = 0
    override def handleBeforeBefore(executer: PhaseExecuter) {
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
            if(0 <= ix && ix < 10 && 0 <= iy && iy < field.height) {
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
              } else if(((70 <= id && id <= 73) || (76 <= id && id <= 79)) && !erasedBlocksList(iy)(ix)){
                field(ix, iy).id -= 1
                erasedBlocks += 1
                erasedBlocksList(iy)(ix) = true
              } else if(id == 74 || id == 80 || id == 86){
              } else {
                if(field(ix, iy).id > 0 && !erasedBlocksList(iy)(ix)){
                  field(ix, iy) = new Block(0)
                  erasedBlocks += 1
                  erasedBlocksList(iy)(ix) = true
                }
              }
            }
          }
        }
      }
      if(bombTimer == bombTimerMax) {
        erasedBlocksList = IndexedSeq.fill(field.height)(Array.fill(10)(false))
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
    override def handleBeforeAfter(executer: PhaseExecuter){
      handler.afterBomb(_this, erasedBlocks)
      erasedBlocks = 0
    }
  }
  
  var fallingPieceCounter = 0f
  var fallingPieceCounterDelta = 1f
  
  val phaseFalling : Phase = new Phase {
    val id = 3
    var beforeTime = 0
    var afterTime = 0
    override def handleBeforeBefore(executer: PhaseExecuter) {
      field.makeFallingPieces()
    }
    override def procedureBefore(executer: PhaseExecuter){
      if(field.fallingPieceSet.size == 0) {
        executer.enterPhase(phaseMakingBigBomb, false)
      }
      else super.procedureBefore(executer)
    }
    def procedureWorking(executer: PhaseExecuter) {
      fallingPieceCounter += fallingPieceCounterDelta
      while (fallingPieceCounter >= 1){
        for(e <- field.fallingPieceSet){
          if(e.containsPersistentBlock){
            // TODO: I really don't know why this bug occurs; this statement is to prevent black blocks from falling.
            // TODO: If this bug is fixed, remove this statement
            field.setFallingPiece(e)
            field.fallingPieceSet -= e
          } else {
            e.y -= 1
            if(field.checkHitFallingPiece(piece = e)) {
              e.y += 1
              field.setFallingPiece(e)
              field.fallingPieceSet -= e
            }
          }
        }
        for(e <- field.fallingPieceSetIndependent){
          e.y -= 1
          if(field.checkHitFallingPiece(piece = e)) {
            e.y += 1
            field.setFallingPiece(e)
            field.fallingPieceSetIndependent -= e
          }
        }
        fallingPieceCounter -= 1
      }
      if(field.fallingPieceSet.size == 0 && field.fallingPieceSetIndependent.size == 0){
        lastLines = 0
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
          if(!alongToBigBombSet(ix, iy)){
            if(field(ix + 1, iy).id == 64 && !alongToBigBombSet(ix + 1, iy) &&
                field(ix, iy - 1).id == 64 && !alongToBigBombSet(ix, iy - 1) &&
                field(ix + 1, iy - 1).id == 64 && !alongToBigBombSet(ix + 1, iy - 1)){
              makingBigBombSet += Tuple2(ix, iy)
            }
          }
        }
      }
  }
  def alongToBigBombSet(ix: Int, iy: Int) = makingBigBombSet.contains(Tuple2(ix - 1, iy)) || makingBigBombSet.contains(Tuple2(ix - 1, iy)) || makingBigBombSet.contains(Tuple2(ix, iy + 1)) || makingBigBombSet.contains(Tuple2(ix - 1, iy + 1))
  def makeBigBomb(): Int = {
    var i = 0
    for(e <- makingBigBombSet){
      var (x, y) = e
      field(x, y) = new Block(65)
      field(x + 1, y) = new Block(66)
      field(x, y - 1) = new Block(67)
      field(x + 1, y - 1) = new Block(68)
      i += 1
    }
    makingBigBombSet = HashSet.empty
    i
  }
  val phaseMakingBigBomb : Phase = new Phase {
    val id = 4
    var beforeTime = 10
    var afterTime = 10
    override def handleBeforeBefore(executer: PhaseExecuter){
      chain = 0
      var flag = false
      for(iy <- 0 until field.height; ix <- 0 until 10){
        if(field(ix, iy).id != 0 && !field(ix, iy).unerasable) flag = true
      }
      if(flag){
        searchBigBomb()
      } else {
        handler.allClear(_this)
      }
    }
    override def procedureBefore(executer: PhaseExecuter){
      if(allEraseFlag){
        allEraseFlag = false
        executer.enterPhase(phaseErasingField, false)
      }
      else if(makingBigBombSet.size == 0) executer.enterPhase(phaseMoving, false)
      else super.procedureBefore(executer)
    }
    def procedureWorking(executer: PhaseExecuter) {
      handler.makeBigBomb(_this, makeBigBomb())
      executer.enterPhase(phaseMoving, true)
    }
  }
  
  val phaseErasingField: Phase = new Phase {
    val id = 5
    var beforeTime = 0
    var afterTime = 180
    def procedureWorking(executer: PhaseExecuter) {
      executer.enterPhase(phaseMoving, true)
    }
    override def procedureAfter(executer: PhaseExecuter) {
      if(executer.timer % 5 == 0) {
        for(i <- 0 until 10){
          field(i, executer.timer / 5) = new Block(0)
        }
      }
      super.procedureAfter(executer) 
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
 
  
  private var fallCounter = 0f
  var fallCounterDelta = 0f
  private var softDropCounter = 0f
  var softDropCounterDelta = 1f
  private var lockdownTimer = 0
  var lockdownTimerMax = 60
  private var forceLockdownTimer = 0
  var forceLockdownTimerMax = 180
  
  private var moveDirection = 0
  private var firstMoveTimer = 0
  var firstMoveTimerMax = 5
  private var moveCounter = 0f
  var moveCounterDelta = 0.5f
  
  private var lastLines = 0
  private var chain = 0
  
  var allEraseFlag = false
  var nextAllBombFlag = false
  
  // 0: Normal, 1: Puzzle
  var nextMinoDisplayType = 0
  // use if type=1
  var nextMinoDisplayCursor = 0
  
  handler.init(this)
  field.init()
  var executer: PhaseExecuter = new PhaseExecuter(phaseReady)

  def update() {
        executer.exec()
  }
  def render(g: Graphics) {
    g.setBackground(Color.darkGray)
    g.clear()
    //Resource.design.draw()
    g.pushTransform()
    g.translate(168, 512)
    drawField(g)(field)
    if(executer.currentPhase.id == 0 && executer.currentPosition == Position.WORKING) {
      drawFieldMinoGhost(g)(field)
      drawFieldMino(g)(field, 0.2f - lockdownTimer / (lockdownTimerMax toFloat) * 0.15f)
    }
    if(executer.currentPhase.id == 2 && executer.currentPosition == Position.WORKING) {
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
    for(e <- field.fallingPieceSetIndependent) {
      drawFallingPiece(g)(e)
    }
    g.popTransform()
    
    nextMinoDisplayType match {
      case 0 => {
        if(rule.numNext >= 1 && !field.generator(0).isEmpty) {
          g.pushTransform()
          g.translate(216, 136)
          drawNextMino(g)(field.generator(0).get)
          g.popTransform()
        }
        
        if(rule.numNext >= 2 && !field.generator(1).isEmpty) {    
          g.pushTransform()
          g.translate(304, 128)
          drawNextMino(g)(field.generator(1).get, true)
          g.popTransform()
        }
        
        g.pushTransform()
        g.translate(352, 128)
        for(i <- 2 until rule.numNext){
          if(!field.generator(i).isEmpty) drawNextMino(g)(field.generator(i).get, true)
          g.translate(0, 32)      
        }
        g.popTransform()
      }
      case 1 => {
        if(rule.numNext >= 1 && !field.generator(0).isEmpty) {
          g.pushTransform()
          g.translate(216, 136)
          drawNextMino(g)(field.generator(0).get)
          g.popTransform()
        }
        
        g.pushTransform()
        g.translate(352, 128)
        for(i <- 1 + nextMinoDisplayCursor until 6 + nextMinoDisplayCursor){
          if(!field.generator(i).isEmpty) drawNextMino(g)(field.generator(i).get, true)
          g.translate(0, 32)
        }
        g.popTransform()
        
        if(nextMinoDisplayCursor > 0) Resource.boldfont.drawString("^", 372, 96, TextAlign.CENTER)
        if(nextMinoDisplayCursor + 6 < field.generator.size) Resource.boldfont.drawString("v", 372, 256, TextAlign.CENTER)
      }
    }

    
    g.pushTransform()
    g.translate(160, 128)
    if(field.holdMino != null) drawNextMino(g)(field.holdMino, true)
    g.popTransform()
    
    Resource.frame.draw(152, 144)
    
    Resource.frame.draw(456, 144)
    
    g.setColor(new Color(1f, 1f, 1f))
    Resource.boldfont.drawString("PhaseID: %d\nPosition: %s\nTimer: %d\nFall: %f\nSoft: %f\nLock: %d\nForce: %d\nDirection: %d\nFirstMove: %d\nMove: %f\nLines: %d\nBomb: %d\nFallPiece: %f\nChain: %d\nLastLines: %d".
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
            chain,
            lastLines),
            472, 160)
            
    handler.render(this, g)
  }
    
  /*override def graphicId(): Int = {
    var id = block.id
    if(0 <= id && id < 64) id
    else if(id == 64) 128
    else if(id == 65) 129
    else if(id == 66) 130
    else if(id == 67) 193
    else if(id == 68) 194
    else if(69 <= id && id <= 86) (id - 69) + 133
    else id
  }*/
}