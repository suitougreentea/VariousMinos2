package io.github.suitougreentea.VariousMinos.rule

import io.github.suitougreentea.VariousMinos.Field
import io.github.suitougreentea.VariousMinos.Mino

class RotationSystemVariantPlus extends RotationSystemVariantClassic {
  var lastMino: Mino = null
  var alreadyBonus = false
  
    override def rotateCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var id = field.currentMino.minoId
    var (ox, oy) = id match {
      case 1 => offsetCW21(state)  //w2h1
      case 4 => offsetCW41(state)  //w4h1
      case 3 | 7 => offsetCW22(state)  //w2h2
      case 5 | 6 | 9 | 18 | 19 | 21 => offsetCW32(state)  //w3h2
      case 8 => offsetCWS(state)
      case 10 => offsetCWZ(state)
      case 14 | 15 | 16 | 17 | 25 | 26 => offsetCW42(state)  //w4h2
      case 0 | 2 | 13 | 11 | 12 | 20 | 22 | 23 | 24 | 27 | 28 => (0, 0)  //w3h1, w5h1, w3h3
    }
    field.currentMino.rotateCW()
    if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy))){
      id match {
        case 2 | 4 | 13 => {
          if(field.currentMino == lastMino && alreadyBonus){
            field.currentMino.rotateCCW()
            false
          } else {
            var o = if(id == 2) 1 else 2
            if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy + o))){
              field.currentMino.rotateCCW()
              false
            } else {
              field.currentMinoX += ox
              field.currentMinoY += oy + o
              lastMino = field.currentMino
              alreadyBonus = true
              true
            }
          }
        }
        case _ => {
          if(field.checkHit(minoPos = (field.currentMinoX + ox + 1, field.currentMinoY + oy))){
            if(field.checkHit(minoPos = (field.currentMinoX + ox - 1, field.currentMinoY + oy))){
              if(id != 9 || (field.currentMino == lastMino && alreadyBonus)) { //TODO
                field.currentMino.rotateCCW()
                false
              } else {
                if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy + 1))){
                  field.currentMino.rotateCCW()
                  false
                } else {
                  field.currentMinoX += ox
                  field.currentMinoY += oy + 1
                  lastMino = field.currentMino
                  alreadyBonus = true
                  true
                }
              }
            } else {
              field.currentMinoX += ox - 1
              field.currentMinoY += oy
              true
            }
          } else {
            field.currentMinoX += ox + 1
            field.currentMinoY += oy
            true
          }
        }
      }
    } else {
      field.currentMinoX += ox
      field.currentMinoY += oy
      true
    }
  }

  override def rotateCCW(field: Field): Boolean = {
    var state = field.currentMino.rotationState
    var id = field.currentMino.minoId
    var (ox, oy) = id match {
      case 1 => offsetCCW21(state)  //w2h1
      case 4 => offsetCCW41(state)  //w4h1
      case 3 | 7 => offsetCCW22(state)  //w2h2
      case 5 | 6 | 9 | 18 | 19 | 21 => offsetCCW32(state)  //w3h2
      case 8 => offsetCCWS(state)
      case 10 => offsetCCWZ(state)
      case 14 | 15 | 16 | 17 | 25 | 26 => offsetCCW42(state)  //w4h2
      case 0 | 2 | 13 | 11 | 12 | 20 | 22 | 23 | 24 | 27 | 28 => (0, 0)  //w3h1, w5h1, w3h3
    }
    field.currentMino.rotateCCW()
    if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy))){
      id match {
        case 2 | 4 | 13 => {
          if(field.currentMino == lastMino && alreadyBonus){
            field.currentMino.rotateCW()
            false
          } else {
            var o = if(id == 2) 1 else 2
            if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy + o))){
              field.currentMino.rotateCW()
              false
            } else {
              field.currentMinoX += ox
              field.currentMinoY += oy + o
              lastMino = field.currentMino
              alreadyBonus = true
              true
            }
          }
        }
        case _ => {
          if(field.checkHit(minoPos = (field.currentMinoX + ox + 1, field.currentMinoY + oy))){
            if(field.checkHit(minoPos = (field.currentMinoX + ox - 1, field.currentMinoY + oy))){
              if(id != 9 || (field.currentMino == lastMino && alreadyBonus)) { //TODO
                field.currentMino.rotateCW()
                false
              } else {
                if(field.checkHit(minoPos = (field.currentMinoX + ox, field.currentMinoY + oy + 1))){
                  field.currentMino.rotateCW()
                  false
                } else {
                  field.currentMinoX += ox
                  field.currentMinoY += oy + 1
                  lastMino = field.currentMino
                  alreadyBonus = true
                  true
                }
              }
            } else {
              field.currentMinoX += ox - 1
              field.currentMinoY += oy
              true
            }
          } else {
            field.currentMinoX += ox + 1
            field.currentMinoY += oy
            true
          }
        }
      }
    } else {
      field.currentMinoX += ox
      field.currentMinoY += oy
      true
    }
  }
}