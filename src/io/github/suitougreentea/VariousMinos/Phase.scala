package io.github.suitougreentea.VariousMinos

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer

trait Phase {
  val id: Int
  var beforeTime: Int
  var afterTime: Int
  def handleBeforeBefore(executer: PhaseExecuter) {}
  def procedureBefore(executer: PhaseExecuter) {
    if(executer.timer == beforeTime) {
      executer.timer = 0
      executer.moveToWorking()
    } else {
      executer.timer += 1 
    }
  }
  def handleAfterBefore(executer: PhaseExecuter) {}
  def procedureWorking(executer: PhaseExecuter)
  def handleBeforeAfter(executer: PhaseExecuter) {}
  def procedureAfter(executer: PhaseExecuter) {
    if(executer.timer == beforeTime) {
      executer.timer = 0
      executer.moveToNewPhase()
    } else {
      executer.timer += 1 
    }
  }
  def handleAfterAfter(executer: PhaseExecuter) {}
}

class PhaseExecuter (initialPhase: Phase) {
  var timer = 0
  var currentPosition = Position.BEFORE
  
  private var _currentPhase = initialPhase
  def currentPhase: Phase = _currentPhase
  currentPhase.handleBeforeBefore(this)
  
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
    _currentPhase.handleAfterAfter(this)
    _currentPhase = _nextPhase
    currentPosition = Position.BEFORE
    _currentPhase.handleBeforeBefore(this)
    _currentPhase.procedureBefore(this)
  }
  
  def moveToWorking() {
    currentPosition = Position.WORKING
    _currentPhase.handleAfterBefore(this)
    _currentPhase.procedureWorking(this)
  }
  
  def moveToAfter() {
    currentPosition = Position.AFTER
    _currentPhase.handleBeforeAfter(this)
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