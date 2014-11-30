package io.github.suitougreentea.VariousMinos

case class PlayerData(
  var name: String,
  var key: KeyConfig
) {
}

case class KeyConfig(
  var up: List[Int],
  var down: List[Int],
  var left: List[Int],
  var right: List[Int],
  var a: List[Int],
  var b: List[Int],
  var c: List[Int]
) {
}
