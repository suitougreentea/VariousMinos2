package io.github.suitougreentea.VariousMinos.game

import io.github.suitougreentea.VariousMinos.Resource
import io.github.suitougreentea.util.TextAlign
import org.newdawn.slick.Graphics

trait HandlerBombSurvival extends HandlerBomb {
  var level = 0
  var nextStop = 100
  var eraseBlocks = 0
  var endLevel: Int
  
  var handleLevel: List[Int]
  var handleLevelIterator = 0
  
  override def render(game: GameBomb, g: Graphics) {
    Resource.boldfont.drawString(nextStop.toString(), 384, 512, TextAlign.RIGHT)
    Resource.boldfont.drawString(level.toString(), 384, 488, TextAlign.RIGHT)
  }
  override def newMino(game: GameBomb) {
    increaseLevel(game, 1, false)
  }
  
  override def fillLine(game: GameBomb, hit: Int, chain: Int, freeze: Boolean) {
    if(freeze){
      increaseLevel(game, hit, false)
    } else {
      increaseLevel(game, hit, true)
    }
  }
  override def afterBomb(game: GameBomb, eraseBlocks: Int) {
    this.eraseBlocks += eraseBlocks
    while(this.eraseBlocks >= 50) {
      increaseLevel(game, 1, false)
      this.eraseBlocks -= 50
    }
  }
  
  override def makeBigBomb(game: GameBomb, num: Int) {
    increaseLevel(game, num, false)
  }
  
  def increaseLevel(game: GameBomb, num: Int, breakStop: Boolean) {
    if(breakStop || nextStop - level > 1) {
      level += num
      if(level > endLevel) {
        level = endLevel
      }else if(level >= nextStop){
        nextStop += 100
      }
      checkHandle(game)
    }
  }
  
  def checkHandle(game: GameBomb){
    if(handleLevelIterator < handleLevel.size) {
      var nextLevel = handleLevel(handleLevelIterator)
      if(level >= nextLevel) {
        handle(game, nextLevel)
        handleLevelIterator += 1
        checkHandle(game)
      }
    }
  }
  
  def handle(game: GameBomb, level: Int)
}

