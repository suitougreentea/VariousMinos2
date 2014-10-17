package io.github.suitougreentea.VariousMinos

import scala.collection.mutable.ListBuffer

class FallingPiece {
  private var _array: ListBuffer[Array[Block]] = ListBuffer.empty
  var y = -1
  def apply(x: Int, y:Int): Block = {
    var ny = y - this.y
    if(x < 0 || x >= 10 || ny < 0 || ny >= height) new Block(-1) else _array(ny)(x)
  }
  def update(x: Int, y:Int, value: Block) = {
    if(this.y == -1) this.y = y
    var ny = y - this.y
    if(x >= 0 && x < 10 && ny >= 0) {
      while(ny >= height){
        _array :+= Array.fill(10)(new Block(0))
      }
      _array(ny)(x) = value
    }
  }
  def height = _array.size
}