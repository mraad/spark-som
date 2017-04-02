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

  /**
    * Save the SOM grid as a image.
    *
    * @param som        the SOM to save.
    * @param outputPath the output path.
    * @param somSize    the SOM size.
    * @param cellSize   the grid cell size in pixels.
    */
  def saveSOM(som: SOM, outputPath: String, somSize: Int, cellSize: Int): Unit = {
    val bi = new BufferedImage(somSize * cellSize, somSize * cellSize, BufferedImage.TYPE_INT_RGB)
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
    writePNG(bi, outputPath)
  }

  /**
    * Write a buffered image to a file in PNG format.
    *
    * @param bi         the buffered image.
    * @param outputPath the file path.
    */
  private def writePNG(bi: BufferedImage, outputPath: String): Unit = {
    val outputStream = new FileOutputStream(outputPath)
    try {
      ImageIO.write(bi, "png", outputStream)
    } finally {
      outputStream.close()
    }
  }

  /**
    * Save the error sequence as a line chart.
    *
    * @param err        the error sequence.
    * @param outputPath the PNG output file location.
    * @param width      the chart width in pixels.
    * @param height     the chart height in pixels.
    */
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

  /**
    * Save the TSP SOM network as a image. This will create a virtual canvas onto which it will draw the input cities as black squares, the network nodes as red squares and finally will draw a red line between the network nodes to show the path that the SOM has organized.
    *
    * @param som        the TSP SOM.
    * @param cities     the input cities.
    * @param outputPath the output PNG file path.
    * @param imageSize  the image size in pixels.
    */
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
      // Clear the background.
      g.setColor(Color.WHITE)
      g.fillRect(0, 0, imageSize - 1, imageSize - 1)
      // Draw the cities to visit in black.
      g.setColor(Color.BLACK)
      cities.foreach(city => {
        val (px, py) = toPixel(city)
        g.fillRect(px - 3, py - 3, 6, 6)
      })
      // Draw the network nodes in red.
      g.setColor(Color.RED)
      som.nodes.foreach(node => {
        val (px, py) = toPixel(node.city)
        g.drawRect(px - 3, py - 3, 6, 6)
      })
      // Connect 2 consecutive nodes with a line to show a path.
      som.nodes.sliding(2).foreach {
        case Seq(n1, n2) => {
          val (px, py) = toPixel(n1.city)
          val (qx, qy) = toPixel(n2.city)
          g.drawLine(px, py, qx, qy)
        }
      }
      // Close the path by drawing a line from the first node to the last.
      val (px, py) = toPixel(som.nodes.head.city)
      val (qx, qy) = toPixel(som.nodes.last.city)
      g.drawLine(px, py, qx, qy)
    } finally {
      g.dispose()
    }
    writePNG(bi, outputPath)
  }

}
