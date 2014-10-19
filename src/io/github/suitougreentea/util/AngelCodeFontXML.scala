package io.github.suitougreentea.util

import java.io.File
import scala.xml.XML
import org.newdawn.slick.Image
import scala.collection.mutable.HashMap
import org.newdawn.slick.Color

class AngelCodeFontXML (fntPath: String){
  val fntFile = new File(fntPath)
  val fnt = XML.loadFile(fntFile.getPath())
  
  val pages: Array[Image] = new Array((fnt \ "common" \ "@pages").text.toInt)
  val chars: HashMap[Int, Glyph] = HashMap.empty
  
  val lineHeight = (fnt \ "common" \ "@lineHeight").text.toInt
  val base = (fnt \ "common" \ "@base").text.toInt
  
  for(e <- fnt \ "pages" \ "page") {
    val file = new File(fntFile.getParent(), (e \ "@file").text)
    pages((e \ "@id").text.toInt) = new Image(file.getPath())
  }
  
  for(e <- fnt \ "chars" \ "char") {
    var id = (e \ "@id").text.toInt
    
    var x = (e \ "@x").text.toInt
    var y = (e \ "@y").text.toInt
    var width = (e \ "@width").text.toInt
    var height = (e \ "@height").text.toInt
    var xoffset = (e \ "@xoffset").text.toInt
    var yoffset = (e \ "@yoffset").text.toInt
    var xadvance = (e \ "@xadvance").text.toInt
    var page = pages((e \ "@page").text.toInt)
    
    chars += id -> new Glyph(x, y, width, height, xoffset, yoffset, xadvance, page)
  }
  
  def drawStringEmbedded(str: String, x: Int, y: Int, color: Color = new Color(1f, 1f, 1f)): Int = {
    var cx = 0
    for(c <- str){
      val glyph = chars.getOrElse(c, null)
      if(c != null){
        glyph.page.draw(x + cx + glyph.xoffset,
            y + glyph.yoffset + lineHeight - base,
            x + cx + glyph.xoffset + glyph.width,
            y + glyph.yoffset + lineHeight - base + glyph.height,
            glyph.x,
            glyph.y,
            glyph.x + glyph.width,
            glyph.y + glyph.height,
            color)
      }
      cx += glyph.xadvance
    }
    cx
  }
  
  def drawString(str: String, x: Int, y: Int, color: Color = new Color(1f, 1f, 1f)) {
    var cy = 0
    for(s <- str.split("\n")){
      drawStringEmbedded(s, x, y + cy, color)
      cy += lineHeight
    }
  }
}

class Glyph(val x: Int, val y: Int, val width: Int, val height: Int, val xoffset: Int, val yoffset: Int, val xadvance: Int, val page: Image){

}