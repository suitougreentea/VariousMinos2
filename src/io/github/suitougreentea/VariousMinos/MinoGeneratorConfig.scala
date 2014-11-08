package io.github.suitougreentea.VariousMinos

import scala.collection.mutable.HashSet

trait MinoGeneratorConfig {
  val infinite: Boolean
}

class MinoGeneratorConfigInfinite extends MinoGeneratorConfig {
  val infinite = true
}

class MinoGeneratorConfigFinite extends MinoGeneratorConfig {
  val infinite = false
}

class MinoGeneratorConfigBombInfinite(
    var set: HashSet[Int],
    var bombFrequency: Int = 1, val bombOffset: Int = 0,
    var allBombFrequency: Int = 0, val allBombOffset: Int = 0,
    var whiteFrequency: Int = 0, val whiteOffset: Int = 0, var whiteLevel: Int = 0,
    var blackFrequency: Int = 0, val blackOffset: Int = 0, var blackLevel: Int = 0,
    var yellowFrequency: Int = 0, val yellowOffset: Int = 0, var yellowLevel: Int = 0
) extends MinoGeneratorConfigInfinite {
}

class MinoGeneratorConfigBombFinite(val list: Seq[(Int, Int)]) extends MinoGeneratorConfigFinite {
  
}