package io.github.suitougreentea.VariousMinos

class Mino (val minoId: Int, defaultRotationState: Int, blocks: Array[Block]){
  private var _array = Array.fill(5)(Array.fill(5)(new Block(0)))
  private var _rotationState = 0
  def rotationState = _rotationState
  
  if(minoId >= 0) {
    val _list = MinoList(minoId)
    for (i <- 0 until _list.length) {
      var (x, y) = _list(i)
      this(x, y) = blocks(i)
    } 
  }
  
  defaultRotationState match {
    case 0 =>
    case 1 => rotateCW()
    case 2 => rotateDouble()
    case 3 => rotateCCW()
  }
  
  def this (minoId: Int, defaultRotationState: Int, block: Block = new Block(1)) = {
    this(minoId, defaultRotationState, Array.fill(MinoList.numBlocks(minoId))(block))
  }

  def apply(x: Int, y:Int): Block = _array(y)(x)
  def update(x: Int, y:Int, value: Block) = _array(y)(x) = value
  
  def rotateCW() {
    var result : Array[Array[Block]] = Array.fill(5)(Array.fill(5)(null))
    for(iy <- 0 until 5; ix <- 0 until 5) result(iy)(ix) = _array(ix)(4 - iy)
    _array = result
    _rotationState = (_rotationState + 1) % 4
  }

  def rotateCCW() {
    var result : Array[Array[Block]] = Array.fill(5)(Array.fill(5)(null))
    for(iy <- 0 until 5; ix <- 0 until 5) result(iy)(ix) = _array(4 - ix)(iy)
    _array = result
    _rotationState = (_rotationState + 3) % 4
  }

  def rotateDouble() {
    var result : Array[Array[Block]] = Array.fill(5)(Array.fill(5)(null))
    for(iy <- 0 until 5; ix <- 0 until 5) result(iy)(ix) = _array(4 - iy)(4 - ix)
    _array = result
    _rotationState = (_rotationState + 2) % 4
  }
}

object MinoList {
  private val _list = Array (
      Array ((2, 2)),  // 0: 1
      Array ((1, 3), (2, 3)),  // 1: 2
      Array ((1, 2), (2, 2), (3, 2)),  // 2: 3I
      Array ((1, 3), (1, 2), (2, 2)),  // 3: 3L
      Array ((0, 3), (1, 3), (2, 3), (3, 3)),  // 4: 4I
      Array ((1, 3), (1, 2), (2, 2), (3, 2)),  // 5: 4J
      Array ((3, 3), (1, 2), (2, 2), (3, 2)),  // 6: 4L
      Array ((1, 3), (2, 3), (1, 2), (2, 2)),  // 7: 4O
      Array ((2, 3), (3, 3), (1, 2), (2, 2)),  // 8: 4S
      Array ((2, 3), (1, 2), (2, 2), (3, 2)),  // 9: 4T
      Array ((1, 3), (2, 3), (2, 2), (3, 2)),  // 10: 4Z
      Array ((2, 3), (2, 2), (3, 2), (1, 1), (2, 1)),  // 11: 5F
      Array ((2, 3), (1, 2), (2, 2), (2, 1), (3, 1)),  // 12: 5FM
      Array ((0, 2), (1, 2), (2, 2), (3, 2), (4, 2)),  // 13: 5I
      Array ((3, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 14: 5L
      Array ((0, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 15: 5LM
      Array ((0, 3), (1, 3), (1, 2), (2, 2), (3, 2)),  // 16: 5N
      Array ((2, 3), (3, 3), (0, 2), (1, 2), (2, 2)),  // 17: 5NM
      Array ((1, 3), (2, 3), (1, 2), (2, 2), (3, 2)),  // 18: 5P
      Array ((2, 3), (3, 3), (1, 2), (2, 2), (3, 2)),  // 19: 5PM
      Array ((2, 3), (2, 2), (1, 1), (2, 1), (3, 1)),  // 20: 5T
      Array ((1, 3), (3, 3), (1, 2), (2, 2), (3, 2)),  // 21: 5U
      Array ((1, 3), (1, 2), (1, 1), (2, 1), (3, 1)),  // 22: 5V
      Array ((1, 3), (1, 2), (2, 2), (2, 1), (3, 1)),  // 23: 5W
      Array ((2, 3), (1, 2), (2, 2), (3, 2), (2, 1)),  // 24: 5X
      Array ((2, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 25: 5Y
      Array ((1, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 26: 5YM
      Array ((1, 3), (2, 3), (2, 2), (2, 1), (3, 1)),  // 27: 5Z
      Array ((2, 3), (3, 3), (2, 2), (1, 1), (2, 1))  // 28: 5ZM
  )
  
  def numBlocks(minoId: Int) = _list(minoId).length
  
  def apply(minoId: Int) = _list(minoId)
}
