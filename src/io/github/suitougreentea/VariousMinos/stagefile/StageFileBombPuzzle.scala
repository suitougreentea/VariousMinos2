package io.github.suitougreentea.VariousMinos.stagefile

case class StageFileBombPuzzle (
  stages: List[BombPuzzleStage]
) {
}

case class BombPuzzleStage (
  mino_list: List[BombPuzzleMinoList],
  gravity: Float,
  lock: Int,
  field: List[Array[Int]]
) {
}

case class BombPuzzleMinoList (
  id: Int,
  // 0: normal, 1: allbomb, 2: white, 3: black
  `type`: Int,
  bomb: Int
) {  
}