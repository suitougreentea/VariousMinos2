package io.github.suitougreentea.VariousMinos.stagefile

import scala.collection.mutable.HashSet

case class StageFileBombContest (
  stages: List[BombContestStage]
) {
}

case class BombContestStage(
  mino_num: Int,
  mino_set: List[Int],
  bomb_frequency: Int,
  bomb_offset: Int,
  allbomb_frequency: Int,
  allbomb_offset: Int,
  white_frequency: Int,
  white_offset: Int,
  white_level: Int,
  black_frequency: Int,
  black_offset: Int,
  black_level: Int,
  gravity: Float,
  lock: Int,
  field: List[Array[Int]]
) {
}