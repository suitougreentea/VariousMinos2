package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.Color
import org.newdawn.slick.Graphics

trait CommonRenderer {
  def graphicId(block: Block): Int = block.id
  
  def drawBlock(g: Graphics)(block: Block, x: Int, y: Int, small: Boolean = false, transparency: Float = 1f) {
    if(block == null) return
    var id = graphicId(block)
    var sbx: Int = id % 64
    var sby: Int = id / 64
    var sx = sbx * 16
    var sy = sby * 16
    var scale = if(small) 8 else 16
    Resource.block.draw(x, y, x + scale, y + scale, sx, sy, sx + 16, sy + 16, new Color(1f, 1f, 1f, transparency))
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
    drawMino(g)(mino, false, 0.5f)
    g.popTransform()
  }

  def drawMino(g: Graphics)(mino: Mino, small: Boolean = false, transparency: Float = 1f) {
    var scale = if(small) 8 else 16
    for(iy <- 0 until 5; ix <- 0 until 5) drawBlock(g)(mino(ix, iy), ix * scale, -iy * scale - scale, small, transparency)
  }

  def drawFallingPiece(g: Graphics)(piece: FallingPiece) {
    for(iy <- piece.y until piece.y + piece.height; ix <- 0 until 10) drawBlock(g)(piece(ix, iy), ix * 16, -iy * 16 - 16)
  }
  
  // center the position (rect: w5h3)
  def drawNextMino(g: Graphics)(mino: Mino, small: Boolean = false) {
    var scale = if(small) 8 else 16
    var ((x, y), (w, h)) = MinoList.rectangle(mino.minoId, mino.rotationState)
    g.pushTransform()
    g.translate(((5 - w) / 2f - x) * scale, -((3 - h) / 2f - y) * scale)
    drawMino(g)(mino, small)
    g.popTransform()
  }
}