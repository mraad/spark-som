package com.esri

/**
  * Class to represent a city
  *
  * @param x the city x location
  * @param y the city y location
  */
case class City(x: Double, y: Double) {

  /**
    * Calculate the manhattan distance to another city.
    *
    * @param that the other city.
    * @return the manhattan distance to other city.
    */
  def manDist(that: City) = {
    val dx = (that.x - this.x).abs
    val dy = (that.y - this.y).abs
    dx + dy
  }

  /**
    * Train this city to move closer to a target city.
    *
    * @param that  the target city.
    * @param alpha the learning rate.
    * @param theta the distance influence.
    * @return a new City instance.
    */
  def train(that: City, alpha: Double, theta: Double) = {

    def train(w: Double, t: Double) = w + alpha * theta * (t - w)

    City(train(this.x, that.x), train(this.y, that.y))
  }
}

object City {
  private val rnd = new java.security.SecureRandom()

  /**
    * Create a City instance with coordinate between -1.0 and 1.0
    *
    * @return a new City instance.
    */
  def random() = City(-1.0 + 2.0 * rnd.nextDouble, -1.0 + 2.0 * rnd.nextDouble)
}
