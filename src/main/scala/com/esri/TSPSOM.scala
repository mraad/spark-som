package com.esri

import scala.collection.mutable.ArrayBuffer
import scala.math._

/**
  * Create a circular 1D SOM.
  *
  * @param nodes the network nodes.
  */
case class TSPSOM(nodes: Seq[TSPNode]) {

  private val lenNodes = nodes.length

  /**
    * Find the best matching node for a given city.
    *
    * @param city a given city.
    * @return the best matching node.
    */
  def findBMU(city: City) = {
    nodes.minBy(_.city manDist city)
  }

  /**
    * Calculate the error given a set of cities.
    *
    * @param cities the cities to evaluate.
    * @return the average distance between the BMUs and the cities.
    */
  def calcError(cities: Seq[City]) = {
    cities
      .foldLeft(0.0)((prev, city) => {
        prev + findBMU(city).city.manDist(city)
      }) / cities.length
  }

  /**
    * Train the network given a city, search radius and a learning rate.
    *
    * @param city   the city to train on.
    * @param radius the search radius.
    * @param alpha  the learning rate.
    * @return a new TSPSOM instance with adjusted nodes.
    */
  def train(city: City, radius: Double, alpha: Double): TSPSOM = {
    val radius2 = radius * radius
    val deno2 = -2.0 * radius2
    val bmu = findBMU(city)
    val adjNodes = nodes.foldLeft(new ArrayBuffer[TSPNode](lenNodes))((arr, node) => {
      val dist2 = node.dist2(bmu, lenNodes)
      // TODO: do not cap by radius, just decay the node.
      if (dist2 <= radius2) {
        val theta = exp(dist2 / deno2)
        arr += node.train(city, alpha, theta)
      } else {
        arr += node
      }
    })
    TSPSOM(adjNodes)
  }

  /**
    * Train the network given a city and training parameters.
    *
    * @param city  the city to train on.
    * @param param the training parameters.
    * @return a new TSPSOM instance with adjusted nodes.
    */
  def train(city: City, param: Parameters): TSPSOM = {
    train(city, param.radius, param.alpha)
  }
}

object TSPSOM extends Serializable {
  /**
    * Create a TSPSOM instance with random cities.
    *
    * @param nodeIDMax the number of nodes.
    * @return a TSPSOM instance initialized with random cities.
    */
  def apply(nodeIDMax: Int) = {
    val arr = new ArrayBuffer[TSPNode](nodeIDMax)
    var nodeID = 0
    while (nodeID < nodeIDMax) {
      arr += TSPNode(nodeID, City.random)
      nodeID += 1
    }
    new TSPSOM(arr)
  }
}
