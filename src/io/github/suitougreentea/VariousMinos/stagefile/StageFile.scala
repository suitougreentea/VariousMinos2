package io.github.suitougreentea.VariousMinos.stagefile

import scala.reflect.ClassTag

abstract class StageFile[T <: Stage] {
   var `type`: String
   var version: Int
   var stages: Array[T]
}

abstract class Stage() {
}