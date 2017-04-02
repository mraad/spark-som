package com.esri

import org.scalatest.{FlatSpec, Matchers}

class TSPSOMSpec extends FlatSpec with Matchers {

  it should "find BMU" in {
    val n1 = TSPNode(0, City(10, 11))
    val n2 = TSPNode(1, City(20, 22))
    val n3 = TSPNode(2, City(30, 33))
    val som = TSPSOM(Seq(n1, n2, n3))

    som.findBMU(City(9, 9)).nodeID shouldBe 0
    som.findBMU(City(10, 10)).nodeID shouldBe 0
    som.findBMU(City(11, 10)).nodeID shouldBe 0

    som.findBMU(City(19, 20)).nodeID shouldBe 1
    som.findBMU(City(20, 20)).nodeID shouldBe 1
    som.findBMU(City(21, 20)).nodeID shouldBe 1

    som.findBMU(City(29, 30)).nodeID shouldBe 2
    som.findBMU(City(30, 30)).nodeID shouldBe 2
    som.findBMU(City(31, 30)).nodeID shouldBe 2

  }
}
