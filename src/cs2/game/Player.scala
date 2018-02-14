package cs2.game

import scalafx.scene.image.Image
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import cs2.util.Vec2

/** The player representation for a simple game based on sprites. Handles all 
 *  information regarding the player's positions, movements, and abilities.
 *  
 *  @param avatar the image representing the player
 *  @param initPos the initial position of the '''center''' of the player
 *  @param bulletPic the image of the bullets fired by this player
 */
class Player(avatar:Image, initPos:Vec2, private val bulletPic:Image) 
             extends Sprite(avatar, initPos) with ShootsBullets {

  /** moves the player sprite one "step" to the left.  The amount of this 
   *  movement will likely need to be tweaked in order for the movement to feel 
   *  natural.
   *  
   *  @return none/Unit
   */
     def moveLeft() {
          
        var newPos = new Vec2(-10, 0)
             move(newPos)
          
     }
  
  /** moves the player sprite one "step" to the right (see note above)
   * 
   *  @return none/Unit
   */
     def moveRight() {
     
         var newPos = new Vec2(10, 0)
             move(newPos)
                  
     }
  
  /** creates a new Bullet instance beginning from the player, with an 
   *  appropriate velocity
   * 
   *  @return Bullet - the newly created Bullet object that was fired
   */
   def shoot():Bullet = { 
    
     val pic = new Image("file:CS2RedLaser.png")
     val vel = new Vec2(0,-5)
     val initPos = new Vec2(pos.x+18,pos.y)
    
     new Bullet(pic,initPos.clone(),vel)
    
   }           
}