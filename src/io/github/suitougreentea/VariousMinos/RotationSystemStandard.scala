package io.github.suitougreentea.VariousMinos

class RotationSystemStandard extends RotationSystem {
  var offsetCW44 = Array((-1, 0), (0, 1), (1, 0), (0, -1))
  var offsetCCW44 = Array((0, 1), (1, 0), (0, -1), (-1, 0))
  
  override def rotateCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var (ox, oy) = field.currentMino.minoId match {
      case 1 | 3 | 4 | 7 | 14 | 15 | 16 | 17 | 25 | 26 => offsetCW44(state)
      case _ => (0, 0)
    }
    field.currentMino.rotateCW()
    if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy))){
      field.currentMino.rotateCCW()
      false
    } else {
      field.currentMinoX += ox
      field.currentMinoY += oy
      true
    }
  }

  override def rotateCCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var (ox, oy) = field.currentMino.minoId match {
      case 1 | 3 | 4 | 7 | 14 | 15 | 16 | 17 | 25 | 26  => offsetCCW44(state)
      case _ => (0, 0)
    }
    field.currentMino.rotateCCW()
    if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy))){
      field.currentMino.rotateCW()
      false
    } else {
      field.currentMinoX += ox
      field.currentMinoY += oy
      true
    }
  }
}