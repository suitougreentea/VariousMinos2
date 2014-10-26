package io.github.suitougreentea.VariousMinos

/*
 * 64: Bomb
 * 65-68: BigBomb
 * 69-73: WhiteBlock
 * 74-78: BlackBlock
 * 79: WhiteUnbreakable
 * 80: BlackUnbreakable
 */
class Block (var id: Int){
    //def copy = new Block(id)
  def transparent = if(id == 0) true else false
  var persistent = id match {
    case 74 | 75 | 76 | 77 | 78 | 80 => true
    case _ => false
  } 
  var active = false
  var filled = false
}