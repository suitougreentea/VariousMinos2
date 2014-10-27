package io.github.suitougreentea.VariousMinos.rule

trait SpawnRule {
  def getPosition(minoId: Int): (Int, Int) = (2, 18)
  def getRotation(minoId: Int): Int = 0
}

class SpawnRuleClassic extends SpawnRule {
  override def getPosition(minoId: Int) = (2, 18)
  override def getRotation(minoId: Int) = 2
}