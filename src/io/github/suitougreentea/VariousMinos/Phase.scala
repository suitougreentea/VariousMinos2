package io.github.suitougreentea.VariousMinos

trait Phase {
  def beforeTime: Int
  def afterTime: Int
  def procedure(executer: PhaseExecuter)
}

class PhaseExecuter {
  private var _currentState = new Phase {
    def beforeTime = 0
    def afterTime = 0
    def procedure(executer: PhaseExecuter) = ???
  }
  def enterState(id: Int, enableAfterTimer: Boolean) {
    ???
  }
  
  def exec() {
    _currentState.procedure(this)
  }
}