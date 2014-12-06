package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.{Timer, Resource}
import io.github.suitougreentea.util.TextAlign
import org.newdawn.slick.Graphics

/*
 * Speed Structure
 * * Before Spawn
 * * DAS
 * * Gravity
 * * Lock (Force)
 * * After Spawn
 * * (Counting = 0)
 * * Before Explosion
 * * Bomb Middle/Max
 * * After Explosion
 * * Before Falling
 * * Falling Gravity
 * * After Falling
 * * Big Bomb
 */

trait HandlerBombSurvival extends HandlerBomb {
  var level = 0
  var nextStop = 100
  var eraseBlocks = 0
  var endLevel: Int
  
  var handleLevel: List[Int]
  var handleLevelIterator = 0

  var timer = new Timer()

  override def start(game: GameBomb): Unit = {
    timer.start()
  }
  override def update(game: GameBomb): Unit = {

  }

  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString(nextStop.toString(), 384, 512, TextAlign.RIGHT)
    Resource.boldfont.drawString(level.toString(), 384, 488, TextAlign.RIGHT)
    Resource.boldfont.drawString(timer.mkString("%1$02d:%2$02d:%3$02d"), 143, 512, TextAlign.RIGHT)
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

  def applyDefaultSpeed(game: GameBomb): Unit = {
    game.phaseMoving.beforeTime = 30
    game.firstMoveTimerMax = 10
    game.fallCounterDelta = 1/60f
    game.lockdownTimerMax = 60
    game.forceLockdownTimerMax = 180
    game.phaseMoving.afterTime = 0
    game.phaseCounting.beforeTime = 0
    game.phaseCounting.afterTime = 0
    game.phaseErasing.beforeTime = 10
    game.bombTimerMiddle = 15
    game.bombTimerMax = 30
    game.phaseErasing.afterTime = 0
    game.phaseFalling.beforeTime = 10
    game.fallingPieceCounterDelta = 30f
    game.phaseFalling.afterTime = 0
    game.phaseMakingBigBomb.beforeTime = 8
    game.phaseMakingBigBomb.afterTime = 8
  }

  def handle(game: GameBomb, level: Int)
}

