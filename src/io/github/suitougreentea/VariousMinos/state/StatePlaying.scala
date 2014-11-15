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
import io.github.suitougreentea.VariousMinos.StageLoader
import io.github.suitougreentea.VariousMinos.game.HandlerBombContest
import io.github.suitougreentea.VariousMinos.rule.RuleStandard
import io.github.suitougreentea.VariousMinos.game.HandlerBombEndless
import io.github.suitougreentea.VariousMinos.rule.RuleClassicPlus
import io.github.suitougreentea.VariousMinos.rule.RuleVariantClassic
import io.github.suitougreentea.VariousMinos.rule.RuleVariantPlus
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombInfinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombInfinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombFinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombFinite
import scala.collection.mutable.HashSet
import io.github.suitougreentea.VariousMinos.game.HandlerBombSurvivalEasy
import io.github.suitougreentea.VariousMinos.game.HandlerBombPuzzle
import io.github.suitougreentea.VariousMinos.game.HandlerBombSurvivalNormal
import io.github.suitougreentea.VariousMinos.game.HandlerBombSurvivalHard
import io.github.suitougreentea.VariousMinos.game.HandlerBombSurvivalInsane
import io.github.suitougreentea.VariousMinos.game.HandlerBombSurvivalFreezeChallenge
import io.github.suitougreentea.VariousMinos.game.HandlerBombSurvivalVeryHard

class StatePlaying(@BeanProperty val ID: Int) extends BasicGameState {
  val wrapper1p = new GameWrapper {
    var _control: Control = null
    def control = _control
    val side = 0
    var game: Game = null
    def exit(code: Int) {
      phase = 0
      game = null
    }
  }
  
  var phase = 0
  var modeCursor = 0
  var mode = 0
  var ruleCursor = 0
  var rule = 0
  var difficultyCursor = 0
  var difficulty = 0
  
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
          Resource.boldfont.drawString(">", 16, 64 + modeCursor * 32, color = new Color(1f, 1f, 0f))
          Resource.boldfont.drawString("Select Mode", 80, 16, TextAlign.CENTER, new Color(1f, 0.2f, 0.8f))
          Resource.boldfont.drawString("Endless", 32, 64)
          Resource.boldfont.drawString("Contest", 32, 96)
          Resource.boldfont.drawString("Puzzle", 32, 128)
          Resource.boldfont.drawString("Master", 32, 160, color = new Color(0.3f, 0.3f, 0.3f))
          Resource.boldfont.drawString("Survival", 32, 192)
          Resource.boldfont.drawString("Difficulty: " + difficultyCursor, 0, 256)
        }
        case 1 => {
          Resource.boldfont.drawString(">", 16, 64 + ruleCursor * 32, color = new Color(1f, 1f, 0f))
          Resource.boldfont.drawString("Select Rule", 80, 16, TextAlign.CENTER, new Color(0.2f, 1f, 0.2f))
          Resource.boldfont.drawString("Classic", 32, 64)
          Resource.boldfont.drawString("ClassicPlus", 32, 96)
          Resource.boldfont.drawString("Standard", 32, 128)
          Resource.boldfont.drawString("VariantClassic", 32, 160)
          Resource.boldfont.drawString("VariantPlus", 32, 192)
        }
        case _ =>
      }
      g.popTransform()
      Resource.frame.draw(456, 144)
    }
  }

  def update(gc: GameContainer, sbg: StateBasedGame, d: Int) = {
    phase match {
      case -1 => wrapper1p.game.update()
      case 0 => {
        if(wrapper1p._control.pressed(Buttons.UP)) modeCursor -= 1
        if(wrapper1p._control.pressed(Buttons.DOWN)) modeCursor += 1
        if(wrapper1p._control.pressed(Buttons.LEFT)) difficultyCursor -= 1
        if(wrapper1p._control.pressed(Buttons.RIGHT)) difficultyCursor += 1
        if(wrapper1p._control.pressed(Buttons.A)) modeCursor match {
          case 0 | 1 | 2 | 4 => {
            mode = modeCursor
            difficulty = difficultyCursor
            phase = 1
            modeCursor = 0
          }
          case _ =>
        }
      }
      case 1 => {
        if(wrapper1p._control.pressed(Buttons.UP)) ruleCursor -= 1
        if(wrapper1p._control.pressed(Buttons.DOWN)) ruleCursor += 1
        if(wrapper1p._control.pressed(Buttons.A)) modeCursor match {
          case 0 | 1 | 2 | 3 | 4 => {
            rule = ruleCursor
            startGame(mode, difficulty, rule)
            phase = -1
          }
          case _ =>
        }
      }
    }
    
    gc.getInput().clearKeyPressedRecord()
  }
  
  def startGame(mode: Int, difficulty: Int, rule: Int){
    var handler = mode match {
      case 0 => new HandlerBombEndless()
      case 1 => {
        var loader = new StageLoader("stage/contest.vms")
        loader.load()
        new HandlerBombContest(loader(0))
      }
      case 2 => {
        var loader = new StageLoader("stage/puzzle.vms")
        loader.load()
        new HandlerBombPuzzle(loader(0))
      }
      case 4 => {
        difficulty match {
          case 0 => new HandlerBombSurvivalEasy()
          case 1 => new HandlerBombSurvivalNormal()
          case 2 => new HandlerBombSurvivalHard()
          case 3 => new HandlerBombSurvivalVeryHard()
          case 4 => new HandlerBombSurvivalInsane()
          case 5 => new HandlerBombSurvivalFreezeChallenge(1, 0)
          case 6 => new HandlerBombSurvivalFreezeChallenge(0, 1)
          case 7 => new HandlerBombSurvivalFreezeChallenge(1, 1)
          case 8 => new HandlerBombSurvivalFreezeChallenge(0, 2)
        }
      }
    }
    
    var ruleClass = rule match {
      case 0 => new RuleClassic()
      case 1 => new RuleClassicPlus()
      case 2 => new RuleStandard()
      case 3 => new RuleVariantClassic()
      case 4 => new RuleVariantPlus()
    }
    
    wrapper1p.game = new GameBomb(wrapper1p, handler, ruleClass)
  }
}