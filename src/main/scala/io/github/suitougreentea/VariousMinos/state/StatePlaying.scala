package io.github.suitougreentea.VariousMinos.state

import scala.beans.BeanProperty

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame

import io.github.suitougreentea.VariousMinos.{PlayerData, Control}
import io.github.suitougreentea.VariousMinos.game.GameWrapper

import java.io.File
import java.io.FileReader
import net.liftweb.json.{JsonParser, DefaultFormats}

class StatePlaying(@BeanProperty val ID: Int) extends BasicGameState {
  var wrapper1p: GameWrapper = _
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    val file = new File("save/player/00000000-0000-0000-0000-000000000000.json")
    val reader = new FileReader(file)
    implicit val formats = DefaultFormats
    val json = JsonParser.parse(reader, false)
    val user = json.extract[PlayerData]

    wrapper1p = new GameWrapper(0, user, gc.getInput())
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    wrapper1p.render(g)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    wrapper1p.update()
    
    gc.getInput().clearKeyPressedRecord()
  }
}
