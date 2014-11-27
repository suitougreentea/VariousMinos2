package io.github.suitougreentea.VariousMinos.rule

trait MinoColor {
  def get(minoId: Int) = 1
}

class MinoColorClassic extends MinoColor {
  // TODO
  override def get(minoId: Int) = 28
}

class MinoColorStandard extends MinoColor {
  val color = Array(
      20, 20, 20, 8, 
      20, 26, 5, 8, 14, 32, 2, 
      14, 2, 20, 5, 26, 2, 14, 26, 5, 32, 8, 8, 8, 8, 32, 32, 2, 14)
  override def get(minoId: Int) = color(minoId)
}

class MinoColorVariant extends MinoColor {
  val color = Array(
      2, 2, 2, 8, 
      2, 26, 5, 8, 32, 20, 14,
      32, 14, 2, 5, 26, 14, 32, 26, 5, 20, 8, 8, 8, 8, 20, 20, 14, 32)
  override def get(minoId: Int) = color(minoId)
}