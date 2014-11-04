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

class HandlerBomb {  
  def init(game: GameBomb) {}
  def allClear(game: GameBomb) {}
  def stuck(game: GameBomb) { game.wrapper.exit(0) }
  def noNewMino(game: GameBomb) { game.wrapper.exit(0) }
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
  override def init(game: GameBomb){
    game.field.generator = new MinoGeneratorBombInfinite(game.rule, new MinoGeneratorConfigBombInfinite(HashSet(4, 5, 6, 7, 8, 9, 10))) 
    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 20
    game.forceLockdownTimerMax = 180
    game.phaseMoving.beforeTime = 5
    
  }
  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString("1000", 0, 0)
  }
}