package io.github.suitougreentea.VariousMinos.stagefile

case class StageFileBombPuzzle (
  var `type`: String = "BombPuzzle",
  var version: Int = 0,
  var stages: Array[BombPuzzleStage]
) extends StageFile[BombPuzzleStage] {
  assert(`type` == "BombPuzzle")
}

case class BombPuzzleStage (
  var mino_list: Array[BombPuzzleMinoList],
  var gravity: Int,
  var lock: Int,
  var field: List[Array[Int]]
) extends Stage {
}

case class BombPuzzleMinoList (
  var id: Int,
  // 0: normal, 1: allbomb, 2-7: white, 8-12: black
  var `type`: Int,
  var bomb: Int
) {  
}