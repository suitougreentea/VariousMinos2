package io.github.suitougreentea.VariousMinos.rule

import io.github.suitougreentea.VariousMinos.Field

class RotationSystemVariantClassic extends RotationSystem {
  // TODO 
  override def rotateCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var (ox, oy) = field.currentMino.minoId match {
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

class RotationSystemVariantPlus extends RotationSystemVariantClassic {
  // TODO 
  override def rotateCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var (ox, oy) = field.currentMino.minoId match {
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