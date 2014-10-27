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
import org.newdawn.slick.Color
import org.newdawn.slick.Input
import io.github.suitougreentea.VariousMinos.Buttons
import io.github.suitougreentea.VariousMinos.game.Game
import io.github.suitougreentea.VariousMinos.rule.RuleClassic
import io.github.suitougreentea.VariousMinos.DefaultSetting

class StatePlaying(@BeanProperty val ID: Int) extends BasicGameState {
  val wrapper1p = new GameWrapper {
    var _control: Control = null
    def control = _control
    var game: Game = null
    def gameOver() {
      
    }
  }
  
  var cursor = 0
  var phase = 0
  var mode = 0
  var rule = 0
  
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    wrapper1p._control = new Control(gc.getInput())
  }
  
  override def enter(gc: GameContainer, sbg: StateBasedGame) = {
    
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    g.setBackground(Color.gray)
    g.clear()
    Resource.frame.draw(152, 144)
    g.pushTransform()
    g.translate(168, 160)
    g.setColor(new Color(0f, 0f, 0f, 0.5f))
    g.fillRect(0, 0, 160, 352)
    if(phase == -1){
      g.popTransform()
      wrapper1p.game.render(g)
    } else {
      phase match {
        case 0 => {
          Resource.boldfont.drawString(">", 16, 64 + cursor * 32, color = new Color(1f, 1f, 0f))
          Resource.boldfont.drawString("Select Mode", 80, 16, TextAlign.CENTER, new Color(1f, 0.2f, 0.8f))
          Resource.boldfont.drawString("Endless", 32, 64)
          Resource.boldfont.drawString("Contest", 32, 96)
          Resource.boldfont.drawString("Puzzle", 32, 128)
          Resource.boldfont.drawString("Master", 32, 160)
          Resource.boldfont.drawString("Survival", 32, 192)
        }
        case 1 => {
          Resource.boldfont.drawString(">", 16, 64 + cursor * 32, color = new Color(1f, 1f, 0f))
          Resource.boldfont.drawString("Select Rule", 80, 16, TextAlign.CENTER, new Color(0.2f, 1f, 0.2f))
          Resource.boldfont.drawString("ClassicPlus", 32, 64)
          Resource.boldfont.drawString("Standard", 32, 96)
        }
        case _ =>
      }
      g.popTransform()
      Resource.frame.draw(456, 144)
    }
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    if(phase == -1) {
      wrapper1p.game.update()
    } else {
      if(wrapper1p._control.pressed(Buttons.UP)) cursor -= 1
      if(wrapper1p._control.pressed(Buttons.DOWN)) cursor += 1
      if(wrapper1p._control.pressed(Buttons.A)) {
        phase match {
          case 0 => {
            cursor match {
              case 0 | 1 => {
                mode = cursor
                phase = 1
                cursor = 0
              }
              case _ =>
            }
          }
          case 1 =>
            cursor match {
              case 0 | 1 => {
                rule = cursor
                startGame(mode, rule)
                phase = -1
              }
              case _ =>
            }
          case _ =>
        }
      }
    }
    
    gc.getInput().clearKeyPressedRecord()
  }
  
  def startGame(mode: Int, rule: Int){
    wrapper1p.game = new GameBomb(wrapper1p, new DefaultSetting(new RuleClassic()))
  }
}