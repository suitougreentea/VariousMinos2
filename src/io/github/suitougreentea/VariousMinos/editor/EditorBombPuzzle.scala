package io.github.suitougreentea.VariousMinos.editor

import io.github.suitougreentea.VariousMinos.stagefile.BombPuzzleStage
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import java.io.File
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombPuzzle
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.game.GameBomb
import io.github.suitougreentea.VariousMinos.game.Game
import io.github.suitougreentea.VariousMinos.game.HandlerBombPuzzle
import io.github.suitougreentea.VariousMinos.rule.RuleStandard
import io.github.suitougreentea.VariousMinos.CommonRendererBomb

class EditorBombPuzzle(var file: File, var stage: StageFileBombPuzzle) extends Editor[BombPuzzleStage](file, stage) with CommonRendererBomb {
  var _mino_list: TypeEditorMinoList = _
  var _gravity: TypeEditorInt = _
  var _lock: TypeEditorInt = _
  var _field: TypeEditorField = _
  
  def clone(from: BombPuzzleStage): BombPuzzleStage = {
    var newField = List.fill(22)(Array.fill(10)(0))
    for(iy <- 0 until from.field.size; ix <- 0 until 10) newField(iy)(ix) = from.field(iy)(ix)
    from.copy(field = newField)
  }

  def loadStage() {
    currentStage = stage.stages(currentStageId)
    _field = new TypeEditorField("Field", currentStage.field, 
      List(List.range(1, 36 + 1), List.range(69, 80 + 1), List(64)).flatten)
    _mino_list = new TypeEditorMinoList("Mino Order", currentStage.mino_list)
    _gravity = new TypeEditorInt("Gravity", currentStage.gravity, minValue = 0, maxValue = 1800)
    _lock = new TypeEditorInt("Lockdown", currentStage.lock, minValue = 1)
    fieldList = Array(_field, _mino_list, _gravity, _lock)
  }

  def empty(): BombPuzzleStage = BombPuzzleStage(Array.empty, 1, 60, List.fill(22)(Array.fill(10)(0)))

  def make(): BombPuzzleStage = BombPuzzleStage(
      _mino_list.value,
      _gravity.value,
      _lock.value,
      _field.value
      )

  var fieldList: Array[TypeEditor[_]] = _

  def childRender(g: Graphics): Unit = {
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

  def testPlayGame(): Game = new GameBomb(wrapper, new HandlerBombPuzzle(make()), new RuleStandard())
}