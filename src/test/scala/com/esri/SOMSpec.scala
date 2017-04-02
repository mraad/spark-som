package com.esri

import org.scalatest.{FlatSpec, Matchers}

class SOMSpec extends FlatSpec with Matchers {

  "A SOM" should "find BMU" in {
    val node00 = Node(Coord(0, 0), Weight(0, 0, 0))
    val node01 = Node(Coord(10, 0), Weight(10, 0, 0))
    val node10 = Node(Coord(0, 10), Weight(0, 10, 0))
    val node11 = Node(Coord(10, 10), Weight(10, 10, 0))
    val som = new SOM(Seq(
      node00,
      node01,
      node10,
      node11
    ))
    som.findBestMatchingNode(Weight(1.0, 1.0, 0)) shouldBe node00
    som.findBestMatchingNode(Weight(9.0, 1.0, 0)) shouldBe node01
    som.findBestMatchingNode(Weight(1.0, 9.0, 0)) shouldBe node10
    som.findBestMatchingNode(Weight(9.0, 9.0, 0)) shouldBe node11
  }

  "A SOM" should "train" in {
    val node00 = Node(Coord(0, 0), Weight(0, 0, 0))
    val node01 = Node(Coord(10, 0), Weight(10, 0, 0))
    val node10 = Node(Coord(0, 10), Weight(0, 10, 0))
    val node11 = Node(Coord(10, 10), Weight(10, 10, 0))
    val som = new SOM(Seq(
      node00,
      node01,
      node10,
      node11
    ))
    val res = som.train(Weight(1, 1, 0), radius = 5, alpha = 0)
    res.nodes(1) shouldBe node01
    res.nodes(2) shouldBe node10
    res.nodes(3) shouldBe node11

    val l0 = res.nodes(0)
    l0.coord shouldBe node00.coord
    l0.weight shouldBe Weight(0, 0, 0)
  }

  "A SOM" should "Add to another SOM" in {
    // TODO
  }
}
