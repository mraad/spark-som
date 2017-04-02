package com.esri

import scala.collection.mutable.ArrayBuffer
import scala.math._

/**
  * Self Organizing Map with nodes arranged on a square grid.
  *
  * @param nodes the SOM nodes.
  */
case class SOM(val nodes: Seq[Node]) {

  /**
    * Best Matching Unit
    *
    * @param node the best matching node.
    * @param dist the smallest distance.
    */
  case class BMU(node: Node, dist: Double = Double.PositiveInfinity)

  /**
    * Find Best Matching Unit for a given weight.
    *
    * @param weight the weight to match.
    * @return the best matching unit.
    */
  private def findBMU(weight: Weight) = {
    nodes
      .par
      .foldLeft(BMU(nodes.head))((best, node) => {
        val dist = node distByWeight weight
        if (dist < best.dist)
          BMU(node, dist)
        else
          best
      })
  }

  /**
    * Find Best Matching Node for a given weight.
    *
    * @param weight the weight to match.
    * @return the best matching node.
    */
  def findBestMatchingNode(weight: Weight) = {
    findBMU(weight).node
  }

  /**
    * Create a new SOM given a training weight, a search radius and a learning rate.
    *
    * @param weight the training weight to self organize around.
    * @param radius the search radius around BMU.
    * @param alpha  the learning rate.
    * @return a new SOM instance with updated weights.
    */
  def train(weight: Weight, radius: Double, alpha: Double): SOM = {
    val radius2 = radius * radius
    val deno2 = -2.0 * radius2
    val bmu = findBestMatchingNode(weight)
    val adjustedNodes = nodes
      // TODO: Test if below 'par' will accelerate the training process.
      // .par
      .foldLeft(new ArrayBuffer[Node](nodes.length))((arr, node) => {
      val dist2 = node distByCoord2 bmu
      if (dist2 <= radius2) {
        val theta = exp(dist2 / deno2)
        arr += node.train(weight, alpha, theta)
      } else {
        arr += node
      }
      // arr
    })
    new SOM(adjustedNodes)
  }

  /**
    * Create a new SOM given a training weight and training parameters
    *
    * @param weight     the training weight.
    * @param parameters thr training parameters.
    * @return a new SOM instance with updated weights.
    */
  def train(weight: Weight, parameters: Parameters): SOM = {
    train(weight, parameters.radius, parameters.alpha)
  }

  /**
    * Add this SOM to a given SOM. This SOM node coordinates are preserved. However, the node weights are added together.
    *
    * @param that the SOM to add.
    * @return a new SOM instance.
    */
  def +(that: SOM) = {
    new SOM(this.nodes.zip(that.nodes).map { case (l, r) => l + r })
  }

  /**
    * Divide each node weight by given divisor.
    *
    * @param nume the divisor.
    * @return a new SOM instance with adjusted nodes.
    */
  def /(nume: Double) = {
    new SOM(this.nodes.map(_ / nume))
  }

  /**
    * Calculate the error of the SOM based on given training weights.
    * It is the avg of the best matching units distances.
    *
    * @param weights the training weights to use in the SOM evaluation.
    * @return avg distance of the training weights to the associated SOM BMU.
    */
  def calcError(weights: Seq[Weight]) = {
    weights
      .foldLeft(0.0)((prev, weight) => {
        prev + findBMU(weight).dist
      }) / weights.length
  }
}

object SOM extends Serializable {
  /**
    * Create a new SOM instance based on a given size and with random weights at each node.
    *
    * @param size the SOM width and height.
    * @return a new SOM instance.
    */
  def apply(size: Int): SOM = {
    apply(size, () => Weight.random)
  }

  /**
    * Create a new SOM instance based on a given size and set of training weights.
    * Each node in the SOM will be initialized with a random selection from the training weights.
    *
    * @param size    the SOM width and height.
    * @param weights the training weights to randomly select from to initialize the weights of a node.
    * @return a new SOM instance.
    */
  def apply(size: Int, weights: Seq[Weight]): SOM = {
    val rnd = new java.security.SecureRandom()
    val len = weights.length
    apply(size, () => weights(rnd.nextInt(len)))
  }

  /**
    * Create a new SOM instance based on a given size and a function to initialize the weights.
    *
    * @param size the SOM width and height.
    * @param f    function that returns a Weight instance.
    * @return a new SOM instance.
    */
  def apply(size: Int, f: () => Weight): SOM = {
    val arr = new ArrayBuffer[Node](size * size)
    var y = 0
    while (y < size) {
      var x = 0
      while (x < size) {
        arr += Node(Coord(x, y), f())
        x += 1
      }
      y += 1
    }
    new SOM(arr)
  }
}
