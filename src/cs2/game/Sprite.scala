package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext

/** A graphical sprite object used for gaming or other visual displays
 *  
 *  @constructor create a new sprite based on the given image and initial location
 *  @param img the image used to display this sprite
 *  @param pos the initial position of the '''center''' of the sprite in 2D space
 */
abstract class Sprite (var img:Image, var pos:Vec2) {

  /** moves the sprite a relative amount based on a specified vector
   *  
   *  @param direction - an offset that the position of the sprite should be moved by
   *  @return none/Unit
   */
  
  var intersection: Boolean = false
  
 
  def move (direction:Vec2) { 
    pos += direction
  }
  
  /** moves the sprite to a specific location specified by a vector (not a relative movement)
   *  
   *  @param location - the new location for the sprite's position
   *  @return none/Unit
   */
  def moveTo (location:Vec2) {
    pos = location
  }
  
  /** Method to display the sprite at its current location in the specified Graphics2D context
   *  
   *  @param g - a GraphicsContext object capable of drawing the sprite
   *  @return none/Unit
   */
  def display (g:GraphicsContext,w:Double,h:Double) { 
   g.drawImage(img, pos.x, pos.y, w, h)
  }
  
  def intersects (widthA:Int, heightA: Int, widthB:Int, heightB:Int, location: Vec2) {
    if (!((this.pos.x+widthA < location.x) || (this.pos.x > location.x+widthB)) && !((this.pos.y+heightA < location.y) || (this.pos.y > location.y+heightB))) {
       intersection = true
    } else {
      intersection = false
    }
  }
  
}