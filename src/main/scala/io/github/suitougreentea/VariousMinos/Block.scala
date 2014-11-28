package io.github.suitougreentea.VariousMinos

/*
 * 64: Bomb
 * 65-68: BigBomb
 * 69-74: WhiteBlock
 * 75-80: BlackBlock
 * 81-86: ||Block
 */
class Block (var id: Int){
    //def copy = new Block(id)
  def transparent = if(id == 0) true else false
  var persistent = id match {
    case 75 | 76 | 77 | 78 | 79 | 80 => true
    case _ => false
  }
  var independent = id match {
    case 81 | 82 | 83 | 84 | 85 | 86 => true
    case _ => false
  }
  var unerasable = id match {
    case 74 | 80 | 86 => true
    case _ => false
  }
  var active = false
  var filled = false
}
