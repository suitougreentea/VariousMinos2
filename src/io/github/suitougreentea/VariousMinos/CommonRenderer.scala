package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.lwjgl.opengl.GL11

trait CommonRenderer {
  def graphicId(block: Block): Int = block.id
  //def graphicId(block: Block): Int
  
  def drawBlockByGraphicId(g: Graphics)(id: Int, x: Int, y: Int, small: Boolean = false, transparency: Float = 1f) {
    var sbx: Int = id % 64
    var sby: Int = id / 64
    var sx = sbx * 16
    var sy = sby * 16
    var scale = if(small) 8 else 16
    Resource.block.draw(x, y, x + scale, y + scale, sx, sy, sx + 16, sy + 16, new Color(1f, 1f, 1f, transparency))
  }
  
  def drawBlock(g: Graphics)(block: Block, x: Int, y: Int, small: Boolean = false, transparency: Float = 1f) {
    if(block == null) return
    var id = graphicId(block)
    drawBlockByGraphicId(g)(id, x, y, small, transparency)
  }
  
  def drawBlockBrighten(g: Graphics)(block: Block, x: Int, y: Int, small: Boolean = false, brightness: Float = 0f) {
    if(block == null) return
    var id = graphicId(block)
    var sbx: Int = id % 64
    var sby: Int = id / 64
    var sx = sbx * 16
    var sy = sby * 16
    var scale = if(small) 8 else 16
    Resource.block.bind();
    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
    Resource.block.draw(x, y, x + scale, y + scale, sx, sy, sx + 16, sy + 16, new Color(brightness, brightness, brightness))
    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
  }
  
  def drawField(g: Graphics)(field: Field) {
    for(iy <- 0 until field.height; ix <- 0 until 10) drawBlock(g)(field(ix, iy), ix * 16, -iy * 16 - 16)
  }
  
  def drawFieldMino(g: Graphics)(field: Field, brightness: Float = 0f) {
    var mino = field.currentMino
    var mx = field.currentMinoX
    var my = field.currentMinoY
    g.pushTransform()
    g.translate(mx * 16, -my * 16)
    drawMinoBrighten(g)(mino, brightness = brightness)
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
  
  def drawMinoBrighten(g: Graphics)(mino: Mino, small: Boolean = false, brightness: Float = 0f) {
    var scale = if(small) 8 else 16
    for(iy <- 0 until 5; ix <- 0 until 5) drawBlockBrighten(g)(mino(ix, iy), ix * scale, -iy * scale - scale, small, brightness)
  }

  def drawFallingPiece(g: Graphics)(piece: FallingPiece) {
    for(iy <- piece.y until piece.y + piece.height; ix <- 0 until 10) drawBlock(g)(piece(ix, iy), ix * 16, -iy * 16 - 16)
  }
  
  // center the position (rect: w5h3)
  def drawNextMino(g: Graphics)(mino: Mino, small: Boolean = false, transparency: Float = 1f) {
    var scale = if(small) 8 else 16
    var ((x, y), (w, h)) = MinoList.rectangle(mino.minoId, mino.rotationState)
    g.pushTransform()
    g.translate(((5 - w) / 2f - x) * scale, -((3 - h) / 2f - y) * scale)
    drawMino(g)(mino, small, transparency)
    g.popTransform()
  }
}

trait CommonRendererBomb extends CommonRenderer {
  def graphicId(id: Int): Int = {
    if(0 <= id && id < 64) id
    else if(id == 64) 128
    else if(id == 65) 129
    else if(id == 66) 130
    else if(id == 67) 193
    else if(id == 68) 194
    else if(69 <= id && id <= 86) (id - 69) + 133
    else id
  }
}