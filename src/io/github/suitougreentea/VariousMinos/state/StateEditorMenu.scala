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
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.GameStageEditor
import io.github.suitougreentea.VariousMinos.editor.Editor
import io.github.suitougreentea.VariousMinos.editor.EditorBombContest
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization
import io.github.suitougreentea.VariousMinos.editor.EditorBombPuzzle
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombPuzzle

class StateEditorMenu(@BeanProperty val ID: Int) extends BasicGameState {
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    Resource.load()
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    Resource.boldfont.drawString("Press Enter", 0, 0)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int): Unit = {
      implicit val formats = DefaultFormats
      //val json = JsonParser.parse(new FileReader("stage/contest.json"), false)
      //sbg.asInstanceOf[GameStageEditor].editor = new EditorBombContest(new File("stage/contest.json"), json.extract[StageFileBombContest])
      val json = JsonParser.parse(new FileReader("stage/puzzle.json"), false)
      sbg.asInstanceOf[GameStageEditor].editor = new EditorBombPuzzle(new File("stage/puzzle.json"), json.extract[StageFileBombPuzzle])
      sbg.enterState(1)
    
    
    val i = gc.getInput()
    if(i.isKeyPressed(Input.KEY_ENTER)) {
      val chooser = new JFileChooser()
      chooser.setCurrentDirectory(new File(".").getAbsoluteFile().getParentFile())
      chooser.showOpenDialog(null)
      val reader = new FileReader(chooser.getSelectedFile())
      implicit val formats = DefaultFormats
      val json = JsonParser.parse(reader, false)
      sbg.asInstanceOf[GameStageEditor].editor = new EditorBombContest(chooser.getSelectedFile(), json.extract[StageFileBombContest])
      reader.close()
      sbg.enterState(1)
    }
    i.clearKeyPressedRecord()
  }
}