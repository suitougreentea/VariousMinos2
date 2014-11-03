package io.github.suitougreentea.VariousMinos.rule

import io.github.suitougreentea.VariousMinos.MinoList

trait SpawnRule {
  def getPosition(minoId: Int) = {
    var r = getRotation(minoId)
    var ((x, y), (w, h)) = MinoList.rectangle(minoId, r)
    (((10 - w) / 2).toInt - x, 22 - y)
  }
  def getRotation(minoId: Int): Int = 0
}

class SpawnRuleClassic extends SpawnRuleVariant {
}

class SpawnRuleStandard extends SpawnRule {
}

class SpawnRuleVariant extends SpawnRule {
  override def getRotation(minoId: Int) = minoId match {
    case 3 | 5 | 6 | 9 | 14 | 15 | 18 | 19 | 20 | 21 | 25 | 26 => 2
    case 22 | 11 | 27 | 28 => 1
    case 12 => 3
    case _ => 0
  }
}