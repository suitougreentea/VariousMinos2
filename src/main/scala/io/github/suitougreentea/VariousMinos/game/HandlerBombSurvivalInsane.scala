package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.{MinoGeneratorConfigBombInfinite, MinoGeneratorBombInfinite}

class HandlerBombSurvivalInsane extends HandlerBombSurvival {
  var handleLevel: List[Int] = List(100)
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
    game.phaseMoving.beforeTime = 4
    game.fallCounterDelta = 30f
    game.lockdownTimerMax = 8
    game.phaseErasing.beforeTime = 0
    game.bombTimerMiddle = 3
    game.bombTimerMax = 4
    game.phaseMakingBigBomb.beforeTime = 0
    game.phaseMakingBigBomb.afterTime = 0
  }

  def handle(game: GameBomb, level: Int): Unit = {

  }
}