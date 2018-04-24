package cs2.game

import scalafx.scene.image.Image
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext

object Constell {
  
  var star = new Image("file:CS2Star.png")
  
}

class Constellation(private val nRows: Int, private val nCols: Int) {
  
  /**
   * method to display all Enemy objects contained within this EnemySwarm
   *
   *  @param g - the GraphicsContext to draw into
   *  @return none/Unit
   */

  val grid = Array.ofDim[Star](nRows, nCols)

  for (x <- 0 until nRows) {
    for (y <- 0 until nCols) {

      val vel = new Vec2(0, 8)
      
      var randX = math.random()/0.05
      var randY = math.random()/0.05
      
      if (y%2 != 0) {
      
      val initPos = new Vec2(randX * 90, randY * 50)
      grid(x)(y) = new Star(Constell.star, initPos, vel)
      
      } else {
        
      val initPos = new Vec2(randX * 90+20, randY * 50)
      grid(x)(y) = new Star(Constell.star, initPos, vel)
        
      }
      
      
      
    }
  }

  def resetGrid() {

    for (x <- 0 until nRows) {
      for (y <- 0 until nCols) {

        val initPos = new Vec2(x * 90 + 250, y * 50 + 50)

        grid(x)(y).moveTo(initPos)

      }
    }
  }

  def display(gc: GraphicsContext) {

    for (x <- 0 until nRows) {
      for (y <- 0 until nCols) {

        var star =  grid(x)(y)
        
        if (y%5 == 0) {
        
        star.display(gc, 3, 3)
    
        } else if (y%6 == 0) {
          
        star.display(gc,1,1)
        
        }else {
          
          star.display(gc, 6, 6)
          
        }

      }
    }
  }
  
  def beDynamic() {

    for (x <- 0 until nRows) {
      for (y <- 0 until nCols) {

        var star = grid(x)(y)
        star.beDynamic()
        
      }
    }
  }
  
  override def clone(): Constellation = {
    
    var constl = new Constellation(nRows, nCols)
    for (x <- 0 until nRows) {
      for (y <- 0 until nCols) {
        
        constl.grid(x)(y) = grid(x)(y).clone()
        
        
      }
    }
    constl
    
  }
  
}