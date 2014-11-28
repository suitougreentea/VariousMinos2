package io.github.suitougreentea.util

import java.io.File
import scala.xml.XML
import org.newdawn.slick.Image
import scala.collection.mutable.HashMap
import org.newdawn.slick.Color
import scala.xml.Elem

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
  
  def measureString(str: String): Int = {
    var cx = 0
    for(c <- str){
      cx += measureChar(c) 
    }
    cx
  }
  
  def measureChar(c: Char): Int = {
    val glyphopt = chars.get(c)
    if(!glyphopt.isEmpty){
      glyphopt.get.xadvance
    } else 0
  }
  
  def drawChar(c: Char, x: Int, y: Int, color: Color = new Color(1f, 1f, 1f)): Int = {
    val glyphopt = chars.get(c)
    if(!glyphopt.isEmpty){
      val glyph = glyphopt.get
      glyph.page.draw(x + glyph.xoffset,
          y + glyph.yoffset + lineHeight - base,
          x + glyph.xoffset + glyph.width,
          y + glyph.yoffset + lineHeight - base + glyph.height,
          glyph.x,
          glyph.y,
          glyph.x + glyph.width,
          glyph.y + glyph.height,
          color)
      glyph.xadvance
    } else {
      0
    }
  }
  
  def drawStringEmbedded(str: String, x: Int, y: Int, color: Color = new Color(1f, 1f, 1f)) {
    var cx = 0
    for(c <- str){
      cx += drawChar(c, x + cx, y, color)
    }
  }
  
  def drawString(str: String, x: Int, y: Int, align: TextAlign.Value = TextAlign.LEFT, color: Color = new Color(1f, 1f, 1f)) {
    var cy = 0
    for(s <- str.split("\n")){
      align match {
        case TextAlign.LEFT => drawStringEmbedded(s, x, y + cy, color)
        case TextAlign.CENTER => drawStringEmbedded(s, x - (measureString(s) / 2), y + cy, color)
        case TextAlign.RIGHT => drawStringEmbedded(s, x - measureString(s), y + cy, color)
      }
      cy += lineHeight
    }
  }
  
  def measureStringFormatted(str: String): Int = {
    var cx = 0
    var i = 0
    while(i < str.length){
      var c = str(i)
      if(c == '@'){
        str(i + 1) match {
          case '#' => {
            if(str(i + 8) == '#') {
              i += 9
            } else {
              i += 11
            }
          }
          case '@' => {
            cx += measureChar('@')
            i += 2
          }
        }
      } else {
        cx += measureChar(c)
        i += 1
      }
    }
    cx
  }
  
  def drawStringFormattedEmbedded(str: String, x: Int, y: Int, startColor: Color) : Color = {
    var cx = 0
    var cc = startColor
    var i = 0
    while(i < str.length()){
      var c = str(i)
      if(c == '@'){
        str(i + 1) match {
          case '#' => {
            if(str(i + 8) == '#') {
              cc = new Color(
                  Integer.parseInt(str.substring(i + 2, i + 4), 16) / 255f, 
                  Integer.parseInt(str.substring(i + 4, i + 6), 16) / 255f, 
                  Integer.parseInt(str.substring(i + 6, i + 8), 16) / 255f)
              i += 9
            } else {
              cc = new Color(
                  Integer.parseInt(str.substring(i + 2, i + 4), 16) / 255f, 
                  Integer.parseInt(str.substring(i + 4, i + 6), 16) / 255f, 
                  Integer.parseInt(str.substring(i + 6, i + 8), 16) / 255f, 
                  Integer.parseInt(str.substring(i + 8, i + 10), 16) / 255f)
              i += 11
            }
          }
          case '@' => cx += drawChar('@', x + cx, y, cc)
          i += 2
        }
      } else {
        cx += drawChar(c, x + cx, y, cc)
        i += 1
      }
    }
    cc
  }
  
  def drawStringFormatted(str: String, x: Int, y: Int, align: TextAlign.Value = TextAlign.LEFT) {
    var cy = 0
    var lastColor = new Color(1f, 1f, 1f)
    for(s <- str.split("\n")){
      align match {
        case TextAlign.LEFT => lastColor = drawStringFormattedEmbedded(s, x, y + cy, lastColor)
        case TextAlign.CENTER => lastColor = drawStringFormattedEmbedded(s, x - (measureStringFormatted(s) / 2), y + cy, lastColor)
        case TextAlign.RIGHT => lastColor = drawStringFormattedEmbedded(s, x - measureStringFormatted(s), y + cy, lastColor)
      }
      cy += lineHeight
    }
  }
}

class Glyph(val x: Int, val y: Int, val width: Int, val height: Int, val xoffset: Int, val yoffset: Int, val xadvance: Int, val page: Image){

}

object TextAlign extends Enumeration {
  val LEFT, CENTER, RIGHT = Value
}
