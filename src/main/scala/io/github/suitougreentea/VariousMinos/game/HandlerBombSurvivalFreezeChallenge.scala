package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.{MinoGeneratorBombInfinite, MinoGeneratorConfigBombInfinite}


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

    applyDefaultSpeed(game)
    game.phaseMoving.beforeTime = 10
    game.firstMoveTimerMax = 8
    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 30
    game.bombTimerMiddle = 4
    game.bombTimerMax = 5
    game.phaseMakingBigBomb.beforeTime = 2
    game.phaseMakingBigBomb.afterTime = 2
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
      if(level == 700) game.firstMoveTimerMax = 6
    }
  }
}