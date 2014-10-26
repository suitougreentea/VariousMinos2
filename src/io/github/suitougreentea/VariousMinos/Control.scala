package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.Input
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class Control(val input: Input) {
  val keyConfig: HashMap[Buttons.Value, HashSet[Int]] = HashMap.empty
  keyConfig(Buttons.UP) = HashSet(Input.KEY_UP)
  keyConfig(Buttons.DOWN) = HashSet(Input.KEY_DOWN)
  keyConfig(Buttons.LEFT) = HashSet(Input.KEY_LEFT)
  keyConfig(Buttons.RIGHT) = HashSet(Input.KEY_RIGHT)
  keyConfig(Buttons.A) = HashSet(Input.KEY_Z)
  keyConfig(Buttons.B) = HashSet(Input.KEY_X)
  keyConfig(Buttons.C) = HashSet(Input.KEY_C)
  
  
  def pressed(button: Buttons.Value): Boolean = {
    for(e <- keyConfig(button)){
      if(input.isKeyPressed(e)) return true
    }
    return false
  }
  
  def down(button: Buttons.Value): Boolean = {
    for(e <- keyConfig(button)){
      if(input.isKeyDown(e)) return true
    }
    return false
  }
}

object Buttons extends Enumeration {
  val UP, DOWN, LEFT, RIGHT, A, B, C, D, E, F = Value
}