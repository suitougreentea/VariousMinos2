package io.github.suitougreentea.VariousMinos

import io.github.suitougreentea.VariousMinos.rule.Rule
import scala.collection.mutable.HashSet
import scala.collection.mutable.ArraySeq

trait MinoGenerator {
  def next(): Mino
  val infinite: Boolean
}

trait MinoGeneratorInfinite extends MinoGenerator {
  val infinite = true
}

trait MinoGeneratorFinite extends MinoGenerator {
  var list: ArraySeq[Mino]
  val infinite = false
}

class MinoGeneratorBombInfinite(val rule: Rule, val config: MinoGeneratorConfigBombInfinite) extends MinoGeneratorInfinite {
  rule.randomizer.init(config.set)
  def next() = {
    var id = rule.randomizer.next()
    var num = MinoList.numBlocks(id)
    var array = Array.fill(num)(new Block(rule.color.get(id)))
    //var array = Array.fill(num)(new Block(73))
    array(Math.random() * num toInt) = new Block(64)
    new Mino(id, rule.spawn.getRotation(id), array)
  }
}

class MinoGeneratorBombFinite(val rule: Rule, val config: MinoGeneratorConfigBombFinite) extends MinoGeneratorFinite {
  var list: ArraySeq[Mino] = ArraySeq.fill(config.list.size)(null)
  for(j <- 0 until config.list.size){
    var (id, bomb) = config.list(j)
    var num = MinoList.numBlocks(id)
    var array = Array.fill(num)(new Block(rule.color.get(id)))
    if(bomb != -1) array(bomb) = new Block(64)
    list(j) = new Mino(id, rule.spawn.getRotation(id), array)
  }
  def next() = {
    if(list.contains(0)) list.apply(0) else null
  }
}