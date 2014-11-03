package io.github.suitougreentea.VariousMinos

import scala.collection.mutable.HashSet

/**
 * @author suitougreentea
 */
trait MinoGeneratorConfig {
  val infinite: Boolean
}

class MinoGeneratorConfigInfinite extends MinoGeneratorConfig {
  val infinite = true
}

class MinoGeneratorConfigFinite extends MinoGeneratorConfig {
  val infinite = false
}

class MinoGeneratorConfigBombInfinite(var set: HashSet[Int]) extends MinoGeneratorConfigInfinite {
  
}

class MinoGeneratorConfigBombFinite(val list: Seq[(Int, Int)]) extends MinoGeneratorConfigFinite {
  
}