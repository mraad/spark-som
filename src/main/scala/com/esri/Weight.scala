package com.esri

/**
  * A training or input weight as weight. All component should be [0.0 --> 1.0].
  *
  * @param r the color red component.
  * @param g the color green component.
  * @param b the color blue component.
  */
case class Weight(r: Double, g: Double, b: Double) extends Ordered[Weight] {

  /**
    * Add a given weight to this.
    *
    * @param that the weight to add.
    * @return a new Weight instance where the components are added together.
    */
  def +(that: Weight) = {
    Weight(this.r + that.r, this.g + that.g, this.b + that.b)
  }

  /**
    * Divide the components of this weight by given divisor.
    *
    * @param nume the divisor.
    * @return a new Weight instance with adjusted component value.
    */
  def /(nume: Double) = {
    Weight(this.r / nume, this.g / nume, this.b / nume)
  }

  /**
    * Normalize each component to a value between 0 and 255 and pack as integer in the form RRGGBB.
    *
    * @return RRGGBB.
    */
  def rgb() = {
    val ir = (r * 255.0).toInt
    val ig = (g * 255.0).toInt
    val ib = (b * 255.0).toInt
    (ir << 16) | (ig << 8) | ib
  }

  /**
    * Euclidean distance between this color and a given color.
    *
    * @param that a given color.
    * @return Euclidean distance between this color and a given color.
    */
  def dist(that: Weight) = {
    // TODO implement RADIAL distance
    val dr = that.r - this.r
    val dg = that.g - this.g
    val db = that.b - this.b
    Math.sqrt(dr * dr + dg * dg + db * db)
  }

  /**
    * Adjust this weight to "move" to given target weight.
    * The movement is based on the difference between the weights proportional to the learning rate (alpha) and the distance influence (theta).
    *
    * @param that  the target weight.
    * @param alpha the learning rate.
    * @param theta the distance influence.
    * @return new adjusted Weight instance.
    */
  def train(that: Weight, alpha: Double, theta: Double) = {

    def train(w: Double, t: Double) = w + alpha * theta * (t - w)

    Weight(train(this.r, that.r), train(this.g, that.g), train(this.b, that.b))
  }

  /**
    * Compare this weight to a another weight.
    *
    * @param that the other weight to compare to
    * @return 0 if the same, -1 if less, 1 if greater.
    */
  override def compare(that: Weight): Int = {
    if (this.r < that.r) -1
    else if (this.r > that.r) 1
    else if (this.g < that.g) -1
    else if (this.g > that.g) 1
    else if (this.b < that.b) -1
    else if (this.b > that.b) 1
    else 0
  }
}

object Weight {
  private val rnd = new java.security.SecureRandom()

  /**
    * @return a new instance with random weight values.
    */
  def random() = new Weight(rnd.nextDouble, rnd.nextDouble, rnd.nextDouble)
}
