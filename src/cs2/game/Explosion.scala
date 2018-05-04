package cs2.game

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import cs2.util.Vec2
import scalafx.animation.AnimationTimer
import scalafx.scene.paint.Color
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scalafx.scene.image.Image
import scalafx.scene.text.Text
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.geometry.Insets
import scalafx.scene.shape.Rectangle
import cs2.util.Vec2

object Explosion {
  
  val explosionImage1 = new Image("file:CSExplosion1.png")
  val explosionImage2 = new Image("file:CSExplosion2.png")
  val explosionImage3 = new Image("file:CSExplosion3.png")
  val explosionImage4 = new Image("file:CSExplosion4.png")
  
}


class Explosion(pic: Image, initPos: Vec2, explode: Boolean)
  extends Sprite(pic, initPos) {
    
  

  
  var step = 0
  var exploding = explode
  
  def timeStep() {
      
      step += 1 
      
     if (exploding == true) {
        if (step <= 5) {
    img = Explosion.explosionImage1 
    } else if (step > 5 && step <= 10) {
    img = Explosion.explosionImage2
    } else if (step > 10 && step <= 15) {
    img = Explosion.explosionImage3
    } else if (step > 20 && step <= 25) {
    img = Explosion.explosionImage4
    exploding = false
    }
    }
  }
  
  override def clone(): Explosion = {
    
    var ex = new Explosion(Explosion.explosionImage1, pos.clone(), true)
	  ex.step = step
	  ex.exploding = exploding
	  ex
    
  }
  
}