package io.github.suitougreentea.VariousMinos

import scala.xml.XML
import scala.collection.mutable.ArraySeq
import scala.collection.mutable.HashSet

class StageLoader(val filename: String){
  var stage = new StageFile()
  
  def load(){
    val xml = XML.loadFile(filename)
    
    stage.`type` = (xml \ "@type").text
    stage.rule = (xml \ "@rule").text
    
    val stages = xml \ "stages" \ "stage"
    stage.stages = ArraySeq.fill(stages.size)(new Stage())
    
    for(i <- 0 until stages.size){
      val e = stages(i)
      val c = e \ "config"
      stage.`type` match {
        case "Contest" => {
          var list = c \ "mino" \ "list"
          var array = list.text.split(",")
          var set: HashSet[Int] = HashSet.empty
          for(s <- array){
            set += s.toInt
          }
          stage.stages(i).mino = new MinoGeneratorConfigBombInfinite(set)
        }
        case "Puzzle" => {
          var list = c \ "minos" \ "mino"
          var seq: ArraySeq[(Int, Int)] = ArraySeq.empty
          for(e <- list){
            seq = seq :+ (((e \ "@id").text.toInt, (e \ "@bomb").text.toInt))
          }
          stage.stages(i).mino = new MinoGeneratorConfigBombFinite(seq)
        }
      }
      
      val field = e \ "map" \ "row"
      stage.stages(i).field = Array.fill(field.size)(Array.fill(10)(0))
      for(j <- 0 until field.size){
        val s = field(j).text
        val a = s.split(",")
        for(k <- 0 until 10){
          stage.stages(i).field(j)(k) = a(k).toInt
        }
      }
    }
  }
  
  def apply(stageId: Int) = {
    stage.stages(stageId)
  }
}

class StageFile {
  var `type`, rule: String = ""
  var stages: ArraySeq[Stage] = ArraySeq.empty
}

class Stage {
  var mino: MinoGeneratorConfig = _
  var field: Array[Array[Int]] = Array.empty
}