package com.esri

/**
  * The node coordinates.
  *
  * @param x the horizontal displacement.
  * @param y the vertical displacement.
  */
case class Coord(x: Int, y: Int) {

  /**
    * Compute the distance squared to a given coord.
    *
    * @param that a given coord instance.
    * @return the distance squared to a given coord
    */
  def dist2(that: Coord) = {
    val dx = (that.x - this.x).toDouble
    val dy = (that.y - this.y).toDouble
    dx * dx + dy * dy
  }

}
