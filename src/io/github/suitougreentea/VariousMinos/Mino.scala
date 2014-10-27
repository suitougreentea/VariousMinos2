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
      Array ((2, 2)),  // 1
      Array ((1, 3), (2, 3)),  // 2
      Array ((1, 2), (2, 2), (3, 2)),  // 3I
      Array ((1, 3), (1, 2), (2, 2)),  // 3L
      Array ((0, 3), (1, 3), (2, 3), (3, 3)),  // 4I
      Array ((1, 3), (1, 2), (2, 2), (3, 2)),  // 4J
      Array ((3, 3), (1, 2), (2, 2), (3, 2)),  // 4L
      Array ((1, 3), (2, 3), (1, 2), (2, 2)),  // 4O
      Array ((2, 3), (3, 3), (1, 2), (2, 2)),  // 4S
      Array ((2, 3), (1, 2), (2, 2), (3, 2)),  // 4T
      Array ((1, 3), (2, 3), (2, 2), (3, 2)),  // 4Z
      Array ((2, 3), (2, 2), (3, 2), (1, 1), (2, 1)),  // 5F
      Array ((2, 3), (1, 2), (2, 2), (2, 1), (3, 1)),  // 5FM
      Array ((0, 2), (1, 2), (2, 2), (3, 2), (4, 2)),  // 5I
      Array ((3, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 5L
      Array ((0, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 5LM
      Array ((0, 3), (1, 3), (1, 2), (2, 2), (3, 2)),  // 5N
      Array ((2, 3), (3, 3), (0, 2), (1, 2), (2, 2)),  // 5NM
      Array ((1, 3), (2, 3), (1, 2), (2, 2), (3, 2)),  // 5P
      Array ((2, 3), (3, 3), (1, 2), (2, 2), (3, 2)),  // 5PM
      Array ((2, 3), (2, 2), (1, 1), (2, 1), (3, 1)),  // 5T
      Array ((1, 3), (3, 3), (1, 2), (2, 2), (3, 2)),  // 5U
      Array ((1, 3), (1, 2), (1, 1), (2, 1), (3, 1)),  // 5V
      Array ((1, 3), (1, 2), (2, 2), (2, 1), (3, 1)),  // 5W
      Array ((2, 3), (1, 2), (2, 2), (3, 2), (2, 1)),  // 5X
      Array ((2, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 5Y
      Array ((1, 3), (0, 2), (1, 2), (2, 2), (3, 2)),  // 5YM
      Array ((1, 3), (2, 3), (2, 2), (2, 1), (3, 1)),  // 5Z
      Array ((2, 3), (3, 3), (2, 2), (1, 1), (2, 1))  // 5ZM
  )
  
  def numBlocks(minoId: Int) = _list(minoId).length
  
  def apply(minoId: Int) = _list(minoId)
}
