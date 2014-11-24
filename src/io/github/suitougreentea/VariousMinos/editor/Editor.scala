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
import io.github.suitougreentea.VariousMinos.Control
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.CommonRendererBomb
import io.github.suitougreentea.VariousMinos.stagefile.StageFile
import io.github.suitougreentea.VariousMinos.stagefile.Stage
import scala.reflect.ClassTag
import io.github.suitougreentea.VariousMinos.game.Game

abstract class Editor[T <: Stage](file: File, stage: StageFile[T])(implicit ct: ClassTag[T]) {
  var currentStageId = 0
  var currentStage: T = _
  
  var cursor = 0
  var detailedEditor = false
  
  var testplay = false
  var wrapper: GameWrapper = _
  
  var stageMenu = false
  var stageMenuCursor = 0
  
  loadStage()
  
  var fieldList: Array[TypeEditor[_]]
  
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
        // TODO: asInstanceOf?
        var result: Array[T] = stage.stages :+ null.asInstanceOf[T]
        for(i <- result.size - 2 to stageMenuCursor by -1){
          result(i + 1) = result(i)
        }
        result(stageMenuCursor) = empty()
        stage.stages = result
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_C)) {
        // Copy
        var result: Array[T] = stage.stages :+ null.asInstanceOf[T]
        for(i <- result.size - 2 to stageMenuCursor by -1){
          result(i + 1) = result(i)
        }
        result(stageMenuCursor) = clone(result(stageMenuCursor + 1))
        stage.stages = result
        loadStage()
      }
      if(i.isKeyPressed(Input.KEY_D) && stage.stages.length > 1) {
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
            wrapper.game = testPlayGame()
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
      
      childRender(g)
      
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
  
  def childRender(g: Graphics)
  def loadStage()
  def make(): T
  def empty(): T
  def clone(from: T): T
  def testPlayGame(): Game
}