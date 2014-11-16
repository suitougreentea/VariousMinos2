package io.github.suitougreentea.VariousMinos

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import io.github.suitougreentea.VariousMinos.stagefile.BombContestStage
import org.newdawn.slick.Input
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import io.github.suitougreentea.VariousMinos.game.GameWrapper
import io.github.suitougreentea.VariousMinos.game.GameBomb
import io.github.suitougreentea.VariousMinos.rule.RuleStandard
import io.github.suitougreentea.VariousMinos.game.HandlerBombContest


trait Editor {
  def update(i: Input)
  def render(g: Graphics)
}

class EditorBombContest(var file: StageFileBombContest) extends Editor {
  var currentStage = file.stages(0)
  
  var cursor = 0
  var detailedEditor = false
  
  var testplay = false
  var wrapper: GameWrapper = _
  
  val _field = new TypeEditorField("Field", currentStage.field, 
      List(List.range(1, 36 + 1), List.range(69, 80 + 1), List(64)).flatten)
  val _mino_num = new TypeEditorInt("Number of Mino(s)", currentStage.mino_num)
  val _mino_set = new TypeEditorMinoSet("Available Mino Set", currentStage.mino_set)
  val _bomb_frequency = new TypeEditorInt("Bomb Frequency", currentStage.bomb_frequency)
  val _bomb_offset = new TypeEditorInt("Bomb Offset", currentStage.bomb_offset)
  val _allbomb_frequency = new TypeEditorInt("All Bomb Mino Frequency", currentStage.allbomb_frequency)
  val _allbomb_offset = new TypeEditorInt("All Bomb Mino Offset", currentStage.allbomb_offset)
  val _white_frequency = new TypeEditorInt("White Mino Frequency", currentStage.white_frequency)
  val _white_offset = new TypeEditorInt("White Mino Offset", currentStage.white_offset)
  val _white_level = new TypeEditorInt("White Mino Level", currentStage.white_level)
  val _black_frequency = new TypeEditorInt("Black Mino Frequency", currentStage.black_frequency)
  val _black_offset = new TypeEditorInt("Black Mino Offset", currentStage.black_offset)
  val _black_level = new TypeEditorInt("Black Mino Level", currentStage.black_level)
  val _gravity = new TypeEditorFloat("Gravity", currentStage.gravity)
  val _lock = new TypeEditorInt("Lockdown", currentStage.lock)
  
  val fieldList: Array[TypeEditor[_]] = Array(
    _field, _mino_num, _mino_set, _bomb_frequency, _bomb_offset, _allbomb_frequency, _allbomb_offset,
    _white_frequency, _white_offset, _white_level, _black_frequency, _black_level, _gravity, _lock
  )
  
  def make() = BombContestStage(
      _mino_num.value,
      _mino_set.value,
      _bomb_frequency.value,
      _bomb_offset.value,
      _allbomb_frequency.value,
      _allbomb_offset.value,
      _white_frequency.value,
      _white_offset.value,
      _white_level.value,
      _black_frequency.value,
      _black_offset.value,
      _black_level.value,
      _gravity.value,
      _lock.value,
      _field.value
  )
  
  def update(i: Input) {
    var current = if(cursor >= 0 && cursor < fieldList.size) fieldList(cursor) else null
    if(detailedEditor){
      if(current != null) current.updateDetailedEditor(i)
      if(i.isKeyPressed(Input.KEY_ESCAPE)) detailedEditor = false
    } else if(testplay) {
      wrapper.update()
    } else {
      if(current != null) current.update(i)
      if(i.isKeyPressed(Input.KEY_UP) && cursor >= 0) cursor -= 1
      if(i.isKeyPressed(Input.KEY_DOWN) && cursor < fieldList.size - 1) cursor += 1
      if(i.isKeyPressed(Input.KEY_ENTER)) {
        cursor match {
          case -1 => {
            wrapper = new GameWrapper(0, new Control(i)) {
              override def exit(code: Int){
                phase = 0
                testplay = false
              }
            }
            wrapper.game = new GameBomb(wrapper, new HandlerBombContest(make()), new RuleStandard())
            wrapper.phase = -1
            testplay = true
          }
          case _ => if(current.hasDetailedEditor){
            detailedEditor = true
          }
        }
      }
    }
  }
  def render(g: Graphics) {
    g.setBackground(Color.darkGray)
    g.clear()
    
    if(testplay) {
      g.translate(320, 0)
      wrapper.render(g)
    } else {
      Resource.boldfont.drawString("TEST PLAY", 16, 48,
          color = if(cursor == -1) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      for((e, i) <- fieldList.zipWithIndex){
        Resource.boldfont.drawString("%s: %s".format(e.displayName, e.valueString), 16, 64 + i * 16,
            color = if(cursor == i) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      }
      Resource.boldfont.drawString(">", 8, 64 + cursor * 16)
      
      g.pushTransform()
      g.translate(320, 0)
      Resource.frame.draw(152, 144)
      g.pushTransform()
      g.translate(168, 160)
      g.translate(0, 352)
      for(iy <- 0 until _field.value.size; ix <- 0 until 10) drawBlock(g)(_field.value(iy)(ix), ix * 16, -iy * 16 - 16)
      g.popTransform()
      g.popTransform()
      
      if(detailedEditor) fieldList(cursor).renderDetailedEditor(g)
    }
  }
  
  def drawBlock(g: Graphics)(block: Int, x: Int, y: Int, small: Boolean = false, transparency: Float = 1f) {
    var id = graphicId(block)
    var sbx: Int = id % 64
    var sby: Int = id / 64
    var sx = sbx * 16
    var sy = sby * 16
    var scale = if(small) 8 else 16
    Resource.block.draw(x, y, x + scale, y + scale, sx, sy, sx + 16, sy + 16, new Color(1f, 1f, 1f, transparency))
  }
  
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

trait TypeEditor[T] {
  val displayName: String
  
  val hasDetailedEditor: Boolean
  
  var value: T
  
  def save(){
  }
  
  def valueString = value.toString()
  
  def update(i: Input)
  
  def updateDetailedEditor(i: Input) {}
  def renderDetailedEditor(g: Graphics) {}
}

class TypeEditorInt(val displayName: String, var value: Int) extends TypeEditor[Int] {
  val hasDetailedEditor: Boolean = true

  def update(i: Input): Unit = {
    if(i.isKeyPressed(Input.KEY_LEFT)) value -= 1
    if(i.isKeyPressed(Input.KEY_RIGHT)) value += 1
  }
}
class TypeEditorFloat(val displayName: String, var value: Float) extends TypeEditor[Float] {
  val hasDetailedEditor: Boolean = true

  def update(i: Input): Unit = {
    if(i.isKeyPressed(Input.KEY_LEFT)) value -= 0.05f
    if(i.isKeyPressed(Input.KEY_RIGHT)) value += 0.05f
  }
}

class TypeEditorMinoSet(val displayName: String, var value: List[Int]) extends TypeEditor[List[Int]] {
  val hasDetailedEditor: Boolean = true
  override def valueString = "[EDIT]"

  def update(i: Input): Unit = {}
  override def updateDetailedEditor(i: Input) {
    
  }
  override def renderDetailedEditor(g: Graphics) {
    
  }
}

class TypeEditorField(val displayName: String, var value: List[Array[Int]], val availableBlocks: List[Int]) extends TypeEditor[List[Array[Int]]] {
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
      drawBlock(g)(e, (i % 10) * 16, (i / 10) * 16, false, if(blockCursor == i) 1f else 0.7f)
    }
    g.pushTransform()
    g.translate(320, 0)
    g.translate(168, 160)
    g.setColor(Color.white)
    g.drawRect(cursorX * 16, 336 - cursorY * 16 , 16, 16)
    g.popTransform()
  }
  
  def drawBlock(g: Graphics)(block: Int, x: Int, y: Int, small: Boolean = false, transparency: Float = 1f) {
    var id = graphicId(block)
    var sbx: Int = id % 64
    var sby: Int = id / 64
    var sx = sbx * 16
    var sy = sby * 16
    var scale = if(small) 8 else 16
    Resource.block.draw(x, y, x + scale, y + scale, sx, sy, sx + 16, sy + 16, new Color(1f, 1f, 1f, transparency))
  }
  
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