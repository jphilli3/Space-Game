package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

class Star(pic:Image, initPos:Vec2, private var vel:Vec2) extends Sprite(pic, initPos) {

  /** advances the position of the Bullet over a single time step
   * 
   *  @return none/Unit
   */
  val start = 1
    val end   = 5
    var rnd = new scala.util.Random
    var num = start + rnd.nextInt( (end - start) + 1 ).toInt
    
    var direction = new Vec2(0,num)
 
  def beDynamic() {
    
    move(direction)
    
  }
	
	override def clone():Star = {
	  
	  var str = new Star(img,pos.clone(),direction)
	  str.direction = direction
	  str
	  
	}
}