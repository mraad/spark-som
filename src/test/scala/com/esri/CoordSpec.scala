package com.esri

import org.scalatest.{FlatSpec, Matchers}

class CoordSpec extends FlatSpec with Matchers {
  "A Coord" should "calculate distance^2" in {
    val coord = Coord(10, 20)
    coord.dist2(Coord(20, 40)) shouldBe 10.0 * 10.0 + 20.0 * 20.0
  }
}
