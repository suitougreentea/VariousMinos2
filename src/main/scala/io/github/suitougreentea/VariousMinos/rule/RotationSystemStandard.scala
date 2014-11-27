package io.github.suitougreentea.VariousMinos.rule

import io.github.suitougreentea.VariousMinos.Field

class RotationSystemStandard extends RotationSystemClassic {
  // 0->R, R->X, X->L, L->0
  val superOffset3x3CW = Array(
	  Array(( 0, 0),(-1, 0),(-1, 1),( 0,-2),(-1,-2)),
	  Array(( 0, 0),( 1, 0),( 1,-1),( 0, 2),( 1, 2)),
	  Array(( 0, 0),( 1, 0),( 1, 1),( 0,-2),( 1,-2)),
	  Array(( 0, 0),(-1, 0),(-1,-1),( 0, 2),(-1, 2))
  )
  val superOffset4x4CW = Array(
	  Array(( 0, 0),(-2, 0),( 1, 0),(-2,-1),( 1, 2)),
	  Array(( 0, 0),(-1, 0),( 2, 0),(-1, 2),( 2,-1)),
	  Array(( 0, 0),( 2, 0),(-1, 0),( 2, 1),(-1,-2)),
	  Array(( 0, 0),( 1, 0),(-2, 0),( 1,-2),(-2, 1))
  )
  
  // 0->L, R->0, X->R, L->X
  val superOffset3x3CCW = Array(
	  Array(( 0, 0),( 1, 0),( 1, 1),( 0,-2),( 1,-2)),
	  Array(( 0, 0),( 1, 0),( 1,-1),( 0, 2),( 1, 2)),
	  Array(( 0, 0),(-1, 0),(-1, 1),( 0,-2),(-1,-2)),
	  Array(( 0, 0),(-1, 0),(-1,-1),( 0, 2),(-1, 2))
  )
  val superOffset4x4CCW = Array(
	  Array(( 0, 0),(-1, 0),( 2, 0),(-1, 2),( 2,-1)),
	  Array(( 0, 0),( 2, 0),(-1, 0),( 2, 1),(-1,-2)),
	  Array(( 0, 0),( 1, 0),(-2, 0),( 1,-2),(-2, 1)),
	  Array(( 0, 0),(-2, 0),( 1, 0),(-2,-1),( 1, 2))
  )
  
  val superOffsetNotImplemented = Array(
	  Array(( 0, 0),( 0, 0),( 0, 0),( 0, 0),( 0, 0)),
	  Array(( 0, 0),( 0, 0),( 0, 0),( 0, 0),( 0, 0)),
	  Array(( 0, 0),( 0, 0),( 0, 0),( 0, 0),( 0, 0)),
	  Array(( 0, 0),( 0, 0),( 0, 0),( 0, 0),( 0, 0))
  )
  // TODO: More minos 
  override def rotateCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var (ox, oy) = field.currentMino.minoId match {
      case 1 | 3 | 4 | 7 | 14 | 15 | 16 | 17 | 25 | 26 => offsetCW44(state)
      case _ => (0, 0)
    }
    field.currentMino.rotateCW()
    for(i <- 0 until 5){
      var (sx, sy) = field.currentMino.minoId match {
        case 4 => superOffset4x4CW(state)(i)
        case 5 | 6 | 7 | 8 | 9 | 10 => superOffset3x3CW(state)(i)
        case _ => superOffsetNotImplemented(state)(i)
      }
      if(!field.checkHit(minoPos = (field.currentMinoX + ox + sx, field.currentMinoY + oy + sy))){
        field.currentMinoX += ox + sx
        field.currentMinoY += oy + sy
        return true
      }
    }
    field.currentMino.rotateCCW()
    return false
  }

  override def rotateCCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var (ox, oy) = field.currentMino.minoId match {
      case 1 | 3 | 4 | 7 | 14 | 15 | 16 | 17 | 25 | 26 => offsetCCW44(state)
      case _ => (0, 0)
    }
    field.currentMino.rotateCCW()
    for(i <- 0 until 5){
      var (sx, sy) = field.currentMino.minoId match {
        case 4 => superOffset4x4CCW(state)(i)
        case 5 | 6 | 7 | 8 | 9 | 10 => superOffset3x3CCW(state)(i)
        case _ => superOffsetNotImplemented(state)(i)
      }
      if(!field.checkHit(minoPos = (field.currentMinoX + ox + sx, field.currentMinoY + oy + sy))){
        field.currentMinoX += ox + sx
        field.currentMinoY += oy + sy
        return true
      }
    }
    field.currentMino.rotateCW()
    return false
  }
}