package com.esri

import org.scalatest.{FlatSpec, Matchers}

class TSPNodeSpec extends FlatSpec with Matchers {

  it should "calc distance between 1 and 4" in {
    val n1 = TSPNode(1, City.random)
    val n2 = TSPNode(4, City.random)
    n1.dist(n2, 5) shouldBe 2
    n2.dist(n1, 5) shouldBe 2
  }

  it should "calc distance between 2 and 4" in {
    val n1 = TSPNode(2, City.random)
    val n2 = TSPNode(4, City.random)
    n1.dist(n2, 5) shouldBe 2
    n2.dist(n1, 5) shouldBe 2
  }

  it should "calc distance between 2 and 3" in {
    val n1 = TSPNode(2, City.random)
    val n2 = TSPNode(3, City.random)
    n1.dist(n2, 5) shouldBe 1
    n2.dist(n1, 5) shouldBe 1
  }

  it should "calc distance between 0 and 4" in {
    val n1 = TSPNode(0, City.random)
    val n2 = TSPNode(4, City.random)
    n1.dist(n2, 5) shouldBe 1
    n2.dist(n1, 5) shouldBe 1
  }

  it should "be less or equal to 2" in {
    var i = 0
    while (i < 5) {
      val n1 = TSPNode(i, City.random)
      var j = 0
      while (j < 5) {
        val n2 = TSPNode(j, City.random)
        n1.dist(n2, 5) should be <= 2
        n2.dist(n1, 5) should be <= 2
        j += 1
      }
      i += 1
    }
  }

  it should "be less or equal to 5" in {
    var i = 0
    while (i < 10) {
      val n1 = TSPNode(i, City.random)
      var j = 0
      while (j < 10) {
        val n2 = TSPNode(j, City.random)
        n1.dist(n2, 10) should be <= 5
        n2.dist(n1, 10) should be <= 5
        j += 1
      }
      i += 1
    }
  }

  it should "calc distance between 1 8" in {
    val n1 = TSPNode(1, City.random)
    val n2 = TSPNode(8, City.random)
    n1.dist(n2, 10) shouldBe 3
    n2.dist(n1, 10) shouldBe 3
  }
}
