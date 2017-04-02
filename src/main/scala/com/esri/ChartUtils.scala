package com.esri

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{File, FileOutputStream}
import javax.imageio.ImageIO

import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartUtilities}
import org.jfree.data.xy.DefaultXYDataset

/**
  * Utility class to save charted data.
  */
object ChartUtils extends Serializable {

  def saveSOM(som: SOM, outputPath: String, mapSize: Int): Unit = {
    saveSOM(som, outputPath, mapSize, 10)
  }

  def saveSOM(som: SOM, outputPath: String, mapSize: Int, cellSize: Int): Unit = {
    val bi = new BufferedImage(mapSize * cellSize, mapSize * cellSize, BufferedImage.TYPE_INT_RGB)
    som.nodes.foreach(node => {
      var sx = node.coord.x * cellSize
      var i = 0
      while (i < cellSize) {
        var sy = node.coord.y * cellSize
        var j = 0
        while (j < cellSize) {
          bi.setRGB(sx, sy, node.weight.rgb)
          sy += 1
          j += 1
        }
        sx += 1
        i += 1
      }
    })
    writePNG(outputPath, bi)
  }

  private def writePNG(outputPath: String, bi: BufferedImage) = {
    val outputStream = new FileOutputStream(outputPath)
    try {
      ImageIO.write(bi, "png", outputStream)
    } finally {
      outputStream.close()
    }
  }

  def saveErr(err: Seq[Double], outputPath: String, width: Int, height: Int): Unit = {
    val dataset = new DefaultXYDataset()
    val x = new Array[Double](err.length)
    val y = new Array[Double](err.length)
    val data = Array[Array[Double]](x, y)
    dataset.addSeries("Error", data)

    err.zipWithIndex.foreach {
      case (d, i) => {
        x(i) = i.toDouble
        y(i) = d
      }
    }

    val chart = ChartFactory.createXYLineChart("SOM", "Epoch", "Avg Distance Difference",
      dataset, PlotOrientation.VERTICAL, false, false, false)
    ChartUtilities.saveChartAsPNG(new File(outputPath), chart, width, height)
  }

  def saveTSP(som: TSPSOM, cities: Seq[City], outputPath: String, imageSize: Int) = {
    val xmin = -1.0
    val xmax = 1.0
    val ymin = -1.0
    val ymax = 1.0
    val dx = xmax - xmin
    val dy = ymax - ymin

    def toPixel(city: City) = {
      val px = (imageSize * (city.x - xmin) / dx).toInt
      val py = (imageSize * (city.y - ymin) / dy).toInt
      (px, py)
    }

    val bi = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB)
    val g = bi.getGraphics
    try {
      g.setColor(Color.WHITE)
      g.fillRect(0, 0, imageSize - 1, imageSize - 1)
      g.setColor(Color.BLACK)
      cities.foreach(city => {
        val (px, py) = toPixel(city)
        g.fillRect(px - 3, py - 3, 6, 6)
      })
      g.setColor(Color.RED)
      som.nodes.foreach(node => {
        val (px, py) = toPixel(node.city)
        g.drawRect(px - 3, py - 3, 6, 6)
      })
      som.nodes.sliding(2).foreach {
        case Seq(n1, n2) => {
          val (px, py) = toPixel(n1.city)
          val (qx, qy) = toPixel(n2.city)
          g.drawLine(px, py, qx, qy)
        }
      }
      val (px, py) = toPixel(som.nodes.head.city)
      val (qx, qy) = toPixel(som.nodes.last.city)
      g.drawLine(px, py, qx, qy)
    } finally {
      g.dispose()
    }
    writePNG(outputPath, bi)
  }

  /**
    * Plot error curve using Lightning server.
    *
    * @param err  the error sequence.
    * @param host the lightning host.
    */
  /*
  def plotErr(err: Seq[Double], host: String = "http://localhost:3000"): Unit = {
    val l = Lightning(host)
    l.createSession("SOM Err")
    l.line(Array(err.toArray))
  }
  */
}
