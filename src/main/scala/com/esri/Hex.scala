package com.esri

import scala.math._

/*
 http://www.redblobgames.com/grids/hexagons/
 TODO: Finish this
 */

case class CubeD(x: Double, y: Double, z: Double) {

  def toHexD() = {
    HexD(x, z)
  }

  def toHexI() = {
    HexI(x.toInt, z.toInt)
  }

  def round() = {
    val rx = x.round
    val ry = y.round
    val rz = z.round

    val dx = (rx - x).abs
    val dy = (ry - y).abs
    val dz = (rz - z).abs

    if (dx > dy && dx > dz) {
      CubeD(-ry - rz, ry, rz)
    } else if (dy > dz) {
      CubeD(rx, -rx - rz, rz)
    } else {
      CubeD(rx, ry, -rx - ry)
    }
  }
}

case class HexI(q: Int, r: Int)

object HexI extends Serializable {
  def fromPixel(x: Double, y: Double, size: Double) = {
    val q = x * 2.0 / 3.0 / size
    val r = (-x / 3.0 + sqrt(3) / 3.0 * y) / size
    HexD(q, r).round()
  }
}

case class HexD(q: Double, r: Double) {
  def toCubeD() = {
    CubeD(q, -q - r, r)
  }

  def round() = {
    toCubeD
      .round
      .toHexI
  }
}
