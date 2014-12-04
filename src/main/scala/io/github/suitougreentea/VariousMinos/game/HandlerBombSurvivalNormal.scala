package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.Resource
import org.newdawn.slick.Graphics
import io.github.suitougreentea.util.TextAlign
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombInfinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombInfinite
import scala.collection.mutable.HashSet

class HandlerBombSurvivalNormal extends HandlerBombSurvival {
  var handleLevel: List[Int] = List(20, 40, 60, 80, 100, 130, 160, 200, 250, 300, 350, 400, 500)
  var endLevel: Int = 500

  def init(game: GameBomb) {
    game.field.generator = new MinoGeneratorBombInfinite(
      game.rule,
      new MinoGeneratorConfigBombInfinite(
        Set(4, 5, 6, 7, 8, 9, 10),
        bombFrequency = 2,
        allBombFrequency = 30
      )
    )

    applyDefaultSpeed(game)
    game.phaseMoving.beforeTime = 25
    game.bombTimerMiddle = 10
    game.bombTimerMax = 20
    game.fallingPieceCounterDelta = 1f
    game.phaseMakingBigBomb.beforeTime = 8
    game.phaseMakingBigBomb.afterTime = 8
    setParam(game, param(0))
  }

  def handle(game: GameBomb, level: Int): Unit = {
    setParam(game, param(level))
    if(level == 200) game.fallingPieceCounterDelta = 30f
    if(level == 300) {
      game.phaseMakingBigBomb.beforeTime = 4
      game.phaseMakingBigBomb.afterTime = 4
      game.bombTimerMiddle = 8
      game.bombTimerMax = 16
    }
  }

  // gravity, lock
  def param(level: Int) = level match {
    case 0 => (1/60f, 60)
    case 20 => (1/48f, 60)
    case 40 => (1/32f, 60)
    case 60 => (1/24f, 60)
    case 80 => (1/16f, 60)
    case 100 => (1/8f, 60)
    case 130 => (1/4f, 60)
    case 160 => (1/2f, 60)
    case 200 => (1f, 55)
    case 250 => (3f, 50)
    case 300 => (30f, 50)
    case 350 => (30f, 43)
    case 400 => (30f, 35)
    case 500 => (30f, 20)
  }

  def setParam(game: GameBomb, speed: (Float, Int)): Unit = {
    val (gravity, lock) = speed
    game.fallCounterDelta = gravity
    game.lockdownTimerMax = lock
  }
}
