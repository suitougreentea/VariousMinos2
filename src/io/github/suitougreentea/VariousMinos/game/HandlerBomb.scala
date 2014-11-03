package io.github.suitougreentea.VariousMinos.game

class HandlerBomb {  
  def init(game: GameBomb) {}
  def allClear(game: GameBomb) {}
  def stuck(game: GameBomb) { game.wrapper.exit(0) }
  def noNewMino(game: GameBomb) { game.wrapper.exit(0) }
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