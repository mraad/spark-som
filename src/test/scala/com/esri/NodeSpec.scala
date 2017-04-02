package com.esri

import org.scalatest.{FlatSpec, Matchers}

import scala.math._

class NodeSpec extends FlatSpec with Matchers {

  "A node" should "calculate distance to another node by coord" in {
    val orig = Node(Coord(10, 20), Weight(10, 20, 30))
    val dest = Node(Coord(20, 40), Weight(20, 30, 40))

    orig.distByCoord2(dest) shouldBe 10 * 10 + 20 * 20
  }

  "A node" should "calculate distance to another node by weight" in {
    val orig = Node(Coord(10, 20), Weight(10, 20, 30))
    val dest = Node(Coord(20, 40), Weight(20, 31, 42))

    orig.distByWeight(dest.weight) shouldBe sqrt(10 * 10 + 11 * 11 + 12 * 12)
  }

  "A node" should "train towards a target weight" in {
    val node = Node(Coord(10, 20), Weight(10, 20, 30))
    node.train(Weight(11, 22, 33), 1.0, 0.5) shouldBe Node(Coord(10, 20), Weight(10.5, 21, 31.5))
  }

  "A node" should "add to another node" in {
    Node(Coord(10, 20), Weight(10, 20, 30)) + Node(Coord(11, 21), Weight(11, 22, 33)) shouldBe
      Node(Coord(10, 20), Weight(21, 42, 63))
  }
}
