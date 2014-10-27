package io.github.suitougreentea.VariousMinos.game

trait HandlerBomb {  
  def allClear(game: GameBomb) {}
  def stuck(game: GameBomb) { game.wrapper.exit(0) }
}

class HandlerBombEndless extends HandlerBomb {
  
}

class HandlerBombContest extends HandlerBomb {
  override def allClear(game: GameBomb) {
    game.wrapper.exit(1)
  }
}