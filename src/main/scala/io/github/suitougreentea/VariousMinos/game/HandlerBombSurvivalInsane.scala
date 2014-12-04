package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.{MinoGeneratorConfigBombInfinite, MinoGeneratorBombInfinite}

class HandlerBombSurvivalInsane extends HandlerBombSurvival {
  var handleLevel: List[Int] = List(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400)
  var endLevel: Int = 1500

  def init(game: GameBomb): Unit = {
    game.field.generator = new MinoGeneratorBombInfinite(
        game.rule,
        new MinoGeneratorConfigBombInfinite(
            Set(4, 5, 6, 7, 8, 9, 10),
            bombFrequency = 1,
            allBombFrequency = 15
         )
    )

    applyDefaultSpeed(game)
    game.phaseMoving.beforeTime = 8
    game.firstMoveTimerMax = 3
    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 15
    game.phaseErasing.beforeTime = 0
    game.bombTimerMiddle = 5
    game.bombTimerMax = 6
    game.phaseFalling.beforeTime = 6
    game.phaseMakingBigBomb.beforeTime = 0
    game.phaseMakingBigBomb.afterTime = 0
    setParam(game, param(0))
  }

  // Before, Lock
  def param(level: Int) = level match {
    case 0 => (8, 13)
    case 100 => (8, 12)
    case 200 => (7, 12)
    case 300 => (7, 11)
    case 400 => (6, 10)
    case 500 => (6, 10)
    case 600 => (6, 10)
    case 700 => (6, 10)
    case 800 => (6, 10)
    case 900 => (6, 10)
    case 1000 => (6, 10)
    case 1100 => (6, 9)
    case 1200 => (6, 8)
    case 1300 => (5, 8)
    case 1400 => (5, 7)
  }

  def setParam(game: GameBomb, param: (Int, Int)): Unit = {
    val (before, lock) = param
    game.phaseMoving.beforeTime = before
    game.lockdownTimerMax = lock
  }

  def handle(game: GameBomb, level: Int): Unit = {
    if(level == 1500){

    } else {
      setParam(game, param(level))
      if(level == 500) {
        game.phaseFalling.beforeTime = 3
      }
      if(level == 1000) {
        game.bombTimerMiddle = 3
        game.bombTimerMax = 4
        game.phaseFalling.beforeTime = 0
      }
    }
  }
}