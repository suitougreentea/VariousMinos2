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
import io.github.suitougreentea.VariousMinos.Editor
import io.github.suitougreentea.VariousMinos.GameStageEditor
import io.github.suitougreentea.VariousMinos.EditorBombContest
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import net.liftweb.json.DefaultFormats

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
      val json = JsonParser.parse(new FileReader("stage/contest.json"), false)
      sbg.asInstanceOf[GameStageEditor].editor = new EditorBombContest(json.extract[StageFileBombContest])
      sbg.enterState(1)
    
    
    val i = gc.getInput()
    if(i.isKeyPressed(Input.KEY_ENTER)) {
      val chooser = new JFileChooser()
      chooser.setCurrentDirectory(new File(".").getAbsoluteFile().getParentFile())
      chooser.showOpenDialog(null)
      implicit val formats = DefaultFormats
      val json = JsonParser.parse(new FileReader(chooser.getSelectedFile()), false)
      sbg.asInstanceOf[GameStageEditor].editor = new EditorBombContest(json.extract[StageFileBombContest])
      sbg.enterState(1)
    }
    i.clearKeyPressedRecord()
  }
}