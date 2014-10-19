package io.github.suitougreentea.VariousMinos

class Block (var id: Int){
    //def copy = new Block(id)
  def transparent = if(id == 0) true else false
  
  var active = false
  var filled = false
}