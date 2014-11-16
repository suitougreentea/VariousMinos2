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
  var bombCounter = config.bombOffset
  var allBombCounter = config.allBombOffset
  var whiteCounter = config.whiteOffset
  var blackCounter = config.blackOffset
  var yellowCounter = config.yellowOffset
  rule.randomizer.init(config.set)
  def next() = {
    var id = rule.randomizer.next()
    var num = MinoList.numBlocks(id)
    var array: Array[Block] = Array.empty
    bombCounter += 1
    allBombCounter += 1
    whiteCounter += 1
    blackCounter += 1
    yellowCounter += 1
     
    array = Array.fill(num)(new Block(rule.color.get(id)))
    if(whiteCounter == config.whiteFrequency){
      whiteCounter = 0
      array = Array.fill(num)(new Block(69 + config.whiteLevel))
    }
    if(blackCounter == config.blackFrequency){
      blackCounter = 0
      array = Array.fill(num)(new Block(75 + config.blackLevel))
    }
    if(yellowCounter == config.yellowFrequency){
      yellowCounter = 0
      array = Array.fill(num)(new Block(81 + config.yellowLevel))
    }
    if(allBombCounter == config.allBombFrequency){
      allBombCounter = 0
      array = Array.fill(num)(new Block(64))
    }
    if(bombCounter == config.bombFrequency){
      bombCounter = 0
      array(Math.random() * num toInt) = new Block(64)  
    }
    
    new Mino(id, rule.spawn.getRotation(id), array)
  }
}

class MinoGeneratorBombFinite(val rule: Rule, val config: MinoGeneratorConfigBombFinite) extends MinoGeneratorFinite {
  var list: ArraySeq[Mino] = ArraySeq.fill(config.list.size)(null)
  for(j <- 0 until config.list.size){
    var e = config.list(j)
    var num = MinoList.numBlocks(e.id)
    var array = Array.fill(num)(new Block(rule.color.get(e.id)))
    if(e.bomb != -1) array(e.bomb) = new Block(64)
    list(j) = new Mino(e.id, rule.spawn.getRotation(e.id), array)
  }
  def next() = {
    if(list.contains(0)) list.apply(0) else null
  }
}