package io.github.suitougreentea.VariousMinos.state

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import scala.beans.BeanProperty
import org.newdawn.slick.Graphics
import javax.swing.JFileChooser
import org.newdawn.slick.Input
import java.io.FileReader
import net.liftweb.json.JsonParser
import java.io.File
import io.github.suitougreentea.VariousMinos.{PlayerData, Resource, GameStageEditor}
import io.github.suitougreentea.VariousMinos.editor.Editor
import io.github.suitougreentea.VariousMinos.editor.EditorBombContest
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization
import io.github.suitougreentea.VariousMinos.editor.EditorBombPuzzle
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombPuzzle
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JString

class StateEditorMenu(@BeanProperty val ID: Int) extends BasicGameState {
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    Resource.load()
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    Resource.boldfont.drawString("Open...", 48, 32)
    Resource.boldfont.drawString(">", 32, 32)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int): Unit = {
    val i = gc.getInput()
    if(i.isKeyPressed(Input.KEY_ENTER)) {
      val chooser = new JFileChooser()
      chooser.setCurrentDirectory(new File(".").getAbsoluteFile().getParentFile())
      chooser.showOpenDialog(null)
      
      val file = chooser.getSelectedFile()
      val reader = new FileReader(file)
      
      implicit val formats = DefaultFormats
      val json = JsonParser.parse(reader, false)

      val playerFile = new File("save/player/00000000-0000-0000-0000-000000000000.json")
      val playerReader = new FileReader(playerFile)
      val playerJson = JsonParser.parse(playerReader, false)
      
      sbg.asInstanceOf[GameStageEditor].editor = (json \ "type").asInstanceOf[JString].s match {
        case "BombContest" => new EditorBombContest(file, json.extract[StageFileBombContest], playerJson.extract[PlayerData])
        case "BombPuzzle" => new EditorBombPuzzle(file, json.extract[StageFileBombPuzzle], playerJson.extract[PlayerData])
      }
      
      reader.close()
      sbg.enterState(1)
    }
    i.clearKeyPressedRecord()
  }
}