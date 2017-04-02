package com.esri

/**
  * A SOM node.
  *
  * @param coord  the coordinate of the node.
  * @param weight the weights of the node.
  */
case class Node(coord: Coord, weight: Weight) {

  /**
    * Add the weight of a given node to this weight.
    *
    * @param that a node to add.
    * @return a new Node instance with this coord and this.weight + that.weight.
    */
  def +(that: Node) = {
    Node(coord, weight + that.weight)
  }

  /**
    * Divide the weight of this node by given divisor.
    *
    * @param nume the divisor.
    * @return a new Node instance with adjusted weight.
    */
  def /(nume: Double) = {
    Node(coord, weight / nume)
  }

  /**
    * Calculate the distance of this node to a given node based on the node coords.
    *
    * @param that the given node.
    * @return the distance between this node coord and given node coord.
    */
  def distByCoord2(that: Node) = this.coord dist2 that.coord

  /**
    * Calculate the distance of this node weight to a given weight.
    *
    * @param that the given weight.
    * @return the distance between this node weight and the given weight.
    */
  def distByWeight(that: Weight) = this.weight dist that

  /**
    * Train this node for a given target weight.
    *
    * @param weight the target weight.
    * @param alpha  the learning rate.
    * @param theta  the distance infuence.
    * @return a new Node instance with adjusted weights.
    */
  def train(weight: Weight, alpha: Double, theta: Double) = Node(coord, this.weight.train(weight, alpha, theta))

}
