package io.github.suitougreentea.VariousMinos.rule

trait MinoColor {
  def get(minoId: Int) = 1
}

class MinoColorClassic extends MinoColor {
  // TODO
  override def get(minoId: Int) = 28
}

class MinoColorStandard extends MinoColor {
  // TODO
  override def get(minoId: Int) = minoId + 1
}

class MinoColorVariant extends MinoColor {
  // TODO
  override def get(minoId: Int) = minoId + 1
}