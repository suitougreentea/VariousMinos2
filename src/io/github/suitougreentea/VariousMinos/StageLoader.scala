package io.github.suitougreentea.VariousMinos

import scala.xml.XML
import scala.collection.mutable.ArraySeq

class StageLoader {
  var stage = new StageFile()
  
  def load(){
    val xml = XML.loadFile("stage/stage.vms")
    
    stage.`type` = (xml \ "@type").text
    stage.rule = (xml \ "@rule").text
    
    val stages = xml \ "stages" \ "stage"
    stage.stages = ArraySeq.fill(stages.size)(new Stage())
    
    for(i <- 0 until stages.size){
      val e = stages(i)
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
  var field: Array[Array[Int]] = Array.empty
}