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

/** main object that initiates the execution of the game, including construction
 *  of the window.
 *  Will create the stage, scene, and canvas to draw upon. Will likely contain or
 *  refer to an AnimationTimer to control the flow of the game.
 */
object SpaceGameApp extends JFXApp {
   stage = new JFXApp.PrimaryStage {
     
     title = "SpaceX Tesla Roadster VS The Aliens"
      
     scene = new Scene(500,500) {
         
         val canvas = new Canvas(500,500) 
        
         content = canvas
         
         val gc = canvas.graphicsContext2D

         val pPic = new Image("file:CS2Tesla.png")
         val bPic = new Image("file:CS2RedLaser.png")
         val pInitPos = new Vec2(250,425 )
         val pVel = new Vec2(0,10)
         val player = new Player(pPic, pInitPos, bPic)
         val enemySwarm = new EnemySwarm(6,4)
     
         player.display(gc, 40, 65)
      
          canvas.onKeyPressed = (event:KeyEvent) => {
        
            if (event.code == KeyCode.Left) {
            
              player.moveLeft()
          
            } 
            
            if (event.code == KeyCode.Right) {
              
              player.moveRight()
              
            }
            
            if (event.code == KeyCode.Space) {
              
              bullets ::= player.shoot()
              
            }
          }
          
          canvas.requestFocus()
          
           var bullets:List[Bullet] = Nil
           
           var prevTime:Long = 0
            
           val timer = AnimationTimer (t => {
             
            gc.fillRect(0, 0, 500, 500)
            player.display(gc,40,65)
            enemySwarm.display(gc)
            
            if (t-prevTime > 1e9/2) {
           
            prevTime = t
            enemySwarm.shoot()
            bullets ::= enemySwarm.shoot()
            
            }
           
            for (bullet <- bullets) {
              bullet.display(gc, 4, 10)
              bullet.timeStep()
            }
          })
          
          timer.start
    }
  } 
}