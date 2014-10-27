package io.github.suitougreentea.VariousMinos.rule

trait MinoColor {
  def get(minoId: Int) = 1
}

class MinoColorClassic extends MinoColor {
  override def get(minoId: Int) = 28
}

class MinoColorStandard extends MinoColor {
  override def get(minoId: Int) = minoId + 1
}