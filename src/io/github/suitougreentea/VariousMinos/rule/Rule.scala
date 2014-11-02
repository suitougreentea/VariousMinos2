package io.github.suitougreentea.VariousMinos.rule

trait Rule {
  val randomizer: Randomizer
  val rotation: RotationSystem
  val color: MinoColor
  val spawn: SpawnRule
  val enableUpKey = true
  val upKeyLock = true
  val downKeyLock = false
  val enableHold = true
  val numNext = 7
  val enableInitialHold = true
  val enableInitialMove = false
  val enableInitialRotate = true
}

class RuleClassic extends Rule {
  val randomizer = new RandomizerRandom()
  val rotation = new RotationSystemClassic()
  val color = new MinoColorClassic()
  val spawn = new SpawnRuleClassic()
  override val enableUpKey = false
  override val upKeyLock = true
  override val downKeyLock = true
  override val enableHold = false
  override val numNext = 1
  override val enableInitialRotate = false
}

class RuleStandard extends Rule {
  val randomizer = new RandomizerBag()
  val rotation = new RotationSystemStandard()
  val color = new MinoColorStandard()
  val spawn = new SpawnRuleStandard()
}