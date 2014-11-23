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
import java.io.FileWriter
import java.io.File


trait Editor {
  def update(i: Input)
  def render(g: Graphics)
}

class EditorBombContest(var file: File, var stage: StageFileBombContest) extends Editor with CommonRendererBomb {
  var currentStageId = 0
  var currentStage: BombContestStage = _
  
  var cursor = 0
  var detailedEditor = false
  
  var testplay = false
  var wrapper: GameWrapper = _
  
  var stageMenu = false
  var stageMenuCursor = 0
  
  var _field: TypeEditorField = _
  var _mino_num: TypeEditorInt = _
  var _mino_set: TypeEditorMinoSet = _
  var _bomb_frequency: TypeEditorInt = _
  var _bomb_offset: TypeEditorInt = _
  var _allbomb_frequency: TypeEditorInt = _
  var _allbomb_offset: TypeEditorInt = _
  var _white_frequency: TypeEditorInt = _
  var _white_offset: TypeEditorInt = _
  var _white_level: TypeEditorInt = _
  var _black_frequency: TypeEditorInt = _
  var _black_offset: TypeEditorInt = _
  var _black_level: TypeEditorInt = _
  var _gravity: TypeEditorInt = _
  var _lock: TypeEditorInt = _
  
  var fieldList: Array[TypeEditor[_]] = _
  
  loadStage()
  
  def loadStage() {
    currentStage = stage.stages(currentStageId)
    _field = new TypeEditorField("Field", currentStage.field, 
    List(List.range(1, 36 + 1), List.range(69, 80 + 1), List(64)).flatten)
    _mino_num = new TypeEditorInt("Number of Mino(s)", currentStage.mino_num, minValue = 1)
    _mino_set = new TypeEditorMinoSet("Available Mino Set", currentStage.mino_set)
    _bomb_frequency = new TypeEditorInt("Bomb Frequency", currentStage.bomb_frequency, minValue = 0)
    _bomb_offset = new TypeEditorInt("Bomb Offset", currentStage.bomb_offset, minValue = 0)
    _allbomb_frequency = new TypeEditorInt("All Bomb Mino Frequency", currentStage.allbomb_frequency, minValue = 0)
    _allbomb_offset = new TypeEditorInt("All Bomb Mino Offset", currentStage.allbomb_offset, minValue = 0)
    _white_frequency = new TypeEditorInt("White Mino Frequency", currentStage.white_frequency, minValue = 0)
    _white_offset = new TypeEditorInt("White Mino Offset", currentStage.white_offset, minValue = 0)
    _white_level = new TypeEditorInt("White Mino Level", currentStage.white_level, minValue = 0, maxValue = 5)
    _black_frequency = new TypeEditorInt("Black Mino Frequency", currentStage.black_frequency, minValue = 0)
    _black_offset = new TypeEditorInt("Black Mino Offset", currentStage.black_offset, minValue = 0)
    _black_level = new TypeEditorInt("Black Mino Level", currentStage.black_level, minValue = 0, maxValue = 5)
    _gravity = new TypeEditorInt("Gravity", currentStage.gravity, minValue = 0, maxValue = 1800)
    _lock = new TypeEditorInt("Lockdown", currentStage.lock, minValue = 1)
    fieldList = Array(
      _field, _mino_num, _mino_set, _bomb_frequency, _bomb_offset, _allbomb_frequency, _allbomb_offset,
      _white_frequency, _white_offset, _white_level, _black_frequency, _black_offset, _black_level, _gravity, _lock
    )
  }
  
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
  
  def empty() = BombContestStage(100, List(4, 5, 6, 7, 8, 9, 10), 1, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 60, List.fill(22)(Array.fill(10)(0)))
  
  def clone(from: BombContestStage) = {
    var newField = List.fill(22)(Array.fill(10)(0))
    for(iy <- 0 until from.field.size; ix <- 0 until 10) newField(iy)(ix) = from.field(iy)(ix)
    from.copy(mino_set = from.mino_set.toList, field = newField)
  }
  
  def update(i: Input) {
    var current = if(cursor >= 0 && cursor < fieldList.size) fieldList(cursor) else null
    if(detailedEditor){
      if(current != null) current.updateDetailedEditor(i)
      if(i.isKeyPressed(Input.KEY_ESCAPE)) detailedEditor = false
    } else if(testplay) {
      wrapper.update()
    } else if(stageMenu) {
      if(i.isKeyPressed(Input.KEY_UP) && stageMenuCursor >= 1) {
        stageMenuCursor -= 1
        stage.stages(currentStageId) = make()
        currentStageId = stageMenuCursor
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_DOWN) && stageMenuCursor < stage.stages.size - 1) {
        stageMenuCursor += 1
        stage.stages(currentStageId) = make()
        currentStageId = stageMenuCursor
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_ENTER)) {
        stage.stages(currentStageId) = make()
        currentStageId = stageMenuCursor
        loadStage()
        stageMenu = false
      }
      if(i.isKeyPressed(Input.KEY_A)) {
        stage.stages = stage.stages :+ empty()
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_I)) {
        // New
        var result = stage.stages :+ null
        for(i <- result.size - 2 to stageMenuCursor by -1){
          result(i + 1) = result(i)
        }
        result(stageMenuCursor) = empty()
        stage.stages = result
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_C)) {
        // Copy
        var result = stage.stages :+ null
        for(i <- result.size - 2 to stageMenuCursor by -1){
          result(i + 1) = result(i)
        }
        result(stageMenuCursor) = clone(result(stageMenuCursor + 1))
        stage.stages = result
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_D)) {
        // Delete
        var result = stage.stages.toArray
        if(stageMenuCursor < result.size - 1){
          for(i <- stageMenuCursor to result.size - 2 by -1){
            result(i) = result(i + 1)
          }
        }
        result = result.init
        stage.stages = result
        if(stageMenuCursor >= result.size) stageMenuCursor = result.size - 1
        if(currentStageId >= stage.stages.size) currentStageId = stage.stages.size - 1
        loadStage()
      }
    } else {
      if(current != null) current.update(i)
      if(cursor == -2) {
        if(i.isKeyPressed(Input.KEY_LEFT) && currentStageId >= 1) {
          stage.stages(currentStageId) = make()
          currentStageId -= 1
          loadStage()
        }
        if(i.isKeyPressed(Input.KEY_RIGHT) && currentStageId < stage.stages.size - 1) {
          stage.stages(currentStageId) = make()
          currentStageId += 1
          loadStage()
        }
      }
      if(i.isKeyPressed(Input.KEY_UP) && cursor >= -2) cursor -= 1
      if(i.isKeyPressed(Input.KEY_DOWN) && cursor < fieldList.size - 1) cursor += 1
      if(i.isKeyPressed(Input.KEY_ENTER)) {
        cursor match {
          case -3 => {
            
          }
          case -2 => {
            stage.stages(currentStageId) = make()
            stageMenuCursor = currentStageId
            stageMenu = true
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
        stage.stages(currentStageId) = make()
        val output = Serialization.write(stage)
        
        val writer = new FileWriter(file, false);
        writer.write(output);
        writer.close();
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
      Resource.boldfont.drawString(file.getName(), 16, 16, TextAlign.LEFT, new Color(1f, 0.2f, 0.8f))
      Resource.boldfont.drawString("[BombContest]", 800 - 16, 16, TextAlign.RIGHT, new Color(1f, 0.2f, 0.8f))
      Resource.hr.draw(16, 32 + 2, 800 - 16, 32 + 6, 0, 0, 8, 4, new Color(1f, 0.2f, 0.8f))
      Resource.boldfont.drawString("Global Config", 32, 48,
          color = if(cursor == -3) new Color(1f, 1f, 1f) else new Color(0.8f, 0.8f, 0.8f))
      Resource.boldfont.drawString(s"Stage: ${currentStageId + 1} / ${stage.stages.size}", 32, 64,
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
      Resource.hr.draw(16, 600 - 32 - 6, 800 - 16, 600 - 32 - 2, 0, 0, 8, 4, new Color(1f, 1f, 1f))
      Resource.boldfont.drawString("F1 - Save", 16, 600 - 32, TextAlign.LEFT, new Color(1f, 1f, 1f))
      
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
      if(stageMenu) {
        g.setColor(new Color(0f, 0f, 0f, 0.8f))
        g.fillRect(0, 0, 800, 600)
        Resource.boldfont.drawString(s"Stage: ${stageMenuCursor + 1} / ${stage.stages.size}", 64, 64)
        Resource.hr.draw(16, 600 - 32 - 6, 800 - 16, 600 - 32 - 2, 0, 0, 8, 4, new Color(1f, 1f, 1f))
        Resource.boldfont.drawString("A - Add, I - Insert, C - Copy, D - Delete", 16, 600 - 32, TextAlign.LEFT, new Color(1f, 1f, 1f))
      }
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