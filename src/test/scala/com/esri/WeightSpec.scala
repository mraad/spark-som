package com.esri

import org.apache.commons.math3.util.FastMath
import org.scalatest.{FlatSpec, Matchers}

class WeightSpec extends FlatSpec with Matchers {

  "A Weight" should "calculate distance to another weight" in {
    val weight1 = Weight(1, 2, 3)
    val weight2 = Weight(10, 20, 30)
    val dr = 10.0 - 1.0
    val dg = 20.0 - 2.0
    val db = 30.0 - 3.0
    weight1 dist weight2 shouldBe FastMath.sqrt(dr * dr + dg * dg + db * db)
  }

  "A Weight" should "train with alpha=0 and theta=0" in {
    val target = Weight(10, 20, 30)
    val weight = Weight(11, 21, 31)
    weight.train(target, 0.0, 0.0) shouldBe Weight(11, 21, 31)
  }

  "A Weight" should "train with alpha=0 and theta=1" in {
    val target = Weight(10, 20, 30)
    val weight = Weight(11, 21, 31)
    weight.train(target, 0.0, 1.0) shouldBe Weight(11, 21, 31)
  }

  "A Weight" should "train with alpha=1 and theta=0" in {
    // Theta is 1 when r is 0
    val target = Weight(10, 20, 30)
    val weight = Weight(11, 21, 31)
    weight.train(target, 1.0, 0.0) shouldBe Weight(11, 21, 31)
  }

  "A Weight" should "be the same as the target weight" in {
    // Theta is 1 when r is 0
    val target = Weight(10, 20, 30)
    val weight = Weight(11, 21, 31)
    weight.train(target, 1.0, 1.0) shouldBe Weight(10, 20, 30)
  }

  "A Weight" should "move 1/2 way to target weight" in {
    // Theta is 1 when r is 0
    val target = Weight(10, 20, 30)
    val weight = Weight(11, 21, 31)
    weight.train(target, 1.0, 0.5) shouldBe Weight(10.5, 20.5, 30.5)
  }

  "A Weight" should "move 3/4 way to target weight" in {
    // Theta is 1 when r is 0
    val target = Weight(10, 20, 30)
    val weight = Weight(11, 21, 31)
    weight.train(target, 1.0, 0.75) shouldBe Weight(10.25, 20.25, 30.25)
  }

  "A Weight" should "add to another weight" in {
    Weight(10, 20, 30) + Weight(11, 22, 33) shouldBe Weight(21, 42, 63)
  }

}
