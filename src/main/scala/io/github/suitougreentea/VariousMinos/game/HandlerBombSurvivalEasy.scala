package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.{MinoGeneratorBombInfinite, MinoGeneratorConfigBombInfinite, Resource}
import io.github.suitougreentea.util.TextAlign
import org.newdawn.slick.Graphics

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