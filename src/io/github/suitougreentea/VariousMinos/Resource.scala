package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.Image

object Resource {
  var frame,
  design,
  block: Image = _

  def load(){
    frame = loadImage("image/frame.png")
    design = loadImage("image/design.png")
    block = loadImage("image/block.png")
  }
  
  def loadImage(path: String): Image = {
    new Image("res/" + path) 
  }

}