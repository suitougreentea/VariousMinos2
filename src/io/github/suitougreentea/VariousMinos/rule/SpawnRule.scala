package io.github.suitougreentea.VariousMinos.rule

trait SpawnRule {
  def get(minoId: Int): (Int, Int, Int) = (2, 18, 0)
}

class SpawnRuleStandard extends SpawnRule {
  override def get(minoId: Int) = (0, 0, 2)
}