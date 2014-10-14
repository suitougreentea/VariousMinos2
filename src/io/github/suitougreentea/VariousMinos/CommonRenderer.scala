package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.Color
import org.newdawn.slick.Graphics

trait CommonRenderer {
  def drawBlock(g: Graphics)(block: Block, x: Int, y: Int, small: Boolean = false, transparency: Float = 1f) {
    if(block == null) return
    var sbx: Int = block.id % 64
    var sby: Int = block.id / 64
    if (small) {
      
    } else {
      var sx = sbx * 16
      var sy = sby * 16
      Resource.block.draw(x, y, x + 16, y + 16, sx, sy, sx + 16, sy + 16, new Color(1f, 1f, 1f, transparency))
    }
  }
  
  def drawField(g: Graphics)(field: Field) {
    for(iy <- 0 until field.height; ix <- 0 until 10) drawBlock(g)(field(ix, iy), ix * 16, -iy * 16 - 16)
  }
  
  def drawFieldMino(g: Graphics)(field: Field) {
    var mino = field.currentMino
    var mx = field.currentMinoX
    var my = field.currentMinoY
    g.pushTransform()
    g.translate(mx * 16, -my * 16)
    drawMino(g)(mino)
    g.popTransform()
  }
  
  def drawFieldMinoGhost(g: Graphics)(field: Field) {
    var mino = field.currentMino
    var mx = field.currentMinoX
    var my = field.ghostY
    g.pushTransform()
    g.translate(mx * 16, -my * 16)
    drawMino(g)(mino, 0.5f)
    g.popTransform()
  }

  def drawMino(g: Graphics)(mino: Mino, transparency: Float = 1f) {
    for(iy <- 0 until 5; ix <- 0 until 5) drawBlock(g)(mino(ix, iy), ix * 16, -iy * 16 - 16, false, transparency)
  }
}