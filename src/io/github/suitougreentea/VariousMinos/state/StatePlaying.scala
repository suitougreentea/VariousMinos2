package io.github.suitougreentea.VariousMinos.state

import org.newdawn.slick.GameContainer
import scala.beans.BeanProperty
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import io.github.suitougreentea.VariousMinos.Resource
import org.newdawn.slick.Graphics
import io.github.suitougreentea.util.TextAlign
import io.github.suitougreentea.VariousMinos.game.GameWrapper
import io.github.suitougreentea.VariousMinos.Control
import io.github.suitougreentea.VariousMinos.game.GameBomb

class StatePlaying(@BeanProperty val ID: Int) extends BasicGameState {
  val wrapper1p = new GameWrapper {
    var _control: Control = null
    def control = _control
    val game = new GameBomb(this)
    def gameOver() {
      
    }
  }
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    wrapper1p._control = new Control(gc.getInput())
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    Resource.frame.draw(152, 144)
    g.pushTransform()
    g.translate(168, 160)
    Resource.boldfont.drawString("Select Style", 80, 16, TextAlign.CENTER)
    Resource.boldfont.drawString("Tetra", 16, 48)
    Resource.boldfont.drawString("Bomb", 16, 64)
    Resource.boldfont.drawString("Magic", 16, 80)
    Resource.boldfont.drawString("Spark", 16, 96)
    Resource.jpfont.drawString("爆破が備わったモードです", 0, 128)
    g.popTransform()
    Resource.frame.draw(456, 144)
    wrapper1p.game.render(g)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    wrapper1p.game.update()
  }
}