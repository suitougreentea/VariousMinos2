package io.github.suitougreentea.VariousMinos.rule

import scala.collection.mutable.HashSet

trait Randomizer {
  var minoSet: HashSet[Int] = HashSet.empty
  def init(minoSet: HashSet[Int]) {
    this.minoSet = minoSet
  }
  def next(): Int
  def reset()
}

class RandomizerRandom() extends Randomizer {
  def next() = {
    var list = minoSet.toList
    list(Math.random * list.size toInt)
  }
  def reset() {}
}

class RandomizerBag() extends Randomizer {
  var bag: Array[Boolean] = Array.empty
  var list: List[Int] = List.empty
  var bagRemain = -1
  override def init(minoSet: HashSet[Int]) {
    super.init(minoSet)
    list = minoSet.toList
    resetBag()
  }
  def next(): Int = {
    if(bagRemain == 0) resetBag();
  	var n = Math.random() * bagRemain toInt;
  	var i = 0;
  	while(true){
  	   if(!bag(i)){
  	     if(n == 0) {
  		     bag(i) = true;
  		     bagRemain -= 1;
  		     return list(i);
  	     }
  	     n -= 1;
  	   }
       i += 1;
  	}
  	return -1
  }
  def reset() {}
  def resetBag() {
    bag = Array.fill(minoSet.size)(false)
    bagRemain = bag.length
    println(bagRemain)
  }
}