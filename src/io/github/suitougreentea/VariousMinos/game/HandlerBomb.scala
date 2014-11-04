package io.github.suitougreentea.VariousMinos.game

import org.newdawn.slick.Graphics
import io.github.suitougreentea.VariousMinos.Resource

class HandlerBomb {  
  def init(game: GameBomb) {}
  def allClear(game: GameBomb) {}
  def stuck(game: GameBomb) { game.wrapper.exit(0) }
  def noNewMino(game: GameBomb) { game.wrapper.exit(0) }
  def render(game: GameBomb, g: Graphics) {}
}

class HandlerBombEndless extends HandlerBomb {
  
}

class HandlerBombContest extends HandlerBomb {
  override def allClear(game: GameBomb) {
    game.wrapper.exit(1)
  }
}

class HandlerBombPuzzle extends HandlerBomb {
  override def init(game: GameBomb) {
    game.rule.enableHold = false
  }
}

class HandlerBombSurvival extends HandlerBomb {
  override def init(game: GameBomb){
    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 20
    game.forceLockdownTimerMax = 180
    game.phaseMoving.beforeTime = 5
    
  }
  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString("1000", 0, 0)
  }
}