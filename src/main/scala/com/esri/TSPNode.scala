package com.esri

/**
  * A node in a circular network.
  *
  * @param nodeID the node id.
  * @param city   the associated city.
  */
case class TSPNode(nodeID: Int, city: City) {

  /**
    * Calculate the min number of between this node and a given node in a circular network.
    *
    * @param that     a given node.
    * @param maxNodes the max number of nodes in circular network.
    * @return the min number of nodes between this node and a given node.
    */
  def dist(that: TSPNode, maxNodes: Int) = {
    val distR = (that.nodeID - this.nodeID).abs
    val distL = maxNodes - distR
    distR min distL
  }

  /**
    * Calc the distance squared to a given node.
    *
    * @param that     a given node.
    * @param maxNodes the max number of nodes.
    * @return the distance squared
    */
  def dist2(that: TSPNode, maxNodes: Int) = {
    val d = dist(that, maxNodes)
    d * d
  }

  /**
    * Train this node for a given city.
    *
    * @param city  a city to train on.
    * @param alpha the learning rate.
    * @param theta the distance influence.
    * @return a new TSPNode instance.
    */
  def train(city: City, alpha: Double, theta: Double) = {
    TSPNode(nodeID, this.city.train(city, alpha, theta))
  }

}
