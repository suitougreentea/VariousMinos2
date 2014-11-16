package io.github.suitougreentea.VariousMinos.game

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
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonParser
import java.io.FileReader
import net.liftweb.json.DefaultFormats
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombPuzzle

class GameWrapper(val side: Int, val control: Control) {
  var game: Game = null

  def exit(code: Int) {
    game = null
    mode match {
      case 1 => {
        if(code == 1){
          phase = 3
        } else {
          phase = 0
        }
      }
      case 2 => {
        if(code == 1){
          phase = 3
        } else {
          phase = 0
        }
      }
      case _ => {
        phase = 0
      }
    }
  }
  
  var phase = 0
  var modeCursor = 0
  var mode = 0
  var ruleCursor = 0
  var rule = 0
  var difficultyCursor = 0
  var difficulty = 0
  var stageCursor = 0
  var stage = 0
  
  var bombContestStage: StageFileBombContest = _
  var bombPuzzleStage: StageFileBombPuzzle = _
  
  def update() {
    phase match {
      case -1 => game.update()
      case 0 => {
        if (control.pressed(Buttons.UP)) modeCursor -= 1
        if (control.pressed(Buttons.DOWN)) modeCursor += 1
        if (control.pressed(Buttons.LEFT)) difficultyCursor -= 1
        if (control.pressed(Buttons.RIGHT)) difficultyCursor += 1
        if (control.pressed(Buttons.A)) modeCursor match {
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
        if (control.pressed(Buttons.UP)) ruleCursor -= 1
        if (control.pressed(Buttons.DOWN)) ruleCursor += 1
        if (control.pressed(Buttons.A)) mode match {
          case 0 | 3 | 4 => {
            rule = ruleCursor
            startGame()
            phase = -1
          }
          case 1 => {
            rule = ruleCursor
            phase = 2
            implicit val formats = DefaultFormats
            bombContestStage = JsonParser.parse(new FileReader("stage/contest.json"), true).extract[StageFileBombContest]
            println(bombContestStage.toString())
          }
          case 2 => {
            rule = ruleCursor
            phase = 2
            implicit val formats = DefaultFormats
            bombPuzzleStage = JsonParser.parse(new FileReader("stage/puzzle.json"), true).extract[StageFileBombPuzzle]
          }
          case _ =>
        }
      }
      case 2 => {
        mode match {
          case 1 => {
            if (control.pressed(Buttons.LEFT)) stageCursor -= 1
            if (control.pressed(Buttons.RIGHT)) stageCursor += 1
            if (control.pressed(Buttons.A)) {
              stage = stageCursor
              startGame()
              phase = -1
            }
          }
          case 2 => {
            if (control.pressed(Buttons.LEFT)) stageCursor -= 1
            if (control.pressed(Buttons.RIGHT)) stageCursor += 1
            if (control.pressed(Buttons.A)) {
              stage = stageCursor
              startGame()
              phase = -1
            }
          }
        }
      }
      case 3 => {
        mode match {
          case 1 => {
            if (control.pressed(Buttons.A)) {             
              stage += 1
              if(stage < bombContestStage.stages.length) {
                startGame()
                phase = -1
              } else {
                phase = 0
              }
            }
          }
          case 2 => {
            if (control.pressed(Buttons.A)) {             
              stage += 1
              if(stage < bombPuzzleStage.stages.length) {
                startGame()
                phase = -1
              } else {
                phase = 0
              }
            }
          }
        }
      }
    }
  }

  def render(g: Graphics) {
    g.setBackground(Color.gray)
    g.clear()
    Resource.frame.draw(152, 144)
    g.pushTransform()
    g.translate(168, 160)
    g.setColor(new Color(0f, 0f, 0f, 0.5f))
    g.fillRect(0, 0, 160, 352)
    if (phase == -1) {
      g.popTransform()
      game.render(g)
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
          Resource.boldfont.drawString("Difficulty: " + difficultyCursor, 32, 256)
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
        case 2 => {
          Resource.boldfont.drawString("Select Stage", 80, 16, TextAlign.CENTER, new Color(0.8f, 0.2f, 0.8f))
          Resource.boldfont.drawString("Stage: " + stageCursor, 32, 64)
        }
        case 3 => {
          Resource.boldfont.drawString("Clear!", 80, 16, TextAlign.CENTER, new Color(0.8f, 1f, 0.2f))
        }
        case _ =>
      }
      g.popTransform()
      Resource.frame.draw(456, 144)
    }
  }

  def startGame() {
    var handler = mode match {
      case 0 => new HandlerBombEndless()
      case 1 => {
        new HandlerBombContest(bombContestStage.stages(stage))
      }
      case 2 => {
        new HandlerBombPuzzle(bombPuzzleStage.stages(stage))
      }
      case 4 => difficulty match {
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

    var ruleClass = rule match {
      case 0 => new RuleClassic()
      case 1 => new RuleClassicPlus()
      case 2 => new RuleStandard()
      case 3 => new RuleVariantClassic()
      case 4 => new RuleVariantPlus()
    }

    game = new GameBomb(this, handler, ruleClass)
  }
}