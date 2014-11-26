package io.github.suitougreentea.VariousMinos

import io.github.suitougreentea.VariousMinos.rule.Rule
import scala.collection.mutable.HashSet
import scala.collection.mutable.ArraySeq
import scala.collection.mutable.Queue

trait MinoGenerator {
  var queue: Queue[Mino] = Queue.empty
  def init()
  def size = queue.size
  def apply(i: Int) = queue.get(i)
  def dequeue(): Mino
  val infinite: Boolean
}

abstract class MinoGeneratorInfinite(size: Int = 7) extends MinoGenerator {
  def init() {
    for(i <- 0 until size) queue += generate()
  }
  val infinite = true
    def dequeue() = {
    if(infinite) queue += generate()
    queue.dequeue()
  }
  def generate(): Mino
}

abstract class MinoGeneratorFinite extends MinoGenerator {
  val infinite = false
  def init() {}
  def dequeue() = queue.dequeue()
}

class MinoGeneratorBombInfinite(val rule: Rule, val config: MinoGeneratorConfigBombInfinite, size: Int = 7) extends MinoGeneratorInfinite(size) {
  var bombCounter = config.bombOffset
  var allBombCounter = config.allBombOffset
  var whiteCounter = config.whiteOffset
  var blackCounter = config.blackOffset
  var yellowCounter = config.yellowOffset
  rule.randomizer.init(config.set)
  super.init()
  
  def generate() = {
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
  for(j <- 0 until config.list.size){
    var e = config.list(j)
    var num = MinoList.numBlocks(e.id)
    var array = Array.fill(num)(new Block(rule.color.get(e.id)))
    if(e.bomb != -1) array(e.bomb) = new Block(64)
    queue += new Mino(e.id, rule.spawn.getRotation(e.id), array)
  }
}