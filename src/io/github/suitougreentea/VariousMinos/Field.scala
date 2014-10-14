package io.github.suitougreentea.VariousMinos

import scala.language.dynamics

class Field {
  private var _field = IndexedSeq.fill(30)(Array.fill(10)(new Block(0)))
  def field = _field
  
  def apply(x: Int, y:Int): Block = if(x < 0 || x >= 10 || y < 0 || y >= height) new Block(-1) else _field(y)(x)
  def update(x: Int, y:Int, value: Block) = _field(y)(x) = value
  
  def height = _field.size
  
  var currentMino: Mino = _
  var currentMinoX = 0
  var currentMinoY = 0
  
  var generateMino = () => new Mino(0, 0, new Block(1))
  var nextMino: Array[Mino] = _
  var holdMino: Mino = null
  var alreadyHolded = false
  
  def init(){
    nextMino = Array.fill(7)(generateMino())
    newMino()
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
    currentMino.rotateCW()
    if(checkHit()) {
      currentMino.rotateCCW()
      false
    } else {
      true
    }
  }

  def rotateMinoCCW(): Boolean = {
    currentMino.rotateCCW()
    if(checkHit()) {
      currentMino.rotateCW()
      false
    } else {
      true
    }
  }
  
  def hardDrop() {
    var mx = currentMinoX
    var my = ghostY
    for(iy <- 0 until 5; ix <- 0 until 5){
      if(currentMino(ix, iy).id != 0) this(mx + ix, my + iy) = currentMino(ix, iy)
    }
  }
  
  def newMino(){
    currentMino = nextMino(0)
    currentMinoX = 2
    currentMinoY = 18
    for(i <- 0 until 6) nextMino(i) = nextMino(i+1)
    nextMino(6) = generateMino()
    alreadyHolded = false
  }
  
  def hold(){
    if(!alreadyHolded){
      if(holdMino == null){
        // TODO: Revert rotation
        holdMino = currentMino
        newMino()
      } else {
        var temp = currentMino
        currentMino = holdMino
        holdMino = temp
        currentMinoX = 2
        currentMinoY = 18
      }
      alreadyHolded = true
    }
  }
  
  def checkHit(field: Field = this, mino: Mino = currentMino,
      minoPos :(Int, Int) = (currentMinoX, currentMinoY)): Boolean = {
    for(iy <- 0 until 5; ix <- 0 until 5) {
      var (mx, my) = minoPos
      if(!field(mx + ix, my + iy).transparent && !mino(ix, iy).transparent) return true
    }
    return false
  }

}