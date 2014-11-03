package io.github.suitougreentea.VariousMinos

import io.github.suitougreentea.VariousMinos.rule.Rule
import io.github.suitougreentea.VariousMinos.game.HandlerBomb

class DefaultSetting(var rule: Rule, val generator: MinoGenerator){
  var field: Array[Array[Int]] = _
}

class DefaultSettingBomb(rule: Rule, var handler: HandlerBomb, generator: MinoGenerator) extends DefaultSetting(rule, generator) {
}