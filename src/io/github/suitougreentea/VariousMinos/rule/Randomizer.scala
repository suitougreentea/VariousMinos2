package io.github.suitougreentea.VariousMinos.rule

import scala.collection.mutable.HashSet

trait Randomizer {
  var minoSet: HashSet[Int] = null
  def next(): Int
  def reset()
}

class RandomizerRandom extends Randomizer {
  def next() = {
    var list = minoSet.toList
    list(Math.random * list.size toInt)
  }
  def reset() {}
}