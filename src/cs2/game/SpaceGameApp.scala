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
object Images {

  val pPic = new Image("file:CS2Tesla.png")
  val pRPic = new Image("file:CS2TeslaRight.png")
  val pLPic = new Image("file:CS2TeslaLeft.png")
  val bPic = new Image("file:CS2RedLaser.png")
  val bossPic = new Image("file:AlienBoss.gif")
  val eBPic = new Image("file:CS2GreenLaser.png")

  val pVel = new Vec2(0, 5)

  val explosionImage1 = new Image("file:CSExplosion1.png")
  val explosionImage2 = new Image("file:CSExplosion2.png")
  val explosionImage3 = new Image("file:CSExplosion3.png")
  val explosionImage4 = new Image("file:CSExplosion4.png")

  val Tesla1H = new Image("file:CS2Tesla1H.png")
  val Tesla1HR = new Image("file:CS2Tesla1HR.png")
  val Tesla1HL = new Image("file:CS2Tesla1HL.png")
  val Tesla2H = new Image("file:CS2Tesla2H.png")
  val Tesla2HR = new Image("file:CS2Tesla2HR.png")
  val Tesla2HL = new Image("file:CS2Tesla2HL.png")
  val Tesla3H = new Image("file:CS2Tesla3H.png")
  val Tesla3HR = new Image("file:CS2Tesla3HR.png")
  val Tesla3HL = new Image("file:CS2Tesla3HL.png")
  
  val BossH1 = new Image("file:BossEnemyH1.png")
  val BossH2 = new Image("file:BossEnemyH2.png")
  val BossH3 = new Image("file:BossEnemyH3.png")

}

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

      val bossInitPos = new Vec2(center, -200)
      val pInitPos = new Vec2(center, cHeight - 165)

      var constellation = new Constellation(10, 10)

      var enemySwarm = new EnemySwarm(6, 4)
      var player = new Player(Images.pPic, pInitPos, Images.bPic)
      var bossEnemy = new Boss(Images.bossPic, bossInitPos, Images.eBPic)

      val playerW = 40
      val playerH = 65

      val enemyW = 40
      val enemyH = 40
      
      val bossW = 80
      val bossH = 80

      val bulletW = 4
      val bulletH = 10

      val starW = 2
      val starH = 2

      var keyEvents = collection.mutable.Set.empty[String]
      var gameStates = collection.mutable.Stack.empty[GameState]

      var pBullets = collection.mutable.ListBuffer.empty[Bullet]
      var eBullets = collection.mutable.ListBuffer.empty[Bullet]
      var explosions = collection.mutable.ListBuffer.empty[Explosion]

      var startGame = true
      var playerLives = 3
      var bossLives = 3
      var score = 0
      var swarmsKilled = 0
      var running = true
      var endCount = 0
      var showBoss = false 

      var explosionCounter = 0

      gc.fillText("Score: " + score, 32, 32)

      displayStart()

      canvas.onKeyPressed = (event: KeyEvent) => {

        startGame = false

        keyEvents += event.code.toString()

        if (keyEvents("P") == true) {

          timer.start

        }

        if (keyEvents("Q") == true) {

          System.exit(1)

        }

        if (keyEvents("SPACE") == true) {

          shootCount += 1

          if (shootCount <= 1 && playerLives >= 0) {

            pBullets += player.shoot()
            if (showBoss == true) {
            eBullets += bossEnemy.shoot()
            }
          }
        }
      }

      canvas.onKeyReleased = (event: KeyEvent) => {

        keyEvents -= event.code.toString()

      }

      canvas.requestFocus()

      var prevTime: Long = 0

      var shootCount: Int = 0

      val timer = AnimationTimer(t => {

        //Save Game States

        if (keyEvents("R") == true) {

          restorePreviousState()

        } else {

          if ((score > 0) && (score % 24 == 0)) {
            
            showBoss = true
            bossEnemy.moveTo(new Vec2(center, cHeight-player.pos.y))
            
            
          }


          for (x <- 0 until 10) {
            for (y <- 0 until 10) {

              var star = constellation.grid(x)(y)

              if (star.pos.y >= 900) {

                star.moveTo(new Vec2(star.pos.x, 0))

              }
            }
          }

          if (playerLives == 2) {

            player.img = Images.Tesla1H

          }

          if (playerLives == 1) {

            player.img = Images.Tesla2H

          }

          if (playerLives == 0) {

            player.img = Images.Tesla3H

          }

          if (keyEvents("LEFT") == true && playerLives >= 0) {
            if (player.pos.x >= 10) {
              player.moveLeft()
              player.img = Images.pLPic
              if (playerLives == 2) {

                player.img = Images.Tesla1HL

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2HL

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3HL

              }

              bossEnemy.move(new Vec2(6, 0))

            }
          }

          if (keyEvents("RIGHT") == true) {
            if (player.pos.x <= (cWidth.toDouble - 45) && playerLives >= 0) {
              player.moveRight()
              player.img = Images.pRPic
              if (playerLives == 2) {

                player.img = Images.Tesla1HR

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2HR

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3HR

              }

              bossEnemy.move(new Vec2(-6, 0))

            }
          }

          if (keyEvents("UP") == true && playerLives >= 0) {
            if (player.pos.y >= 10) {
              player.moveUp()
              player.img = Images.pPic

              if (playerLives == 2) {

                player.img = Images.Tesla1H

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2H

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3H

              }

            }
          }

          if (keyEvents("DOWN") == true && playerLives >= 0) {
            if (player.pos.y <= cHeight.toDouble - 165) {
              player.moveDown()
              player.img = Images.pPic

              if (playerLives == 2) {

                player.img = Images.Tesla1H

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2H

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3H
              }

            }
          }

          if (keyEvents("A") == true && playerLives >= 0) {
            if (player.pos.x >= 10) {
              player.moveLeft()
              player.img = Images.pLPic
              if (playerLives == 2) {

                player.img = Images.Tesla1HL

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2HL

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3HL

              }

            }
          }

          if (keyEvents("D") == true && playerLives >= 0) {
            if (player.pos.x <= (cWidth.toDouble - 45)) {
              player.moveRight()
              player.img = Images.pRPic
              if (playerLives == 2) {

                player.img = Images.Tesla1HR

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2HR

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3HR

              }

            }
          }

          if (keyEvents("W") == true && playerLives >= 0) {
            if (player.pos.y >= 10) {
              player.moveUp()
              player.img = Images.pPic
              if (playerLives == 2) {

                player.img = Images.Tesla1H

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2H

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3H

              }

            }
          }

          if (keyEvents("S") == true && playerLives >= 0) {
            if (player.pos.y <= cHeight.toDouble - 165) {
              player.moveDown()
              player.img = Images.pPic
              if (playerLives == 2) {

                player.img = Images.Tesla1H

              }

              if (playerLives == 1) {

                player.img = Images.Tesla2H

              }

              if (playerLives == 0) {

                player.img = Images.Tesla3H

              }

            }
          }

          if (keyEvents("P") == true) {

            score = 0
            playerLives = 3
            enemySwarm.resetGrid()
            player.moveTo(new Vec2(center, cHeight - 165))
            player.img = Images.pPic
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

                bullet.timeStep()

                if (bullet.pos.y <= 0) {

                  pBullets -= bullet

                }

                enemy.intersects(enemyW, enemyH, bulletW, bulletH, bullet.pos)

                if (enemy.intersection == true) {

                  pBullets -= bullet
                  explosions += new Explosion(Images.explosionImage1, enemy.pos.clone(), true)

                  score += 1

                  enemy.explode()

                }

                bossEnemy.intersects(bossW, bossH, bulletW, bulletH, bullet.pos)
                
                if (bossEnemy.intersection == true) {

                  pBullets -= bullet
                  score += 5
                  bossLives -= 1

                  explosions += new Explosion(Images.explosionImage1, bossEnemy.pos.clone(), true)
                  
                  if (bossLives == 2) {
                    
                    bossEnemy.img = Images.BossH1
                    
                  } else if (bossLives == 1) {
                    
                    bossEnemy.img = Images.BossH2
                    
                  } else if (bossLives == 0) {
                    
                    bossEnemy.img = Images.BossH3
                    
                  }
                  
                  if (bossLives < 0) {

                    showBoss = false
                    bossEnemy.moveTo(new Vec2(center, -200))
                    enemySwarm.resetGrid()
                    
                  }

                }

              }

              // Player Intersections
              player.intersects(playerW, playerH, enemyW, enemyH, enemy.pos)

              if (player.intersection == true && playerLives >= 0) {

                explosions += new Explosion(Images.explosionImage1, player.pos.clone(), true)
                playerLives -= 1

                player.moveTo(new Vec2(center, cHeight - 165))
                if (playerLives <= 0) {

                  player.img = Images.explosionImage1

                }

              }
            }
          }

          for (bullet <- eBullets) {

            bullet.timeStep()
            player.intersects(playerW, playerH, bulletW, bulletH, bullet.pos)

            if (bullet.pos.y >= cHeight) {

              eBullets -= bullet

            }

            if (player.intersection == true) {

              explosions += new Explosion(Images.explosionImage1, player.pos.clone(), true)

              player.moveTo(new Vec2(center, cHeight - 165))

              eBullets -= bullet

              playerLives -= 1
              println("playerLives:" + playerLives)

            }
          }
          
          //Boss Intersections 
          

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

          //Explosions
          for (explosion <- explosions) {

            explosion.timeStep()
            if (explosion.exploding == false) {

              explosions -= explosion
            }

          }

          constellation.beDynamic()
          enemySwarm.beDynamic()

          if (t - prevTime > 1e9 / 600) {

            storeGameStates()

          }

        }

        displayScoreBoard()

        for (bullet <- pBullets) {

          bullet.display(gc, bulletW, bulletH)

        }

        for (bullet <- eBullets) {

          bullet.display(gc, bulletW, bulletH)

        }

        for (explosion <- explosions) {

          explosion.display(gc, 30, 30)

        }

        if (playerLives <= -1) {
          //Game Over

          score = 0
          playerLives = -1
          running = false
          endCount += 1
          if (endCount > 30) {
            displayEnd()
          }

        }

      })

      def restorePreviousState() {

        var lastState = gameStates.pop()

        player = lastState.playerState
        bossEnemy = lastState.bossState
        startGame = lastState.runState
        playerLives = lastState.playerLivesState

        //Stars
        constellation = lastState.stars

        //Enemies
        enemySwarm = lastState.enemies

        eBullets = lastState.enemyBullets
        pBullets = lastState.playerBullets
        explosions = lastState.explosions

        score = lastState.scoreState
        running = lastState.runState
        startGame = lastState.startState
        playerLives = lastState.playerLivesState
        bossLives = lastState.bossLivesState

      }

      def storeGameStates() {

        var pBulletC = pBullets.clone()
        for (x <- 0 to pBulletC.length - 1) {

          pBulletC(x) = pBullets(x).clone()

        }
        var eBulletC = eBullets.clone()
        for (x <- 0 to eBulletC.length - 1) {

          eBulletC(x) = eBullets(x).clone()

        }
        var explosionC = explosions.clone()
        for (x <- 0 to explosionC.length - 1) {

          explosionC(x) = explosions(x).clone()

        }

        var currentState = new GameState(startGame, running, score, playerLives, bossLives, player.clone(), bossEnemy.clone(), enemySwarm.clone(), pBulletC, eBulletC, constellation.clone(), explosionC)
        gameStates.push(currentState)

      }

      def displayScoreBoard() {

        gc.fill = Color.Black
        gc.fillRect(0, 0, 1000, 900)
        constellation.display(gc)
        player.display(gc, playerW, playerH)
        bossEnemy.display(gc, bossW, bossH)
        enemySwarm.display(gc)
        gc.fill = Color.White
        gc.font = scalafx.scene.text.Font.font(50.0)
        gc.fillText("Lives: " + playerLives, 20, 60)
        gc.fillText("Score: " + score, 20, 110)

      }

      def displayStart() {

        endCount = 0
        gc.fill = Color.Black
        gc.fillRect(0, 0, 1000, 900)
        gc.fill = Color.Green
        gc.font = scalafx.scene.text.Font.font(50.0)
        gc.fillText("SpaceX Tesla Roadster VS The Martians", 20, 400)
        gc.font = scalafx.scene.text.Font.font(40.0)
        gc.fillText("Press the 'P' Key to Play!", 260, 450)
        gc.fillText("Press the 'Q' Key to Quit", 220, 500)

      }

      def displayEnd() {

        gc.fill = Color.Black
        gc.fillRect(0, 0, 1000, 900)
        gc.fill = Color.Red
        gc.font = scalafx.scene.text.Font.font(60.0)
        gc.fillText("Game Over", 340, 400)
        gc.font = scalafx.scene.text.Font.font(40.0)
        gc.fillText("Press the 'P' Key to Play Again!", 220, 450)
        gc.fillText("Press the 'Q' Key to Quit", 220, 500)

      }
    }
  }
}