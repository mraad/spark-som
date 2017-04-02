package com.esri

import com.typesafe.config.ConfigFactory
import pb.ProgressBar

import scala.annotation.tailrec
import scala.collection.generic.Growable
import scala.collection.mutable.ArrayBuffer

/**
  * App to SOM RGB Colors.
  */
object SOMApp extends App {

  // Load the HOCON.
  val conf = ConfigFactory.load

  val numWeights = conf.getInt("numWeights")
  val numIter = conf.getInt("numIterations")
  val alpha = conf.getDouble("alpha")

  val somSize = conf.getInt("somSize")
  val somImagePath = conf.getString("somImagePath")
  val somImageCell = conf.getInt("somImageCell")

  val errImagePath = conf.getString("errImagePath")
  val errImageWidth = conf.getInt("errImageWidth")
  val errImageHeight = conf.getInt("errImageHeight")

  // Create a set on random colors.
  val weights = new ArrayBuffer[Weight](numWeights)
  var n = 0
  while (n < numWeights) {
    weights += Weight.random
    n += 1
  }

  val rnd = new java.security.SecureRandom()

  val pb = new ProgressBar(numIter)
  pb.showPercent = false
  pb.showSpeed = false
  pb.showTimeLeft = false

  @tailrec
  def train(iter: Int, som: SOM, err: Growable[Double]): SOM = {
    pb += 1
    err += som.calcError(weights)
    if (iter == numIter)
      som
    else {
      val params = Parameters(iter, numIter, somSize, alpha)
      val weight = weights(rnd.nextInt(weights.length))
      train(iter + 1, som.train(weight, params), err)
    }
  }

  pb.finish()

  val err = new ArrayBuffer[Double](numIter)
  val som = train(0, SOM(somSize, weights), err)

  ChartUtils.saveSOM(som, somImagePath, somSize, somImageCell)
  ChartUtils.saveErr(err, errImagePath, errImageWidth, errImageHeight)

}
