package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

/** Representation of a bullet/projectile for a simple game based on sprites.
 *  Handles all information regarding a bullet's position, movements, and 
 *  abilities.
 *  
 *  @param pic the image representing the bullet
 *  @param initPos the initial position of the '''center''' of the bullet
 *  @param vel the initial velocity of the bullet
 */
class PowerUp(pic:Image, initPos:Vec2, private var species:Int, private var vel:Vec2) extends Sprite(pic, initPos) {

  /** advances the position of the Bullet over a single time step
   * 
   *  @return none/Unit
   */
  
  var velo = vel
  var kind = species
  

	def timeStep() {
	  
	  move(vel)
	  
	}
	
	override def clone(): PowerUp = {
	  
	  var pwr = new PowerUp(img,pos.clone(),species,velo.clone())
	  pwr.vel = velo.clone()
	  pwr.species = kind
	  pwr
	  
	}
	
}