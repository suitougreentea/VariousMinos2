package io.github.suitougreentea.VariousMinos

import scala.language.dynamics

class Field {
  private var _field = IndexedSeq.fill(22)(Array.fill(10)(new Block(0)))
  def field = _field
  
  def apply(x: Int, y:Int): Block = if(x < 0 || x >= 10 || y < 0 || y >= height) new Block(-1) else _field(y)(x)
  def update(x: Int, y:Int, value: Block) = _field(y)(x) = value
  
  def height = _field.size
  
  var currentMino = new Mino(9, 0)
  var currentMinoX = 0
  var currentMinoY = 0
  
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
  
  def checkHit(field: Field = this, mino: Mino = currentMino,
      minoPos :(Int, Int) = (currentMinoX, currentMinoY)): Boolean = {
    for(iy <- 0 until 5; ix <- 0 until 5) {
      var (mx, my) = minoPos
      if(!field(mx + ix, my + iy).transparent && !mino(ix, iy).transparent) return true
    }
    return false
  }

}