package io.github.suitougreentea.VariousMinos.game

import org.newdawn.slick.Graphics
import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.VariousMinos.Block
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombFinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombFinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorBombInfinite
import io.github.suitougreentea.VariousMinos.MinoGeneratorConfigBombInfinite
import scala.collection.mutable.ArraySeq
import scala.collection.mutable.HashSet
import io.github.suitougreentea.util.TextAlign
import io.github.suitougreentea.VariousMinos.Phase
import io.github.suitougreentea.VariousMinos.stagefile.BombContestStage
import io.github.suitougreentea.VariousMinos.stagefile.BombPuzzleStage

trait HandlerBomb {  
  def init(game: GameBomb)
  def start(game: GameBomb) {}
  def update(game: GameBomb) {}
  def render(game: GameBomb, g: Graphics) {}
  def allClear(game: GameBomb) {}
  def stuck(game: GameBomb) { game.wrapper.exit(0) }
  def beforeNewMino(game: GameBomb) {}
  def noNewMino(game: GameBomb) { game.wrapper.exit(0) }
  def newMino(game: GameBomb) {}
  def fillLine(game: GameBomb, hit: Int, chain: Int, freeze: Boolean) {}
  def afterBomb(game: GameBomb, eraseBlocks: Int) {}
  def makeBigBomb(game: GameBomb, num: Int) {}
}

class HandlerBombEndless extends HandlerBomb {
  def init(game: GameBomb) {
    game.field.generator = new MinoGeneratorBombInfinite(game.rule, new MinoGeneratorConfigBombInfinite(Set(4, 5, 6, 7, 8, 9, 10))) 
  }
}

class HandlerBombContest(val stageId: Int, val stage: BombContestStage) extends HandlerBomb {
  val bonus = Array(0, 0, 0, 0, 2, 5, 10, 20, 50, 100, 150, 200, 300, 400, 600, 800, 1000, 1000, 1000, 1000, 1000, 1000, 1000)
  var numMino = -1
  
  def init(game: GameBomb){
    for(iy <- 0 until stage.field.size; ix <- 0 until 10){
      game.field(ix, iy) = new Block(stage.field(iy)(ix))
    }
    var generatorConfig = new MinoGeneratorConfigBombInfinite(
      set = stage.mino_set.toSet,
      bombFrequency = stage.bomb_frequency,
      bombOffset = stage.bomb_offset,
      allBombFrequency = stage.allbomb_frequency,
      allBombOffset = stage.allbomb_offset,
      whiteFrequency = stage.white_frequency,
      whiteOffset = stage.white_offset,
      whiteLevel = stage.white_level,
      blackFrequency = stage.black_frequency,
      blackOffset = stage.black_offset,
      blackLevel = stage.black_level
    )
    game.field.generator = new MinoGeneratorBombInfinite(game.rule, generatorConfig)
    numMino = stage.mino_num

    game.fallCounterDelta = stage.gravity / 60f
    game.lockdownTimerMax = stage.lock
  }
  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString((stageId + 1).toString(), 384, 512, TextAlign.RIGHT)
    Resource.boldfont.drawString(numMino.toString(), 384, 488, TextAlign.RIGHT)
  }
  override def allClear(game: GameBomb) {
    game.wrapper.exit(1)
  }
  override def fillLine(game: GameBomb, hit: Int, chain: Int, freeze: Boolean){
    if(!freeze || chain == 0) {
      numMino += bonus(hit)
      if(hit >= 4) game.nextAllBombFlag = true
    }
  }
  override def afterBomb(game: GameBomb, eraseBlocks: Int) {

  }
  override def beforeNewMino(game: GameBomb) {
    numMino -= 1
    if(numMino < 0) game.wrapper.exit(0)
    val field = game.field
    var num = 0
    for(iy <- 0 until field.height; ix <- 0 until 10) {
      if(field(ix, iy).id != 0 && !field(ix, iy).unerasable) num += 1
    }
    if(num <= 10) game.nextAllBombFlag = true
  }
  override def makeBigBomb(game: GameBomb, num: Int){
    numMino += num
  }
}

class HandlerBombPuzzle(val stageId: Int, val stage: BombPuzzleStage) extends HandlerBomb {
  def init(game: GameBomb) {
    for(iy <- 0 until stage.field.size; ix <- 0 until 10){
      game.field(ix, iy) = new Block(stage.field(iy)(ix))
    }
    game.rule.enableHold = false
    game.field.generator = new MinoGeneratorBombFinite(game.rule, new MinoGeneratorConfigBombFinite(stage.mino_list)) 
    game.nextMinoDisplayType = 1
    game.fallCounterDelta = stage.gravity / 60f
    game.lockdownTimerMax = stage.lock
  }
  
  override def allClear(game: GameBomb) {
    game.wrapper.exit(1)
  }
}
