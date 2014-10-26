package io.github.suitougreentea.VariousMinos

trait MinoColor {
  def getMinoColor(minoId: Int) = 1
}

class MinoColorStandard extends MinoColor {
  override def getMinoColor(minoId: Int) = minoId + 1
}