package com.esri

import com.typesafe.config.ConfigFactory
import pb.ProgressBar

import scala.annotation.tailrec
import scala.collection.generic.Growable
import scala.collection.mutable.ArrayBuffer

/**
  * Standalone SOM App
  */
object TSPApp extends App {

  val conf = ConfigFactory.load

  val numIter = conf.getInt("numIterations")
  val alpha = conf.getDouble("alpha")

  val numCities = conf.getInt("numCities")
  val somSize = conf.getInt("numNodesPerCity") * numCities
  val somImagePath = conf.getString("somImagePath")
  val somImageSize = conf.getInt("somImageSize")

  val errImagePath = conf.getString("errImagePath")
  val errImagWidth = conf.getInt("errImageWidth")
  val errImageHeight = conf.getInt("errImageHeight")

  val cities = new ArrayBuffer[City](numCities)
  var n = 0
  while (n < numCities) {
    cities += City.random
    n += 1
  }

  val rnd = new java.security.SecureRandom()

  val progressBar = new ProgressBar(numIter)
  progressBar.showPercent = false
  progressBar.showSpeed = false
  progressBar.showTimeLeft = false

  @tailrec
  def train(iter: Int, som: TSPSOM, err: Growable[Double]): TSPSOM = {
    progressBar += 1
    err += som.calcError(cities)
    if (iter == numIter)
      som
    else {
      val params = Parameters(iter, numIter, somSize, alpha)
      val weight = cities(rnd.nextInt(cities.length))
      train(iter + 1, som.train(weight, params), err)
    }
  }

  progressBar.finish()

  val err = new ArrayBuffer[Double](numIter)
  val som = train(0, TSPSOM(somSize), err)

  ChartUtils.saveTSP(som, cities, somImagePath, somImageSize)
  ChartUtils.saveErr(err, errImagePath, errImagWidth, errImageHeight)

}
