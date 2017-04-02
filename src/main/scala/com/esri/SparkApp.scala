package com.esri

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

import scala.annotation.tailrec

object SparkApp extends App {
  val spark = SparkSession
    .builder()
    .appName("SOM App")
    .master("local[*]")
    .config("spark.ui.enabled", "false")
    .getOrCreate()

  import spark.implicits._

  try {
    val conf = ConfigFactory.load

    val rgbPath = conf.getString("rgbPath")
    val numIter = conf.getInt("numIterations")
    val numPart = conf.getInt("numPartitions")
    val alpha = conf.getDouble("alpha")
    val somSize = conf.getInt("somSize")
    val somCell = conf.getInt("somImageCell")
    val somPath = conf.getString("somImagePath")

    val schema = ScalaReflection.schemaFor[Weight].dataType.asInstanceOf[StructType]

    val ds = spark
      .read
      .option("delimiter", ",")
      .schema(schema)
      .csv(rgbPath)
      .as[Weight]
      .repartition(numPart)
      .cache()

    @tailrec
    def _train(iter: Int, som: SOM): SOM = {
      if (iter == numIter)
        som
      else {
        val params = Parameters(iter, numIter, somSize, alpha)
        val bc = spark.sparkContext.broadcast(som)
        val newSOM = ds
          .mapPartitions(iter => {
            val localSOM = iter.foldLeft(bc.value)((prevSOM, weight) => {
              prevSOM.train(weight, params)
            })
            Some(localSOM).iterator
          })
          .reduce(_ + _) / numPart.toDouble
        _train(iter + 1, newSOM)
      }
    }

    println("Training...")
    val som = _train(0, SOM(somSize))

    println("Saving...")
    ChartUtils.saveSOM(som, somPath, somSize, somCell)

    println("Done.")
  } finally {
    spark.close()
  }
}
