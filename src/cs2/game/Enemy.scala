package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

/**
 * An enemy representation for a simple game based on sprites. Handles all
 *  information regarding the enemy's position, movements, and abilities.
 *
 *  @param pic the image representing the enemy
 *  @param initPos the initial position of the '''center''' of the enemy
 *  @param bulletPic the image of the bullets fired by this enemy
 */
class Enemy(pic: Image, initPos: Vec2, private val bulletPic: Image)
  extends Sprite(pic, initPos) with ShootsBullets {

  /**
   * creates a new Bullet instance beginning from this Enemy, with an appropriate velocity
   *
   *  @return Bullet - the newly created Bullet object that was fired
   */

  var theta = 0.0
  var counter = 0
  var deltaX = 3

  def beDynamic() {
    
    counter += 1
    theta += math.Pi / 32
    
    
    if (counter > 100) {
      
      deltaX = -deltaX
      counter = 0
      
    }
    
    println(counter)
    
    var direction = new Vec2(deltaX, math.cos(theta))
    move(direction)
    
  }

  def shoot(): Bullet = {

    val pic = new Image("file:CS2GreenLaser.png")
    val vel = new Vec2(0, 8)
    var position = new Vec2(pos.x + 20, pos.y)

    new Bullet(pic, position, vel)

  }
}