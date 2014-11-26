package io.github.suitougreentea.VariousMinos.stagefile

case class StageFileBombContest (
  var `type`: String = "BombContest",
  var version: Int = 0,
  var stages: Array[BombContestStage]
) extends StageFile[BombContestStage] {
  assert(`type` == "BombContest")
}

case class BombContestStage (
  var mino_num: Int,
  var mino_set: List[Int],
  var bomb_frequency: Int,
  var bomb_offset: Int,
  var allbomb_frequency: Int,
  var allbomb_offset: Int,
  var white_frequency: Int,
  var white_offset: Int,
  var white_level: Int,
  var black_frequency: Int,
  var black_offset: Int,
  var black_level: Int,
  var gravity: Int,
  var lock: Int,
  var field: Array[Array[Int]]
) extends Stage {
  if(field.size < 22) field = field.padTo(22, Array.fill(10)(0))
}