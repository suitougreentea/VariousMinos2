package io.github.suitougreentea.VariousMinos.game

import org.newdawn.slick.Graphics
import io.github.suitougreentea.VariousMinos.Control

trait Game {
  def update()
  def render(g: Graphics)
}

trait GameWrapper {
  def control: Control
  def game: Game
  def exit(code: Int)
}