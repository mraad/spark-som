package com.esri

/**
  * Class encapsulating an iteration learning parameters.
  *
  * @param radius the iteration radius.
  * @param alpha  the iteration learning rate.
  */
case class Parameters(val radius: Double, val alpha: Double)

object Parameters extends Serializable {
  /**
    * Create a new Parameters instance.
    *
    * @param iter          the current iteration
    * @param numIterations the max number of iterations.
    * @param size          the SOM size.
    * @param alpha         the initial learning rate.
    * @return a new Parameters instance.
    */
  def apply(iter: Int, numIterations: Int, size: Int, alpha: Double): Parameters = {

    val mapRadius = size * 0.5
    val deno = -1.0 / numIterations
    val iterConst = Math.log(mapRadius) * deno

    Parameters(mapRadius * Math.exp(iter * iterConst), alpha * Math.exp(iter * deno))
  }
}
