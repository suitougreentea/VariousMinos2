package io.github.suitougreentea.VariousMinos.game

import org.newdawn.slick.Graphics
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.Stage
import io.github.suitougreentea.VariousMinos.Block
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombFinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombFinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombInfinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombInfinite
import scala.collection.mutable.ArraySeq
import scala.collection.mutable.HashSet
import io.github.suitougreentea.util.TextAlign
import io.github.suitougreentea.VariousMinos.Phase

class HandlerBomb {  
  def init(game: GameBomb) {}
  def allClear(game: GameBomb) {}
  def stuck(game: GameBomb) { game.wrapper.exit(0) }
  def noNewMino(game: GameBomb) { game.wrapper.exit(0) }
  def newMino(game: GameBomb) {}
  def fillLine(game: GameBomb, hit: Int, chain: Int, freeze: Boolean) {}
  def afterBomb(game: GameBomb, eraseBlocks: Int) {}
  def makeBigBomb(game: GameBomb, num: Int) {}
  def render(game: GameBomb, g: Graphics) {}
}

class HandlerBombEndless extends HandlerBomb {
  override def init(game: GameBomb) {
    game.field.generator = new MinoGeneratorBombInfinite(game.rule, new MinoGeneratorConfigBombInfinite(HashSet(4, 5, 6, 7, 8, 9, 10))) 
  }
}

class HandlerBombContest(val stage: Stage) extends HandlerBomb {
  override def init(game: GameBomb){
    for(iy <- 0 until stage.field.size; ix <- 0 until 10){
      game.field(ix, iy) = new Block(stage.field(iy)(ix))
    }
    game.field.generator = new MinoGeneratorBombInfinite(game.rule, stage.mino.asInstanceOf[MinoGeneratorConfigBombInfinite]) 
  }
  override def allClear(game: GameBomb) {
    game.wrapper.exit(1)
  }
}

class HandlerBombPuzzle(val stage: Stage) extends HandlerBomb {
  override def init(game: GameBomb) {
    for(iy <- 0 until stage.field.size; ix <- 0 until 10){
      game.field(ix, iy) = new Block(stage.field(iy)(ix))
    }
    game.rule.enableHold = false
    game.field.generator = new MinoGeneratorBombFinite(game.rule, stage.mino.asInstanceOf[MinoGeneratorConfigBombFinite]) 
  }
}

class HandlerBombSurvival extends HandlerBomb {
  var level = 0
  var nextStop = 100
  var eraseBlocks = 0
  var endLevel = Int.MaxValue
  
  var handleLevel: List[Int] = List.empty
  var handleLevelIterator = 0
  
  override def init(game: GameBomb){
  }
  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString(nextStop.toString(), 384, 512, TextAlign.RIGHT)
    Resource.boldfont.drawString(level.toString(), 384, 488, TextAlign.RIGHT)
  }
  override def newMino(game: GameBomb) {
    increaseLevel(game, 1, false)
  }
  
  override def fillLine(game: GameBomb, hit: Int, chain: Int, freeze: Boolean) {
    if(freeze){
      increaseLevel(game, hit, false)
    } else {
      increaseLevel(game, hit, true)
    }
  }
  override def afterBomb(game: GameBomb, eraseBlocks: Int) {
    this.eraseBlocks += eraseBlocks
    while(this.eraseBlocks >= 50) {
      increaseLevel(game, 1, false)
      this.eraseBlocks -= 50
    }
  }
  
  override def makeBigBomb(game: GameBomb, num: Int) {
    increaseLevel(game, num, false)
  }
  
  def increaseLevel(game: GameBomb, num: Int, breakStop: Boolean) {
    if(breakStop || nextStop - level > 1) {
      level += num
      if(level > endLevel) {
        level = endLevel
      }else if(level >= nextStop){
        nextStop += 100
      }
      checkHandle(game)
    }
  }
  
  def checkHandle(game: GameBomb){
    if(handleLevelIterator < handleLevel.size) {
      var nextLevel = handleLevel(handleLevelIterator)
      if(level >= nextLevel) {
        handle(game, nextLevel)
        handleLevelIterator += 1
        checkHandle(game)
      }
    }
  }
  
  def handle(game: GameBomb, level: Int) {
    
  }
  

}

class HandlerBombSurvivalEasy extends HandlerBombSurvival {
  handleLevel = List(20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300)
  endLevel = 300
  var ending = false
  var endingMinos = 50
  
  override def init(game: GameBomb) {
    super.init(game)
    game.field.generator = new MinoGeneratorBombInfinite(
        game.rule, 
        new MinoGeneratorConfigBombInfinite(
            HashSet(4, 5, 6, 7, 8, 9, 10),
            bombFrequency = 1,
            allBombFrequency = 15
         )
    )
    
    setGravity(game, speed(0))
    game.lockdownTimerMax = 60
    game.forceLockdownTimerMax = 180
    game.phaseMoving.beforeTime = 20
    game.bombTimerMiddle = 10
    game.bombTimerMax = 20
    game.phaseMakingBigBomb.beforeTime = 0
    game.phaseMakingBigBomb.afterTime = 0
    game.fallingPieceCounterDelta = 30f
  }
  
  def speed(handleLevel: Int) = handleLevel match {
    case 0 => 1/60f
    case 20 => 1.3f/60f
    case 40 => 2/60f
    case 60 => 2.8f/60f
    case 80 => 4/60f
    case 100 => 5.8f/60f
    case 120 => 7/60f
    case 140 => 8/60f
    case 160 => 9/60f
    case 180 => 10/60f
    case 200 => 1/60f
    case 210 => 2/60f
    case 220 => 3/60f
    case 230 => 5/60f
    case 240 => 8/60f
    case 250 => 12/60f
    case 260 => 20/60f
    case 270 => 30/60f
    case 280 => 45/60f
    case 290 => 60/60f
    case 300 => 30f
  }
  
  def setGravity(game: GameBomb, gravity: Float) {
    game.fallCounterDelta = gravity
  }

  override def newMino(game: GameBomb) {
    super.newMino(game)
    if(ending){
      if(endingMinos == 0) game.wrapper.exit(1) else endingMinos -= 1
    }
  }
  
  override def render(game: GameBomb, g: Graphics) {
    super.render(game, g)
    if(ending){
      Resource.boldfont.drawString(endingMinos.toString(), 384, 456, TextAlign.RIGHT)
    }
  }
  
  override def handle(game: GameBomb, level: Int){
    setGravity(game, speed(level))
    if(level == 100){
      game.phaseMoving.beforeTime = 16
      game.bombTimerMiddle = 10
      game.bombTimerMax = 20
    }
    if(level == 200){
      game.phaseMoving.beforeTime = 12
      game.bombTimerMiddle = 8
      game.bombTimerMax = 15
    }
    if(level == 300){
      game.phaseMoving.beforeTime = 10
      game.bombTimerMiddle = 7
      game.bombTimerMax = 12
      game.allEraseFlag = true
      ending = true
    }
  }
}