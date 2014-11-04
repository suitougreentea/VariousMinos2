package io.github.suitougreentea.VariousMinos.rule

trait Rule {
  val randomizer: Randomizer
  val rotation: RotationSystem
  val color: MinoColor
  val spawn: SpawnRule
  val enableUpKey = true
  val upKeyLock = true
  val downKeyLock = false
  var enableHold = true
  val numNext = 7
  val enableInitialHold = true
  val enableInitialMove = false
  val enableInitialRotate = true
  val resetByFalling = true
  val resetByRotating = true
  val resetByMoving = true
}

class RuleClassic extends Rule {
  val randomizer = new RandomizerRandom()
  val rotation = new RotationSystemClassic()
  val color = new MinoColorClassic()
  val spawn = new SpawnRuleClassic()
  override val enableUpKey = false
  override val upKeyLock = true
  override val downKeyLock = true
  enableHold = false
  override val numNext = 1
  override val enableInitialRotate = false
  override val resetByRotating = false
  override val resetByMoving = false
}

class RuleClassicPlus extends RuleClassic {
  override val enableUpKey = true
  override val upKeyLock = true
  override val downKeyLock = false
  enableHold = true
  override val numNext = 7
  override val resetByRotating = true
  override val resetByMoving = true
}

class RuleStandard extends Rule {
  val randomizer = new RandomizerBag()
  val rotation = new RotationSystemStandard()
  val color = new MinoColorStandard()
  val spawn = new SpawnRuleStandard()
}

class RuleVariantClassic extends Rule {
  val randomizer = new RandomizerBag()
  val rotation = new RotationSystemVariantClassic()
  val color = new MinoColorVariant()
  val spawn = new SpawnRuleVariant()
  override val upKeyLock = false
  override val downKeyLock = true
  enableHold = false
  override val numNext = 1
  override val resetByRotating = false
  override val resetByMoving = false
}

class RuleVariantPlus extends RuleVariantClassic {
  override val rotation = new RotationSystemVariantPlus()
  enableHold = true
  override val numNext = 7
}