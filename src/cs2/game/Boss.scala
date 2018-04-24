package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

object Boss {
  
  var greenLaser = new Image("file:CS2GreenLaser.png")
  
}

/**
 * An enemy representation for a simple game based on sprites. Handles all
 *  information regarding the enemy's position, movements, and abilities.
 *
 *  @param pic the image representing the enemy
 *  @param initPos the initial position of the '''center''' of the enemy
 *  @param bulletPic the image of the bullets fired by this enemy
 */
class Boss(pic: Image, initPos: Vec2, private val bulletPic: Image)
  extends Sprite(pic, initPos) with ShootsBullets {

  /**
   * creates a new Bullet instance beginning from this Enemy, with an appropriate velocity
   *
   *  @return Bullet - the newly created Bullet object that was fired
   */

  def shoot(): Bullet = {

    val vel = new Vec2(0, 6)
    var position = new Vec2(pos.x + 20, pos.y)

    new Bullet(Boss.greenLaser, position, vel)

  }
   override def clone(): Boss = {
     
     var bs = new Boss(img,pos.clone(),Boss.greenLaser)
	   bs.pos = pos.clone()
	   bs
     
   }
  
}