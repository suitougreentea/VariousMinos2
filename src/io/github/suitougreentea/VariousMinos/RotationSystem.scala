package io.github.suitougreentea.VariousMinos

trait RotationSystem {
  def rotateCW(field: Field): Boolean = {
    field.currentMino.rotateCW()
    if(field.checkHit()) {
      field.currentMino.rotateCCW()
      false
    } else {
      true
    }
  }
  def rotateCCW(field: Field): Boolean = {
    field.currentMino.rotateCW()
    if(field.checkHit()) {
      field.currentMino.rotateCCW()
      false
    } else {
      true
    }
  }
}