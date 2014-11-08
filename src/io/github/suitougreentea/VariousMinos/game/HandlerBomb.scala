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
  
  override def init(game: GameBomb){
    /*game.field.generator = new MinoGeneratorBombInfinite(
        game.rule, 
        new MinoGeneratorConfigBombInfinite(
            HashSet(4, 5, 6, 7, 8, 9, 10),
            bombFrequency = 2,
            allBombFrequency = 15
         )
    )*/
    game.field.generator = new MinoGeneratorBombInfinite(
        game.rule, 
        new MinoGeneratorConfigBombInfinite(
            HashSet(4, 5, 6, 7, 8, 9, 10),
            bombFrequency = 2,
            whiteFrequency = 20,
            whiteLevel = 4,
            blackFrequency = 2,
            blackLevel = 0,
            blackOffset = 1,
            yellowFrequency = 3,
            yellowLevel = 4
         )
    )  
    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 20
    game.forceLockdownTimerMax = 180
    game.phaseMoving.beforeTime = 5
    game.bombTimerMiddle = 4
    game.bombTimerMax = 4
    game.phaseMakingBigBomb.beforeTime = 0
    game.phaseMakingBigBomb.afterTime = 0
    game.fallingPieceCounterDelta = 30f
  }
  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString(nextStop.toString(), 384, 512, TextAlign.RIGHT)
    Resource.boldfont.drawString(level.toString(), 384, 488, TextAlign.RIGHT)
  }
  override def newMino(game: GameBomb) {
    increaseLevel(1, false)
  }
  override def fillLine(game: GameBomb, hit: Int, chain: Int, freeze: Boolean) {
    if(freeze){
      increaseLevel(hit, false)
    } else {
      increaseLevel(hit, true)
    }
  }
  override def afterBomb(game: GameBomb, eraseBlocks: Int) {
    this.eraseBlocks += eraseBlocks
    while(this.eraseBlocks >= 50) {
      increaseLevel(1, false)
      this.eraseBlocks -= 50
    }
  }
  override def makeBigBomb(game: GameBomb, num: Int) {
    increaseLevel(num, false)
  }
  
  def increaseLevel(num: Int, breakStop: Boolean) {
    if(breakStop || nextStop - level > 1) {
      level += num
      if(level >= nextStop){
        nextStop += 100
      }
    }
  }
}