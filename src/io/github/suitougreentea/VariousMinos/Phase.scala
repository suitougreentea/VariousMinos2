package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer

trait Phase {
  val id: Int
  def beforeTime: Int
  def afterTime: Int
  def procedureBefore(executer: PhaseExecuter) {
    if(executer.timer == beforeTime) {
      executer.timer = 0
      executer.moveToWorking()
    } else {
      executer.timer += 1 
    }
  }
  def procedureWorking(executer: PhaseExecuter)
  def procedureAfter(executer: PhaseExecuter) {
    if(executer.timer == beforeTime) {
      executer.timer = 0
      executer.moveToNewPhase()
    } else {
      executer.timer += 1 
    }
  }
}

class PhaseExecuter (val game: StateBasedGame) {
  var timer = 0
  var currentPosition = Position.BEFORE
  
  private var _currentPhase : Phase = new Phase {
    val id = -1
    def beforeTime = 0
    def afterTime = 0
    def procedureWorking(executer: PhaseExecuter) = ???
  }
  
  def currentPhase = _currentPhase
  
  private var _nextPhase : Phase = null
  
  // Procedureから呼ばれる。通常はAfterを通って次のフェーズへ移る
  def enterPhase(nextPhase: Phase, enableAfterTimer: Boolean = true) {
    _nextPhase = nextPhase
    if (enableAfterTimer) {
      moveToAfter()
    } else {
      moveToNewPhase() 
    }
  }
  
  // 実際にフェーズが移り変わる
  def moveToNewPhase() {
    _currentPhase = _nextPhase
    currentPosition = Position.BEFORE
    _currentPhase.procedureBefore(this)
  }
  
  def moveToWorking() {
    currentPosition = Position.WORKING
    _currentPhase.procedureWorking(this)
  }
  
  def moveToAfter() {
    currentPosition = Position.AFTER
    _currentPhase.procedureAfter(this)
  }
  
  def exec() {
    currentPosition match {
      case Position.BEFORE => _currentPhase.procedureBefore(this)
      case Position.WORKING => _currentPhase.procedureWorking(this)
      case Position.AFTER => _currentPhase.procedureAfter(this)
    }
  }
}

object Position extends Enumeration {
  val BEFORE, WORKING, AFTER = Value
}