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

/**
 * main object that initiates the execution of the game, including construction
 *  of the window.
 *  Will create the stage, scene, and canvas to draw upon. Will likely contain or
 *  refer to an AnimationTimer to control the flow of the game.
 */
object SpaceGameApp extends JFXApp {
  stage = new JFXApp.PrimaryStage {

    title = "SpaceX Tesla Roadster VS The Martians"

    scene = new Scene(1000, 900) {
      

      val canvas = new Canvas(1000, 900)
      
      content = canvas
       
      val gc = canvas.graphicsContext2D

      val cWidth = gc.canvas.width.toDouble
      val cHeight = gc.canvas.height.toDouble
      val center = (gc.canvas.width / 2).toDouble

      val pPic = new Image("file:CS2Tesla.png")
      val bPic = new Image("file:CS2RedLaser.png")
      val pInitPos = new Vec2(center, cHeight - 165)
      val pVel = new Vec2(0, 5)
     
      val player = new Player(pPic, pInitPos, bPic)
      val enemySwarm = new EnemySwarm(6, 4)

      val playerW = 40
      val playerH = 65

      val enemyW = 40
      val enemyH = 40

      val bulletW = 4
      val bulletH = 10

      var keyEvents = collection.mutable.Set.empty[String]

      var lives = 3
      var score = 0
      var running = true
      
      gc.fillText("Score: " + score, 32, 32)
      
      
      player.display(gc, playerW, playerH)

      canvas.onKeyPressed = (event: KeyEvent) => {

        keyEvents += event.code.toString()

        if (keyEvents("SPACE") == true) {

          shootCount += 1

          if (shootCount <= 1) {

            pBullets += player.shoot()

          }
        }
      }

      canvas.onKeyReleased = (event: KeyEvent) => {

        keyEvents -= event.code.toString()

      }

      canvas.requestFocus()

      var pBullets = collection.mutable.ListBuffer.empty[Bullet]
      var eBullets = collection.mutable.ListBuffer.empty[Bullet]

      var prevTime: Long = 0

      var shootCount: Int = 0

      val timer = AnimationTimer(t => {

        if (score % 24 == 0) {

          enemySwarm.resetGrid

        }
        
        gc.fill = Color.Black
        gc.fillRect(0, 0, 1000, 900)
        player.display(gc, 40, 65)
        enemySwarm.display(gc)
        enemySwarm.beDynamic()
        gc.fill = Color.White
        gc.font = scalafx.scene.text.Font.font(50.0)
        gc.fillText("Lives: "+lives,20,60)
        gc.fillText("Score: "+score,20,110)
        
        if (keyEvents("LEFT") == true) {

          if (player.pos.x >= 10) {
            player.moveLeft()

          }
        }

        if (keyEvents("RIGHT") == true) {
          if (player.pos.x <= (cWidth.toDouble - 45)) {
            player.moveRight()

          }
        }

        if (keyEvents("UP") == true) {
          if (player.pos.y >= 10) {
            player.moveUp()

          }
        }

        if (keyEvents("DOWN") == true) {
          if (player.pos.y <= cHeight.toDouble - 165) {
            player.moveDown()

          }
        }
        
        if (keyEvents("R") == true) {
           
          score = 0 
          lives = 3
          enemySwarm.resetGrid()
          player.moveTo(new Vec2(center, cHeight - 165))
          eBullets.clear()
          pBullets.clear()
          
        }

        //Reset Player Shoot Count
        if (t - prevTime > 1e9 / 2) {

          shootCount = 0

        }

        //Enemy Shoot Delay
        if (t - prevTime > 1e9 / 2) {

          prevTime = t
          enemySwarm.shoot()
          eBullets += enemySwarm.shoot()

        }

        //Enemy Swarm Intersections
        for (x <- 0 until 6) {
          for (y <- 0 until 4) {

            var enemy = enemySwarm.grid(x)(y)
            
            for (bullet <- pBullets) {
              bullet.display(gc, bulletW, bulletH)
              bullet.timeStep()

              if (bullet.pos.y <= 0) {

                pBullets -= bullet

              }

              enemy.intersects(enemyW, enemyH, bulletW, bulletH, bullet.pos)

              if (enemy.intersection == true) {

                pBullets -= bullet
                enemy.moveTo(new Vec2(-100, -100))
                score += 1

              }
            }

            // Player Intersections
            player.intersects(playerW, playerH, enemyW, enemyH, enemy.pos)

            if (player.intersection == true) {

              player.moveTo(new Vec2(center, cHeight - 165))

              lives -= 1

            }
          }
        }

        for (bullet <- eBullets) {
          bullet.display(gc, bulletW, bulletH)
          bullet.timeStep()
          player.intersects(playerW, playerH, bulletW, bulletH, bullet.pos)

          if (bullet.pos.y >= cHeight) {

            eBullets -= bullet

          }

          if (player.intersection == true) {

            player.moveTo(new Vec2(center, cHeight - 165))

            eBullets -= bullet

            lives -= 1
            println("lives:" + lives)

          }
        }

        //Bullet Intersections

        for (eBullet <- eBullets) {
          for (pBullet <- pBullets) {

            pBullet.intersects(bulletW, bulletH, bulletW, bulletH, eBullet.pos)
            if (pBullet.intersection == true) {

              pBullets -= pBullet
              eBullets -= eBullet

            }
          }
        }
        
          if (lives <= -1) {
          //Game Over
          score = 0
          lives = -1
          running = false
          gc.fill = Color.Black
          gc.fillRect(0,0,1000,900)
          gc.fill = Color.Red
          gc.font = scalafx.scene.text.Font.font(60.0)
          gc.fillText("Game Over",340,400)
          gc.font = scalafx.scene.text.Font.font(40.0)
          gc.fillText("Press the 'R' Key to Play Again!",220,450)
          
        }
        
      })
      
       timer.start
      
    }
  }
}