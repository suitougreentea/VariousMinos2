package io.github.suitougreentea.VariousMinos

import io.github.suitougreentea.VariousMinos.rule.Rule
import scala.language.dynamics
import scala.collection.mutable.ArraySeq
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashSet

class Field(var rule: Rule) {
  private var _field = IndexedSeq.fill(30)(Array.fill(10)(new Block(0)))
  def field = _field
  
  def apply(x: Int, y:Int): Block = if(x < 0 || x >= 10 || y < 0 || y >= height) new Block(-1) else _field(y)(x)
  def update(x: Int, y:Int, value: Block) = if(x >= 0 && x < 10 && y >= 0 && y < height) _field(y)(x) = value
  
  def height = _field.size
  
  var currentMino: Mino = _
  var currentMinoX = 0
  var currentMinoY = 0
  
  var generator: MinoGenerator = _
  
  var holdMino: Mino = null
  var alreadyHolded = false
  
  var fallingPieceSet: HashSet[FallingPiece] = HashSet.empty
  var fallingPieceSetIndependent: ListBuffer[FallingPiece] = ListBuffer.empty
  
  def init(){
    generator.init()
  }
  
  def ghostY: Int = {
    var result = currentMinoY
    while(!checkHit(minoPos = (currentMinoX, result))) result -= 1
    result + 1
  }
  
  def moveMinoRight(): Boolean = {
    currentMinoX += 1
    if(checkHit()) {
      currentMinoX -= 1
      false
    } else {
      true
    }
  }

  def moveMinoLeft(): Boolean = {
    currentMinoX -= 1
    if(checkHit()) {
      currentMinoX += 1
      false
    } else {
      true
    }
  }

  def moveMinoDown(): Boolean = {
    currentMinoY -= 1
    if(checkHit()) {
      currentMinoY += 1
      false
    } else {
      true
    }
  }

  def moveMinoUp(): Boolean = {
    currentMinoY += 1
    if(checkHit()) {
      currentMinoY -= 1
      false
    } else {
      true
    }
  }
  
  def rotateMinoCW(): Boolean = {
    rule.rotation.rotateCW(this)
  }

  def rotateMinoCCW(): Boolean = {
    rule.rotation.rotateCCW(this)
  }
  
  def hardDrop() {
    var mx = currentMinoX
    var my = ghostY
    for(iy <- 0 until 5; ix <- 0 until 5){
      if(currentMino(ix, iy).id != 0) this(mx + ix, my + iy) = currentMino(ix, iy)
    }
  }
  
  def newMino(){
    /*if(generator.infinite){
      currentMino = nextMino(0)
      for(i <- 0 until 6) _nextMino(i) = nextMino(i + 1)
      _nextMino(6) = generator.next()
    } else {
      val gen = generator.asInstanceOf[MinoGeneratorFinite]
      currentMino = gen.list(0)
      for(i <- 0 until gen.list.size - 1) gen.list(i) = gen.list(i + 1)
      gen.list(gen.list.size - 1) = null
    }*/
    
    currentMino = generator.dequeue()
    
    var (x, y) = rule.spawn.getPosition(currentMino.minoId)
    currentMinoX = x
    currentMinoY = y
    alreadyHolded = false
  }
  
  // returns true if this function generated new mino
  def hold(): Boolean = {
    var result = false
    if(rule.enableHold) {
      if(!alreadyHolded){
        if(holdMino == null){
          holdMino = currentMino
          newMino()
          result = true
        } else {
          var temp = currentMino
          currentMino = holdMino
          holdMino = temp
          var (x, y) = rule.spawn.getPosition(currentMino.minoId)
          currentMinoX = x
          currentMinoY = y
        }
        (holdMino.rotationState - rule.spawn.getRotation(holdMino.minoId) + 4) % 4 match {
          case 0 => 
          case 1 => holdMino.rotateCCW()
          case 2 => holdMino.rotateDouble()
          case 3 => holdMino.rotateCW()
        }
        
        alreadyHolded = true
      }
    }
    return result
  }
  
  def checkHit(field: Field = this, mino: Mino = currentMino,
      minoPos :(Int, Int) = (currentMinoX, currentMinoY)): Boolean = {
    for(iy <- 0 until 5; ix <- 0 until 5) {
      var (mx, my) = minoPos
      if(!field(mx + ix, my + iy).transparent && !mino(ix, iy).transparent) return true
    }
    return false
  }
  
  def filledLines = {
    var result = ArraySeq.empty[Int]
    for(iy <- 0 until height) {
      var num = 0
      for(ix <- 0 until 10) {
        if(this(ix, iy).id != 0){
          num += 1
        }
      }
      if(num == 10){
        result :+= iy
      }
    }
    result
  }
  
  def makeFallingPieces() {
    for(iy <- 0 until height; ix <- 0 until 10){
      if(this(ix, iy).id > 0) {
        val piece = makeFallingPiece(new FallingPiece(), ix, iy)
        fallingPieceSet += piece
      }
    }
    
    for(e <- fallingPieceSet){
      if(e.containsPersistentBlock) {
        setFallingPiece(e)
        fallingPieceSet -= e
      } else {
        e.y -= 1
        if(checkHitFallingPiece(piece = e)) {
          e.y += 1
          setFallingPiece(e)
          fallingPieceSet -= e
        } else {
          e.y += 1
        }
      }
    }
  }
  
  def makeFallingPiece(piece: FallingPiece, x: Int, y: Int) : FallingPiece = {
    var result = piece
    if(this(x, y).persistent) result.containsPersistentBlock = true
    if(this(x, y).independent) result.containsIndependentBlock = true
    result(x, y) = this(x, y)
    this(x, y) = new Block(0)
    if(result.containsIndependentBlock) return result
    if(this(x - 1, y).id > 0 && !this(x - 1, y).independent) result = makeFallingPiece(piece, x - 1, y)
    if(this(x + 1, y).id > 0 && !this(x + 1, y).independent) result = makeFallingPiece(piece, x + 1, y)
    if(this(x, y - 1).id > 0 && !this(x, y - 1).independent) result = makeFallingPiece(piece, x, y - 1)
    if(this(x, y + 1).id > 0 && !this(x, y + 1).independent) result = makeFallingPiece(piece, x, y + 1)
    result
  }
  
  // use FallingPiece.y
  def checkHitFallingPiece(field: Field = this, piece: FallingPiece): Boolean = {
    for(iy <- piece.y until piece.y + piece.height; ix <- 0 until 10) {
      if(!field(ix, iy).transparent && !piece(ix, iy).transparent) return true
    }
    return false
  }

  def setFallingPiece(piece: FallingPiece){
    for(iy <- piece.y until piece.y + piece.height; ix <- 0 until 10) {
      if(piece(ix, iy).id > 0) this(ix, iy) = piece(ix, iy)
    }
  }
  
}