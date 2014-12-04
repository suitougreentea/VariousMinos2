package io.github.suitougreentea.VariousMinos.game

import org.newdawn.slick.GameContainer
import scala.beans.BeanProperty
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import io.github.suitougreentea.VariousMinos._
import org.newdawn.slick.Graphics
import io.github.suitougreentea.util.TextAlign
import org.newdawn.slick.Color
import org.newdawn.slick.Input
import io.github.suitougreentea.VariousMinos.rule.RuleClassic
import io.github.suitougreentea.VariousMinos.rule.RuleStandard
import io.github.suitougreentea.VariousMinos.rule.RuleClassicPlus
import io.github.suitougreentea.VariousMinos.rule.RuleVariantClassic
import io.github.suitougreentea.VariousMinos.rule.RuleVariantPlus
import scala.collection.mutable.HashSet
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombContest
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonParser
import java.io.FileReader
import net.liftweb.json.DefaultFormats
import io.github.suitougreentea.VariousMinos.stagefile.StageFileBombPuzzle

class GameWrapper(val side: Int, val data: PlayerData, val input: Input) {
  var game: Game = null
  var control: Control = new Control(input, data.key)

  def exit(code: Int) {
    //game = null
    mode match {
      case 1 => {
        if(code == 1){
          phase = 3
        } else {
          phase = 4
        }
      }
      case 2 => {
        if(code == 1){
          phase = 3
        } else {
          phase = 4
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

  var tryAgainCursor = 0
  
  var bombContestStage: StageFileBombContest = _
  var bombPuzzleStage: StageFileBombPuzzle = _

  val modeMenu: List[ModeMenuItem] = List(
    new ModeMenuItemWithDifficulty("Endless", new Color(1f, 1f, 1f)) {
      override val difficultyList: List[String] = List("Default")
      override def handler: HandlerBomb = new HandlerBombEndless()
    },
  // The handlers of Contest/Puzzle are defined in other place (startGame())
    new ModeMenuItemWithNothing("Contest", new Color(1f, 1f, 1f), null),
    new ModeMenuItemWithNothing("Puzzle", new Color(1f, 1f, 1f), null),
    new ModeMenuItemWithDifficulty("Master", new Color(0.4f, 0.4f, 0.4f)) {
      override val difficultyList: List[String] = List("Unimplemented")
      override def handler: HandlerBomb = ???
    },
    new ModeMenuItemWithDifficulty("Survival", new Color(1f, 1f, 1f)) {
      override val difficultyList: List[String] = List("Easy", "Normal", /*"Hard", "VeryHard",*/ "Insane", "FC-B1", "FC-W2", "FC-B2", "FC-W3")
      override def handler: HandlerBomb = cursor match {
        case 0 => new HandlerBombSurvivalEasy()
        case 1 => new HandlerBombSurvivalNormal()
        //case 2 => new HandlerBombSurvivalHard()
        //case 3 => new HandlerBombSurvivalVeryHard()
        case 2 => new HandlerBombSurvivalInsane()
        case 3 => new HandlerBombSurvivalFreezeChallenge(1, 0)
        case 4 => new HandlerBombSurvivalFreezeChallenge(0, 1)
        case 5 => new HandlerBombSurvivalFreezeChallenge(1, 1)
        case 6 => new HandlerBombSurvivalFreezeChallenge(0, 2)
      }
    }
  )
  
  def update() {
    phase match {
      case -1 => game.update()
      case 0 => {
        if (control.pressed(Buttons.UP) && modeCursor > 0) modeCursor -= 1
        if (control.pressed(Buttons.DOWN) && modeCursor < modeMenu.length - 1) modeCursor += 1
        if (control.pressed(Buttons.A)) modeCursor match {
          case 0 | 1 | 2 | 4 => {
            mode = modeCursor
            difficulty = difficultyCursor
            phase = 1
            modeCursor = 0
          }
          case _ =>
        }
        modeMenu(modeCursor).updateDetailedMenu(control)
      }
      case 1 => {
        if (control.pressed(Buttons.UP) && ruleCursor > 0) ruleCursor -= 1
        if (control.pressed(Buttons.DOWN) && ruleCursor < 3) ruleCursor += 1
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
      case 4 => {
        if (control.pressed(Buttons.DOWN) || control.pressed(Buttons.UP)) tryAgainCursor = (tryAgainCursor + 1) % 2
        if (control.pressed(Buttons.A)) {
          if(tryAgainCursor == 0) {
            startGame()
            phase = -1
          } else {
            phase = 0
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
    phase match {
      case -1 => {
        g.popTransform()
        game.render(g)
      }
      case 4 => {
        Resource.boldfont.drawString(">", 16, 64 + tryAgainCursor * 32, color = new Color(1f, 1f, 0f))
        Resource.boldfont.drawString("Try Again?", 80, 16, TextAlign.CENTER, new Color(1f, 0.2f, 0.8f))
        Resource.boldfont.drawString("Yes", 32, 64)
        Resource.boldfont.drawString("No", 32, 96)
      }
      case _ => {
        phase match {
          case 0 => {
            Resource.boldfont.drawString(">", 16, 64 + modeCursor * 32, color = new Color(1f, 1f, 0f))
            Resource.boldfont.drawString("Select Mode", 80, 16, TextAlign.CENTER, new Color(1f, 0.2f, 0.8f))
            for((e, i) <- modeMenu.zipWithIndex) {
              Resource.boldfont.drawString(e.name, 32, 64 + i * 32 + (if(modeCursor < i) modeMenu(modeCursor).height else 0))
              if(i == modeCursor) {
                g.pushTransform()
                g.translate(0, 64 + i * 32 + 16)
                e.renderDetailedMenu(g)
                g.popTransform()
              }
            }
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
  }

  def startGame() {
    var handler = mode match {
      case 1 => new HandlerBombContest(stage, bombContestStage.stages(stage))
      case 2 => new HandlerBombPuzzle(stage, bombPuzzleStage.stages(stage))
      case _ => modeMenu(mode).handler
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
