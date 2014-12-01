package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.Resource
import org.newdawn.slick.Graphics
import io.github.suitougreentea.util.TextAlign
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombInfinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombInfinite
import scala.collection.mutable.HashSet

trait HandlerBombSurvival extends HandlerBomb {
  var level = 0
  var nextStop = 100
  var eraseBlocks = 0
  var endLevel: Int
  
  var handleLevel: List[Int]
  var handleLevelIterator = 0
  
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
  
  def handle(game: GameBomb, level: Int)
}

class HandlerBombSurvivalEasy extends HandlerBombSurvival {
  var handleLevel = List(20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300)
  var endLevel = 300
  var ending = false
  var endingMinos = 50
  
  def init(game: GameBomb) {
    game.field.generator = new MinoGeneratorBombInfinite(
        game.rule, 
        new MinoGeneratorConfigBombInfinite(
            Set(4, 5, 6, 7, 8, 9, 10),
            bombFrequency = 1,
            allBombFrequency = 15
         )
    )
    
    setGravity(game, speed(0))
    game.lockdownTimerMax = 60
    game.forceLockdownTimerMax = 180
    game.phaseMoving.beforeTime = 25
    game.bombTimerMiddle = 10
    game.bombTimerMax = 20
    game.phaseMakingBigBomb.beforeTime = 8
    game.phaseMakingBigBomb.afterTime = 8
    game.fallingPieceCounterDelta = 1f
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
  
  def handle(game: GameBomb, level: Int){
    setGravity(game, speed(level))
    if(level == 100){
      game.phaseMoving.beforeTime = 20
      game.bombTimerMiddle = 10
      game.bombTimerMax = 20
    }
    if(level == 200){
      game.phaseMoving.beforeTime = 16
      game.bombTimerMiddle = 8
      game.bombTimerMax = 15
    }
    if(level == 300){
      game.phaseMoving.beforeTime = 14
      game.bombTimerMiddle = 7
      game.bombTimerMax = 12
      game.allEraseFlag = true
      ending = true
    }
  }
}

class HandlerBombSurvivalNormal extends HandlerBombSurvival {
  var handleLevel: List[Int] = ???
  var endLevel: Int = 500
  
  def init(game: GameBomb) {
    ???
  }
  
  def handle(game: GameBomb, level: Int): Unit = {
    ???
  }
}

class HandlerBombSurvivalHard extends HandlerBombSurvival {
  var handleLevel: List[Int] = ???
  var endLevel: Int = 1000
  
  def init(game: GameBomb) {
    ???
  }
  
  def handle(game: GameBomb, level: Int): Unit = {
    ???
  }
}

class HandlerBombSurvivalVeryHard extends HandlerBombSurvival {
  var handleLevel: List[Int] = ???
  var endLevel: Int = 1000
  
  def init(game: GameBomb) {
    ???
  }
  
  def handle(game: GameBomb, level: Int): Unit = {
    ???
  }
}

class HandlerBombSurvivalInsane extends HandlerBombSurvival {
  var handleLevel: List[Int] = ???
  var endLevel: Int = 1500
  
  def init(game: GameBomb) {
    ???
  }
  
  def handle(game: GameBomb, level: Int): Unit = {
    ???
  }
}

class HandlerBombSurvivalFreezeChallenge(val color: Int, val blockLevel: Int) extends HandlerBombSurvival {
  var handleLevel: List[Int] = List(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
  var endLevel: Int = 1000
  
  def init(game: GameBomb) {
    game.field.generator = color match {
      case 0 => new MinoGeneratorBombInfinite(
        game.rule,
        new MinoGeneratorConfigBombInfinite(
          Set(4, 5, 6, 7, 8, 9, 10),
          bombFrequency = 2,
          allBombFrequency = 25,
          whiteFrequency = 1,
          whiteLevel = blockLevel
        )
      )
      case 1 => new MinoGeneratorBombInfinite(
        game.rule,
        new MinoGeneratorConfigBombInfinite(
          Set(4, 5, 6, 7, 8, 9, 10),
          bombFrequency = 2,
          allBombFrequency = 25,
          blackFrequency = 1,
          blackLevel = blockLevel
        )
      )
    }

    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 30
    game.forceLockdownTimerMax = 180
    game.phaseMoving.beforeTime = 10
    game.bombTimerMiddle = 5
    game.bombTimerMax = 5
    game.phaseMakingBigBomb.beforeTime = 2
    game.phaseMakingBigBomb.afterTime = 2
    game.fallingPieceCounterDelta = 30f
  }
  
  // lock, movebefore
  def speed(level: Int) = level match {
    case 0 => (30, 10)
    case 100 => (28, 10)
    case 200 => (26, 10)
    case 300 => (24, 10)
    case 400 => (20, 10)
    case 500 => (18, 10)
    case 600 => (16, 10)
    case 700 => (15, 10)
    case 800 => (15, 8)
    case 900 => (12, 6)
  }

  def setSpeed(game: GameBomb, speed: (Int, Int)): Unit = {
    val (lock, before) = speed
    game.lockdownTimerMax = lock
    game.phaseMoving.beforeTime = before
  }
  
  def handle(game: GameBomb, level: Int): Unit = {
    if(level == 1000) {
      game.wrapper.exit(1)
    } else {
      setSpeed(game, speed(level))
    }
  }
}