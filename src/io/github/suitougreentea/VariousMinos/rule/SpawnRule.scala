package io.github.suitougreentea.VariousMinos.rule

trait SpawnRule {
  def getPosition(minoId: Int): (Int, Int) = (2, 18)
  def getRotation(minoId: Int): Int = 0
}

class SpawnRuleClassic extends SpawnRule {
  //TODO: Investigate
  override def getPosition(minoId: Int) = (2, 18)
  override def getRotation(minoId: Int) = 2
}

class SpawnRuleStandard extends SpawnRule {
  override def getPosition(minoId: Int) = {
    minoId match {
      case 1 => (3, 19)  //w2h1
      case 2 => (2, 19)  //w3h1
      case 4 => (2, 19)  //w4h1
      case 13 => (1, 19)  //w5h1
      case 3 | 7 => (3, 20)  //w2h2
      case 5 | 6 | 8 | 9 | 10 | 18 | 19 | 21 => (2, 20)  //w3h2
      case 14 | 15 | 16 | 17 | 25 | 26 => (2, 20)  //w4h2
      case 11 | 12 | 20 | 22 | 23 | 24 | 27 | 28 => (2, 21)  //w3h3
    }
  }
  override def getRotation(minoId: Int) = 0
}

class SpawnRuleVariant extends SpawnRule {
  override def getPosition(minoId: Int) = (2, 18)
  override def getRotation(minoId: Int) = 2
}