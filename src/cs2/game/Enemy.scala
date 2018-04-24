package cs2.game

import scalafx.scene.image.Image
import scalafx.animation.AnimationTimer
import cs2.util.Vec2

/**
 * An enemy representation for a simple game based on sprites. Handles all
 *  information regarding the enemy's position, movements, and abilities.
 *
 *  @param pic the image representing the enemy
 *  @param initPos the initial position of the '''center''' of the enemy
 *  @param bulletPic the image of the bullets fired by this enemy
 */

object Enemy {

  val explosionImage1 = new Image("file:CSExplosion1.png")
  val explosionImage2 = new Image("file:CSExplosion2.png")
  val explosionImage3 = new Image("file:CSExplosion3.png")
  val explosionImage4 = new Image("file:CSExplosion4.png")
  
  val greenLaser = new Image("file:CS2GreenLaser.png")

}

class Enemy(pic: Image, initPos: Vec2, private val bulletPic: Image)
  extends Sprite(pic, initPos) with ShootsBullets {

  /**
   * creates a new Bullet instance beginning from this Enemy, with an appropriate velocity
   *
   *  @return Bullet - the newly created Bullet object that was fired
   */

  var theta = 0.0
  var counter = 0
  var deltaX = 3.0
  var explosionCounter = 0
  var exploded = false
  var direction = new Vec2(deltaX, math.cos(theta))

  val explosionArray = Array(Enemy.explosionImage1, Enemy.explosionImage2, Enemy.explosionImage3, Enemy.explosionImage4)

  def beDynamic() {

    counter += 1
    theta += math.Pi / 32

    if (counter > 100) {

      deltaX = -deltaX
      counter = 0

    }

    direction = new Vec2(deltaX, math.cos(theta))
    move(direction)

  }

  def shoot(): Bullet = {

    val pic = Enemy.greenLaser
    val vel = new Vec2(0, 8)
    var position = new Vec2(pos.x + 20, pos.y)

    new Bullet(pic, position, vel)

  }

  def explode() {

    val pic = Enemy.explosionImage1
    var position = new Vec2(pos.x, pos.y)

    new Explosion(pic, position, true)
    pos = new Vec2(-100, -100)

  }

  override def clone(): Enemy = {

    var direction = new Vec2(deltaX, math.cos(theta))

    var en = new Enemy(img, pos.clone(), Enemy.greenLaser)
    en.direction = direction.clone()
    en

  }

}