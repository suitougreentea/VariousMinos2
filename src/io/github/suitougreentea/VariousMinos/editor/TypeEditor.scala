package io.github.suitougreentea.VariousMinos.editor

import org.newdawn.slick.Input
import org.newdawn.slick.Graphics
import io.github.suitougreentea.VariousMinos.CommonRenderer
import io.github.suitougreentea.VariousMinos.CommonRendererBomb
import io.github.suitougreentea.VariousMinos.Block
import io.github.suitougreentea.VariousMinos.Mino
import io.github.suitougreentea.VariousMinos.MinoList
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.stagefile.BombPuzzleMinoList
import org.newdawn.slick.Color

trait TypeEditor[T] {
  val displayName: String
  
  val hasDetailedEditor: Boolean
  
  var value: T
  
  def valueString = value.toString()
  
  def update(i: Input)
  
  def updateDetailedEditor(i: Input) {}
  def renderDetailedEditor(g: Graphics) {}
}

class TypeEditorInt(val displayName: String, var value: Int, var minValue: Int = Int.MinValue, var maxValue: Int = Int.MaxValue) extends TypeEditor[Int] {
  val hasDetailedEditor: Boolean = true

  def update(i: Input): Unit = {
    var delta = 1
    if(i.isKeyDown(Input.KEY_LSHIFT)) delta *= 10
    if(i.isKeyDown(Input.KEY_LCONTROL)) delta *= 100
    if(i.isKeyPressed(Input.KEY_LEFT)) value -= delta
    if(i.isKeyPressed(Input.KEY_RIGHT)) value += delta
    if(value < minValue) value = minValue
    if(value > maxValue) value = maxValue
  }
}
class TypeEditorFloat(val displayName: String, var value: Float) extends TypeEditor[Float] {
  val hasDetailedEditor: Boolean = true

  def update(i: Input): Unit = {
    if(i.isKeyPressed(Input.KEY_LEFT)) value -= 0.05f
    if(i.isKeyPressed(Input.KEY_RIGHT)) value += 0.05f
  }
}

class TypeEditorMinoSet(val displayName: String, var value: List[Int]) extends TypeEditor[List[Int]] with CommonRenderer with CommonRendererBomb {
  val hasDetailedEditor: Boolean = true

  val minoList = Array.tabulate(29)(i => new Mino(i, 0, new Block(69)))
  
  var cursor = 0
  
  var set: Set[Int] = value.toSet
  updateList()
  
  def updateList() {
    value = set.toList.sortWith((a, b) => a < b)
  }
  
  override def valueString = value.mkString(",")
  
  def update(i: Input): Unit = {}
  override def updateDetailedEditor(i: Input) {
    if(i.isKeyPressed(Input.KEY_UP)) cursor -= 1
    if(i.isKeyPressed(Input.KEY_DOWN)) cursor += 1
    if(i.isKeyPressed(Input.KEY_LEFT)) cursor -= 18
    if(i.isKeyPressed(Input.KEY_RIGHT)) cursor += 18
    
    if(cursor > 28) cursor = 28
    if(cursor < 0) cursor = 0
    
    if(i.isKeyPressed(Input.KEY_SPACE)) {
      if(set.contains(cursor)) set = set - cursor else set = set + cursor
      updateList() 
    }
  }
  override def renderDetailedEditor(g: Graphics) {
    g.pushTransform()
    g.translate(400, 24)
    for((e, i) <- minoList.zipWithIndex) {
      drawNextMino(g)(e, true, if(set.contains(i)) 1f else 0.5f)
      if(i == 17) g.translate(48, -32 * 17) else g.translate(0, 32)
    }
    g.popTransform()
    
    g.pushTransform()
    g.translate(400, 0)
    g.setColor(Color.white)
    g.drawRect((cursor / 18) * 48, (cursor % 18) * 32, 40, 24)
    g.popTransform()
  }
}

class TypeEditorField(val displayName: String, var value: List[Array[Int]], val availableBlocks: List[Int]) extends TypeEditor[List[Array[Int]]] with CommonRendererBomb {
  val hasDetailedEditor: Boolean = true
  override def valueString = "[EDIT]"

  def update(i: Input): Unit = {}
  
  var blockCursor = 0
  var cursorX, cursorY = 0
  
  override def updateDetailedEditor(i: Input) {
    if(i.isKeyPressed(Input.KEY_W)) blockCursor -= 10
    if(i.isKeyPressed(Input.KEY_S)) blockCursor += 10
    if(i.isKeyPressed(Input.KEY_A)) blockCursor -= 1
    if(i.isKeyPressed(Input.KEY_D)) blockCursor += 1
    
    if(i.isKeyPressed(Input.KEY_UP) && cursorY < 21) cursorY += 1
    if(i.isKeyPressed(Input.KEY_DOWN) && cursorY >= 1) cursorY -= 1
    if(i.isKeyPressed(Input.KEY_LEFT) && cursorX >= 1) cursorX -= 1
    if(i.isKeyPressed(Input.KEY_RIGHT) && cursorX < 9) cursorX += 1
    
    if(i.isKeyDown(Input.KEY_SPACE)) value(cursorY)(cursorX) = availableBlocks(blockCursor)
    if(i.isKeyDown(Input.KEY_LSHIFT)) value(cursorY)(cursorX) = 0
  }
  override def renderDetailedEditor(g: Graphics) {
    for((e, i) <- availableBlocks.zipWithIndex){
      drawBlockByGraphicId(g)(graphicId(e), 384 + (i % 10) * 16, 160 + (i / 10) * 16)
    }
    g.setColor(Color.white)
    g.drawRect(384 + (blockCursor % 10) * 16, 160 + (blockCursor / 10) * 16 , 16, 16)
    
    g.pushTransform()
    g.translate(400, 0)
    g.translate(168, 160)
    g.drawRect(cursorX * 16, 336 - cursorY * 16 , 16, 16)
    g.popTransform()
  }
}

class TypeEditorMinoList(val displayName: String, var value: Array[BombPuzzleMinoList]) extends TypeEditor[Array[BombPuzzleMinoList]] with CommonRendererBomb {
  val hasDetailedEditor: Boolean = true
  override def valueString = "[EDIT]"

  def update(i: Input): Unit = {}
  
  var cursorX, cursorY = 0
  
  override def updateDetailedEditor(i: Input) {
    if(i.isKeyPressed(Input.KEY_DOWN) && cursorY < value.length - 1) cursorY += 1
    if(i.isKeyPressed(Input.KEY_UP) && cursorY >= 1) cursorY -= 1
    if(i.isKeyPressed(Input.KEY_LEFT) && cursorX >= 1) cursorX -= 1
    if(i.isKeyPressed(Input.KEY_RIGHT) && cursorX < 2) cursorX += 1
    if(i.isKeyPressed(Input.KEY_Z)){
      cursorX match {
        case 0 => if(value(cursorY).id >= 1) value(cursorY).id -= 1
        case 1 => if(value(cursorY).`type` >= 1) value(cursorY).`type` -= 1
        case 2 => if(value(cursorY).bomb >= 0) value(cursorY).bomb -= 1
      }
    }
    if(i.isKeyPressed(Input.KEY_X)){
      cursorX match {
        case 0 => if(value(cursorY).id <= 27) value(cursorY).id += 1
        case 1 => if(value(cursorY).`type` <= 11) value(cursorY).`type` += 1
        case 2 => if(value(cursorY).bomb <= 3) value(cursorY).bomb += 1
      }
    }
    if(i.isKeyPressed(Input.KEY_N)){
      value = value :+ new BombPuzzleMinoList(0, 0, 0)
    }
    if(i.isKeyPressed(Input.KEY_I)){
    }
    if(i.isKeyPressed(Input.KEY_D)){
      
    }
  }
  override def renderDetailedEditor(g: Graphics) {
    g.pushTransform()
    g.translate(400, 32)
    for(e <- value) {
      Resource.boldfont.drawString(e.id.toString(), 0, 0)
      Resource.boldfont.drawString(e.`type`.toString(), 24, 0)
      Resource.boldfont.drawString(e.bomb.toString(), 48, 0)
      g.translate(72, 0)
      var num = MinoList.numBlocks(e.id)
      var array = e.`type` match {
        case 0 => Array.fill(num)(new Block(1))
        case 1 => Array.fill(num)(new Block(64))
        case 2 | 3 | 4 | 5 | 6 | 7 => Array.fill(num)(new Block(69 + e.`type` - 2))
        case 8 | 9 | 10 | 11 | 12 | 13 => Array.fill(num)(new Block(75 + e.`type` - 8))
      }
      if(e.bomb >= 0) array(e.bomb) = new Block(64)
      var mino = new Mino(e.id, 0, array)
      drawNextMino(g)(mino, true, 1f)
      g.translate(-72, 32)
    }
    g.popTransform()
    Resource.boldfont.drawString(">" ,400 + cursorX * 24 - 8, 32 + cursorY * 32)
  }
}