package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.Input
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class Control(val input: Input, val config: KeyConfig) {
  val keyConfig: HashMap[Buttons.Value, Set[Int]] = HashMap.empty
  keyConfig(Buttons.UP) = config.up.toSet
  keyConfig(Buttons.DOWN) = config.down.toSet
  keyConfig(Buttons.LEFT) = config.left.toSet
  keyConfig(Buttons.RIGHT) = config.right.toSet
  keyConfig(Buttons.A) = config.a.toSet
  keyConfig(Buttons.B) = config.b.toSet
  keyConfig(Buttons.C) = config.c.toSet
  
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
