package io.github.suitougreentea.VariousMinos.rule

import io.github.suitougreentea.VariousMinos.Field

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