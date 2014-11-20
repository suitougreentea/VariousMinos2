package io.github.suitougreentea.VariousMinos

import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import io.github.suitougreentea.VariousMinos.stagefile.BombContestStage
import org.newdawn.slick.Input
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import io.github.suitougreentea.VariousMinos.game.GameWrapper
import io.github.suitougreentea.VariousMinos.game.GameBomb
import io.github.suitougreentea.VariousMinos.rule.RuleStandard
import io.github.suitougreentea.VariousMinos.game.HandlerBombContest
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonAST
import net.liftweb.json.Serialization
import net.liftweb.json.JsonAST
import net.liftweb.json.DefaultFormats
import io.github.suitougreentea.util.TextAlign


trait Editor {
  def update(i: Input)
  def render(g: Graphics)
}

class EditorBombContest(var file: StageFileBombContest) extends Editor with CommonRendererBomb {
  var currentStageId = 0
  var currentStage = file.stages(currentStageId)
  
  var cursor = 0
  var detailedEditor = false
  
  var testplay = false
  var wrapper: GameWrapper = _
  
  val _field = new TypeEditorField("Field", currentStage.field, 
      List(List.range(1, 36 + 1), List.range(69, 80 + 1), List(64)).flatten)
  val _mino_num = new TypeEditorInt("Number of Mino(s)", currentStage.mino_num, minValue = 1)
  val _mino_set = new TypeEditorMinoSet("Available Mino Set", currentStage.mino_set)
  val _bomb_frequency = new TypeEditorInt("Bomb Frequency", currentStage.bomb_frequency, minValue = 0)
  val _bomb_offset = new TypeEditorInt("Bomb Offset", currentStage.bomb_offset, minValue = 0)
  val _allbomb_frequency = new TypeEditorInt("All Bomb Mino Frequency", currentStage.allbomb_frequency, minValue = 0)
  val _allbomb_offset = new TypeEditorInt("All Bomb Mino Offset", currentStage.allbomb_offset, minValue = 0)
  val _white_frequency = new TypeEditorInt("White Mino Frequency", currentStage.white_frequency, minValue = 0)
  val _white_offset = new TypeEditorInt("White Mino Offset", currentStage.white_offset, minValue = 0)
  val _white_level = new TypeEditorInt("White Mino Level", currentStage.white_level, minValue = 0, maxValue = 5)
  val _black_frequency = new TypeEditorInt("Black Mino Frequency", currentStage.black_frequency, minValue = 0)
  val _black_offset = new TypeEditorInt("Black Mino Offset", currentStage.black_offset, minValue = 0)
  val _black_level = new TypeEditorInt("Black Mino Level", currentStage.black_level, minValue = 0, maxValue = 5)
  val _gravity = new TypeEditorInt("Gravity", currentStage.gravity, minValue = 0, maxValue = 1800)
  val _lock = new TypeEditorInt("Lockdown", currentStage.lock, minValue = 1)
  
  val fieldList: Array[TypeEditor[_]] = Array(
    _field, _mino_num, _mino_set, _bomb_frequency, _bomb_offset, _allbomb_frequency, _allbomb_offset,
    _white_frequency, _white_offset, _white_level, _black_frequency, _black_offset, _black_level, _gravity, _lock
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
      if(i.isKeyPressed(Input.KEY_UP) && cursor >= -2) cursor -= 1
      if(i.isKeyPressed(Input.KEY_DOWN) && cursor < fieldList.size - 1) cursor += 1
      if(i.isKeyPressed(Input.KEY_ENTER)) {
        cursor match {
          case -3 => {
            
          }
          case -2 => {
            
          }
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
      if(i.isKeyPressed(Input.KEY_F1)) {
        implicit val format =  DefaultFormats
        println(Serialization.write(make()))
      }
    }
  }
  def render(g: Graphics) {
    g.setBackground(Color.darkGray)
    g.clear()
    
    if(testplay) {
      g.translate(400, 0)
      wrapper.render(g)
    } else {
      Resource.boldfont.drawString("Editor", 400, 16, TextAlign.CENTER, new Color(1f, 0.2f, 0.8f))
      Resource.boldfont.drawString("Filename.***", 16, 16, TextAlign.LEFT, new Color(1f, 0.2f, 0.8f))
      Resource.boldfont.drawString("[BombContest]", 800 - 16, 16, TextAlign.RIGHT, new Color(1f, 0.2f, 0.8f))
      Resource.hr.draw(16, 32 + 2, 800 - 16, 32 + 6, 0, 0, 8, 4, new Color(1f, 0.2f, 0.8f))
      Resource.boldfont.drawString("Global Config", 32, 48,
          color = if(cursor == -3) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      Resource.boldfont.drawString(s"Stage: ${currentStageId + 1} / ${file.stages.size}", 32, 64,
          color = if(cursor == -2) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      Resource.boldfont.drawString("TEST PLAY", 32, 96,
          color = if(cursor == -1) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      for((e, i) <- fieldList.zipWithIndex){
        Resource.boldfont.drawString("%s: %s".format(e.displayName, e.valueString), 32, 128 + i * 16,
            color = if(cursor == i) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      }
      Resource.boldfont.drawString(">", 16, cursor match {
        case -3 => 48
        case -2 => 64
        case -1 => 96
        case _ => 128 + cursor * 16
      })
      
      g.pushTransform()
      g.translate(400, 0)
      Resource.frame.draw(152, 144)
      g.pushTransform()
      g.translate(168, 160)
      g.translate(0, 352)
      for(iy <- 0 until _field.value.size; ix <- 0 until 10) drawBlockByGraphicId(g)(graphicId(_field.value(iy)(ix)), ix * 16, -iy * 16 - 16)
      g.popTransform()
      g.popTransform()
      
      if(detailedEditor) fieldList(cursor).renderDetailedEditor(g)
    }
  }
}

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
    value = value.toList.sortWith((a, b) => a < b)
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