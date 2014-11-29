package io.github.suitougreentea.VariousMinos.editor

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
import io.github.suitougreentea.VariousMinos.{PlayerData, Control, Resource, CommonRendererBomb}


class EditorBombContest(var file: File, var stage: StageFileBombContest, val player: PlayerData) extends Editor[BombContestStage](file, stage, player) with CommonRendererBomb {
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
  
  def empty() = BombContestStage(100, List(4, 5, 6, 7, 8, 9, 10), 1, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 60, Array.fill(22)(Array.fill(10)(0)))
  
  def clone(from: BombContestStage) = {
    var newField = Array.fill(22)(Array.fill(10)(0))
    for(iy <- 0 until from.field.size; ix <- 0 until 10) newField(iy)(ix) = from.field(iy)(ix)
    from.copy(mino_set = from.mino_set.toList, field = newField)
  }
  
  def testPlayGame = new GameBomb(wrapper, new HandlerBombContest(-1, make()), new RuleStandard())
  
  def childRender(g: Graphics) {
      g.pushTransform()
      g.translate(400, 0)
      Resource.frame.draw(152, 144)
      g.pushTransform()
      g.translate(168, 160)
      g.translate(0, 352)
      for(iy <- 0 until _field.value.size; ix <- 0 until 10) drawBlockByGraphicId(g)(graphicId(_field.value(iy)(ix)), ix * 16, -iy * 16 - 16)
      g.popTransform()
      g.popTransform()
  }
}
