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
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

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

  val batteryFull = new Image("file:CSFullCharge.png")
  val battery90 = new Image("file:CS90Charge.png")
  val battery80 = new Image("file:CS80Charge.png")
  val battery65 = new Image("file:CS65Charge.png")
  val battery50 = new Image("file:CS50Charge.png")
  val battery35 = new Image("file:CS35Charge.png")
  val battery20 = new Image("file:CS20Charge.png")
  val batteryEmpty = new Image("file:CS0Charge.png")

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
      val p1InitPos = new Vec2(center + 50, cHeight - 165)
      val p2InitPos = new Vec2(center - 50, cHeight - 165)

      var constellation = new Constellation(10, 10)

      var enemySwarm = new EnemySwarm(6, 4)
      var player = new Player(Images.pPic, pInitPos, Images.bPic)
      var playerOne = new Player(Images.pPic, p1InitPos, Images.bPic)
      var playerTwo = new Player(Images.pPic, p2InitPos, Images.bPic)
      var bossEnemy = new Boss(Images.bossPic, bossInitPos, Images.eBPic)
      var battery = new Battery(Images.batteryEmpty, new Vec2(50, 700))

      val playerW = 40
      val playerH = 65

      val player2W = 30
      val player2H = 55

      val enemyW = 40
      val enemyH = 40

      val bossW = 80
      val bossH = 80

      val bulletW = 4
      val bulletH = 10

      val laserW = 22
      val laserH = 400

      val starW = 2
      val starH = 2

      var keyEvents = collection.mutable.Set.empty[String]
      var gameStates = collection.mutable.Stack.empty[GameState]

      var pBullets = collection.mutable.ListBuffer.empty[Bullet]
      var eBullets = collection.mutable.ListBuffer.empty[Bullet]
      var BFLBullets = collection.mutable.ListBuffer.empty[Bullet]
      var powerUps = collection.mutable.ListBuffer.empty[PowerUp]
      var explosions = collection.mutable.ListBuffer.empty[Explosion]

      var startGame = true
      var playerLives = 3
      var player1Lives = 3
      var player2Lives = 3
      var bossLives = 3
      var score = 0
      var running = true
      var endCount = 0
      var showBoss = false
      var enemiesInGrid = 24

      var energy = 0
      var counter = 0
      var slow = false
      var speedCounter = 0

      var coop = false
      var p1ShootCount = 0
      var p2ShootCount = 0

      var existBFL = 0
      var existBFL2 = 0

      var explosionCounter = 0

      gc.fillText("Score: " + score, 32, 32)

      displayStart()

      canvas.onKeyPressed = (event: KeyEvent) => {

        startGame = false

        keyEvents += event.code.toString()

        if (keyEvents("P") == true) {

          coop = false
          timer.start()

        }

        if (keyEvents("Q") == true) {

          System.exit(1)

        }

        if (keyEvents("C") == true) {

          coop = true
          timer.start()

        }

        if (keyEvents("SPACE") == true) {

          if (coop == true) {

            p1ShootCount += 1

            if (p1ShootCount <= 1 && player1Lives >= 0) {

              playLaserAudio()

              pBullets += playerOne.shoot()
              if (showBoss == true) {

                playAlienLaserAudio()
                eBullets += bossEnemy.shoot()
              }
            }
          } else {

            shootCount += 1

            if (shootCount <= 1 && playerLives >= 0) {

              playLaserAudio()

              pBullets += player.shoot()
              if (showBoss == true) {
                eBullets += bossEnemy.shoot()
              }
            }
          }
        }

        if (keyEvents("X") == true) {

          if (coop == true) {

            p2ShootCount += 1

            if (p2ShootCount <= 1 && player2Lives >= 0) {

              playLaserAudio()

              pBullets += playerTwo.shoot()

            }
          }
        }
      }

      canvas.onKeyReleased = (event: KeyEvent) => {

        if (keyEvents("B") == true || keyEvents("Z") == true) {

          for (laser <- BFLBullets) {

            playLaserAudio()

            BFLBullets -= laser

          }

        }

        keyEvents -= event.code.toString()

      }

      canvas.requestFocus()

      var prevTime: Long = 0

      var shootCount: Int = 0

      val timer = AnimationTimer(t => {

        if (keyEvents("R") == true) {

          if (gameStates.length != 0) {

            restorePreviousState()

          } else {

            println("No more previous states")

          }

          //Save Game States

        } else {

          counter += 1

          //Energy Battery 100
          if (energy == 240) {

            battery.img = Images.batteryFull

          } else if (energy < 240 && energy > 200) {

            battery.img = Images.battery90

          } else if (energy < 200 && energy > 160) {

            battery.img = Images.battery80

          } else if (energy < 160 && energy > 120) {

            battery.img = Images.battery65

          } else if (energy < 120 && energy > 80) {

            battery.img = Images.battery50

          } else if (energy < 80 && energy > 40) {

            battery.img = Images.battery35

          } else if (energy < 40 && energy > 0) {

            battery.img = Images.battery20

          } else if (energy == 0) {

            battery.img = Images.batteryEmpty

          }

          //Drop Power Up
          if (counter % 317 == 0) {

            var powerUp = enemySwarm.dropPower()
            powerUps += powerUp

          }

          for (power <- powerUps) {

            power.timeStep()

            if (coop == true) {

              playerOne.intersectsPower(playerW, playerH, 30, 30, power.pos)
              playerTwo.intersectsPower(player2W, player2H, 30, 30, power.pos)

              if (playerOne.powerIntersection == true && power.kind == 0) {
                //Energy
                playPowerUpAudio()
                energy += 50
                powerUps -= power

              } else if (playerOne.powerIntersection == true && power.kind == 1) {
                //Life
                playPowerUpAudio()
                player1Lives += 1
                powerUps -= power

              } else if (playerOne.powerIntersection == true && power.kind == 2) {
                //Slow
                playPowerUpAudio()
                slow = true
                speedCounter += 200
                powerUps -= power

              }

              if (playerTwo.powerIntersection == true && power.kind == 0) {
                //Energy
                playPowerUpAudio()
                energy += 50
                powerUps -= power

              } else if (playerTwo.powerIntersection == true && power.kind == 1) {
                //Life
                playPowerUpAudio()
                player2Lives += 1
                powerUps -= power

              } else if (playerTwo.powerIntersection == true && power.kind == 2) {
                //Slow
                playPowerUpAudio()
                slow = true
                speedCounter += 500
                powerUps -= power

              }

            } else {

              player.intersectsPower(playerW, playerW, 30, 30, power.pos)

              if (player.powerIntersection == true && power.kind == 0) {
                //Energy
                playPowerUpAudio()
                energy += 50
                powerUps -= power

              } else if (player.powerIntersection == true && power.kind == 1) {
                //Life
                playPowerUpAudio()
                playerLives += 1
                powerUps -= power

              } else if (player.powerIntersection == true && power.kind == 2) {
                //Slow
                playPowerUpAudio()
                slow = true
                speedCounter += 500
                powerUps -= power

              }

            }

          }

          //BIG LASER
          if (keyEvents("B") == true) {
            if (coop == true) {

              val laserBFL = playerOne.shootBFL()

              if (energy > 0) {

                energy -= 4

                if (BFLBullets.length == 0) {

                  BFLBullets += laserBFL
                  playBFLAudio()

                  if (energy == 0) {

                    println("Remove")

                    for (laser <- BFLBullets) {

                      BFLBullets -= laser

                    }

                  }
                }
              }
            } else {

              val laserBFL = player.shootBFL()

              if (energy >= 0) {

                energy -= 4

                if (BFLBullets.length == 0) {

                  BFLBullets += laserBFL
                  playBFLAudio()

                }
              }

              if (energy < 0) {

                for (laser <- BFLBullets) {

                  BFLBullets -= laser

                }

              }
            }
          }

          //BIG LASER 2
          if (keyEvents("Z") == true) {
            if (coop == true) {

              val laserBFL2 = playerTwo.shootBFL()

              if (energy >= 0) {

                energy -= 4

                if (BFLBullets.length == 0) {

                }

                playBFLAudio()
                BFLBullets += laserBFL2

                if (energy == 0) {

                  println("Remove")

                  for (laser <- BFLBullets) {

                    BFLBullets -= laser

                  }
                }
              }
            } else {

            }

          }

          if ((score > 0) && (enemiesInGrid == 0)) {

            showBoss = true
            bossEnemy.moveTo(new Vec2(center, cHeight - player.pos.y))

          }

          for (x <- 0 until 10) {
            for (y <- 0 until 10) {

              var star = constellation.grid(x)(y)

              if (star.pos.y >= 900) {

                star.moveTo(new Vec2(star.pos.x, 0))

              }
            }
          }

          if (coop == true) {

            if (player1Lives == 2) {

              playerOne.img = Images.Tesla1H

            }

            if (player1Lives == 1) {

              playerOne.img = Images.Tesla2H

            }

            if (player1Lives == 0) {

              playerOne.img = Images.Tesla3H

            }

            if (player2Lives == 2) {

              playerTwo.img = Images.Tesla1H

            }

            if (player2Lives == 1) {

              playerTwo.img = Images.Tesla2H

            }

            if (player2Lives == 0) {

              playerTwo.img = Images.Tesla3H

            }

          } else {

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

          if (keyEvents("LEFT") == true && playerLives >= 0 && player1Lives >= 0) {
            if (coop == true) {

              if (playerOne.pos.x >= 10) {
                playerOne.moveLeft()
                playerOne.img = Images.pLPic
                if (player1Lives == 2) {

                  playerOne.img = Images.Tesla1HL

                }

                if (player1Lives == 1) {

                  playerOne.img = Images.Tesla2HL

                }

                if (player1Lives == 0) {

                  playerOne.img = Images.Tesla3HL

                }

                bossEnemy.move(new Vec2(6, 0))

              }

            } else {

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
          }

          if (keyEvents("RIGHT") == true) {
            if (coop == true) {

              if (playerOne.pos.x <= (cWidth.toDouble - 45) && playerLives >= 0 && player1Lives >= 0) {
                playerOne.moveRight()
                playerOne.img = Images.pRPic
                if (player1Lives == 2) {

                  playerOne.img = Images.Tesla1HR

                }

                if (player1Lives == 1) {

                  playerOne.img = Images.Tesla2HR

                }

                if (player1Lives == 0) {

                  playerOne.img = Images.Tesla3HR

                }

                bossEnemy.move(new Vec2(-6, 0))
              }

            } else {

              if (player.pos.x <= (cWidth.toDouble - 45) && playerLives >= 0 && player1Lives >= 0) {
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
          }

          if (keyEvents("UP") == true && playerLives >= 0 && player1Lives >= 0) {
            if (coop == true) {

              if (playerOne.pos.y >= 10) {
                playerOne.moveUp()
                playerOne.img = Images.pPic

                if (player1Lives == 2) {

                  playerOne.img = Images.Tesla1H

                }

                if (player1Lives == 1) {

                  playerOne.img = Images.Tesla2H

                }

                if (player1Lives == 0) {

                  playerOne.img = Images.Tesla3H

                }
              }

            } else {

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
          }

          if (keyEvents("DOWN") == true && playerLives >= 0 && player1Lives >= 0) {
            if (coop == true) {

              if (playerOne.pos.y <= cHeight.toDouble - 165) {
                playerOne.moveDown()
                playerOne.img = Images.pPic

                if (player1Lives == 2) {

                  playerOne.img = Images.Tesla1H

                }

                if (player1Lives == 1) {

                  playerOne.img = Images.Tesla2H

                }

                if (player1Lives == 0) {

                  playerOne.img = Images.Tesla3H
                }
              }

            } else {

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
          }

          if (keyEvents("A") == true && playerLives >= 0 && player2Lives >= 0) {
            if (coop == true) {

              if (playerTwo.pos.x >= 10) {
                playerTwo.moveLeft()
                playerTwo.img = Images.pLPic
                if (player2Lives == 2) {

                  playerTwo.img = Images.Tesla1HL

                }

                if (player2Lives == 1) {

                  playerTwo.img = Images.Tesla2HL

                }

                if (player2Lives == 0) {

                  playerTwo.img = Images.Tesla3HL

                }

              }

            } else {

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
          }

          if (keyEvents("D") == true && playerLives >= 0 && player2Lives >= 0) {
            if (coop == true) {

              if (playerTwo.pos.x <= (cWidth.toDouble - 45)) {
                playerTwo.moveRight()
                playerTwo.img = Images.pRPic
                if (player2Lives == 2) {

                  playerTwo.img = Images.Tesla1HR

                }

                if (player2Lives == 1) {

                  playerTwo.img = Images.Tesla2HR

                }

                if (player2Lives == 0) {

                  playerTwo.img = Images.Tesla3HR

                }
              }

            } else {

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
          }

          if (keyEvents("W") == true && playerLives >= 0 && player2Lives >= 0) {
            if (coop == true) {
              if (playerTwo.pos.y >= 10) {
                playerTwo.moveUp()
                playerTwo.img = Images.pPic
                if (player2Lives == 2) {

                  playerTwo.img = Images.Tesla1H

                }

                if (player2Lives == 1) {

                  playerTwo.img = Images.Tesla2H

                }

                if (player2Lives == 0) {

                  playerTwo.img = Images.Tesla3H

                }
              }

            } else {

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
          }

          if (keyEvents("S") == true && playerLives >= 0 && player2Lives >= 0) {
            if (coop == true) {

              if (playerTwo.pos.y <= cHeight.toDouble - 165) {
                playerTwo.moveDown()
                playerTwo.img = Images.pPic
                if (player2Lives == 2) {

                  playerTwo.img = Images.Tesla1H

                }

                if (player2Lives == 1) {

                  playerTwo.img = Images.Tesla2H

                }

                if (player2Lives == 0) {

                  playerTwo.img = Images.Tesla3H

                }
              }

            } else {

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
          }

          if (keyEvents("P") == true) {

            showBoss = false
            coop = false
            score = 0
            energy = 0
            playerLives = 3
            player1Lives = 3
            player2Lives = 3
            enemySwarm.resetGrid()
            enemiesInGrid = 24
            player.moveTo(new Vec2(center, cHeight - 165))
            player.img = Images.pPic
            eBullets.clear()
            pBullets.clear()

          }

          if (keyEvents("C") == true) {

            showBoss = false
            coop = true
            score = 0
            slow = false
            energy = 0
            playerLives = 3
            player1Lives = 3
            player2Lives = 3
            enemySwarm.resetGrid()
            enemiesInGrid = 24
            playerOne.moveTo(new Vec2(center + 50, cHeight - 165))
            playerTwo.moveTo(new Vec2(center - 50, cHeight - 165))
            playerOne.img = Images.pPic
            playerTwo.img = Images.pPic
            eBullets.clear()
            pBullets.clear()

          }

          //Reset Player Shoot Count
          if (t - prevTime > 1e9 / 2) {

            shootCount = 0
            p1ShootCount = 0
            p2ShootCount = 0

          }

          //Enemy Shoot Delay
          if (t - prevTime > 1e9 / 2) {

            prevTime = t

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

                  playExplosionAudio()
                  explosions += new Explosion(Images.explosionImage1, enemy.pos.clone(), true)

                  score += 1
                  if (energy < 240) {
                    energy += 10
                  }
                  enemiesInGrid -= 1

                  enemy.explode()

                }

                bossEnemy.intersects(bossW, bossH, bulletW, bulletH, bullet.pos)

                if (bossEnemy.intersection == true) {

                  pBullets -= bullet
                  score += 5
                  if (energy < 240) {
                    energy += 80
                  }
                  bossLives -= 1

                  playExplosionAudio()

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
                    enemiesInGrid = 24
                    bossEnemy.img = Images.bossPic
                    bossLives = 3

                  }

                }

              }

              //BFL Intersections

              for (laser <- BFLBullets) {

                bossEnemy.intersects(bossW, bossH, laserW, laserH, laser.pos)
                enemy.intersects(enemyW, enemyH, laserW, laserH, laser.pos)

                if (enemy.intersection == true) {

                  playExplosionAudio()

                  explosions += new Explosion(Images.explosionImage1, enemy.pos.clone(), true)

                  score += 1
                  if (energy < 240) {
                    energy += 10
                  }
                  enemiesInGrid -= 1

                  enemy.explode()

                }

                if (bossEnemy.intersection == true) {

                  score += 5
                  if (energy < 240) {
                    energy += 80
                  }
                  bossLives -= 1

                  playExplosionAudio()

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
                    enemiesInGrid = 24
                    bossEnemy.img = Images.bossPic
                    bossLives = 3

                  }

                }

              }

              // Player Intersections
              if (coop == true) {

                playerOne.intersects(playerW, playerH, enemyW, enemyH, enemy.pos)

                if (playerOne.intersection == true && player1Lives >= 0) {

                  playExplosionAudio()

                  explosions += new Explosion(Images.explosionImage1, playerOne.pos.clone(), true)
                  player1Lives -= 1

                  playerOne.moveTo(new Vec2(center, cHeight - 165))
                  if (player1Lives <= 0) {

                    playerOne.img = Images.explosionImage1

                  }
                }

                playerTwo.intersects(player2W, player2H, enemyW, enemyH, enemy.pos)

                if (playerTwo.intersection == true && player2Lives >= 0) {

                  playExplosionAudio()

                  explosions += new Explosion(Images.explosionImage1, playerTwo.pos.clone(), true)
                  player2Lives -= 1

                  playerTwo.moveTo(new Vec2(center, cHeight - 165))
                  if (player2Lives <= 0) {

                    playerTwo.img = Images.explosionImage1

                  }
                }

              } else {

                player.intersects(playerW, playerH, enemyW, enemyH, enemy.pos)

                if (player.intersection == true && playerLives >= 0) {

                  playExplosionAudio()

                  explosions += new Explosion(Images.explosionImage1, player.pos.clone(), true)
                  playerLives -= 1

                  player.moveTo(new Vec2(center, cHeight - 165))
                  if (playerLives <= 0) {

                    player.img = Images.explosionImage1

                  }
                }
              }
            }
          }

          for (bullet <- eBullets) {

            if (coop == true) {

              bullet.timeStep()
              playerOne.intersects(playerW, playerH, bulletW, bulletH, bullet.pos)
              playerTwo.intersects(player2W, player2H, bulletW, bulletH, bullet.pos)

              if (bullet.pos.y >= cHeight) {

                eBullets -= bullet

              }

              if (playerOne.intersection == true) {

                playExplosionAudio()

                explosions += new Explosion(Images.explosionImage1, playerOne.pos.clone(), true)

                playerOne.moveTo(new Vec2(center, cHeight - 165))

                eBullets -= bullet

                player1Lives -= 1
                println("playerLives:" + player1Lives)

              }

              if (playerTwo.intersection == true) {

                playExplosionAudio()

                explosions += new Explosion(Images.explosionImage1, playerTwo.pos.clone(), true)

                playerTwo.moveTo(new Vec2(center, cHeight - 165))

                eBullets -= bullet

                player2Lives -= 1
                println("playerLives:" + player2Lives)

              }

            } else {

              bullet.timeStep()
              player.intersects(playerW, playerH, bulletW, bulletH, bullet.pos)

              if (bullet.pos.y >= cHeight) {

                eBullets -= bullet

              }

              if (player.intersection == true) {

                playExplosionAudio()

                explosions += new Explosion(Images.explosionImage1, player.pos.clone(), true)

                player.moveTo(new Vec2(center, cHeight - 165))

                eBullets -= bullet

                playerLives -= 1
                println("playerLives:" + playerLives)

              }
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

          //Explosions
          for (explosion <- explosions) {

            explosion.timeStep()
            if (explosion.exploding == false) {

              explosions -= explosion
            }

          }

          //Enemy Speed

          if (speedCounter != 0) {

            speedCounter -= 1
            slow = true
            enemySwarm.beDynamic(slow)
            println(slow)

          } else if (speedCounter <= 10) {
            slow = false
            enemySwarm.beDynamic(slow)
            speedCounter = 0
            println(slow)

          }

          constellation.beDynamic()

          if (t - prevTime > 1e9 / 600) {

            storeGameStates()

          }

        }

        displayScoreBoard()

        for (bullet <- pBullets) {

          bullet.display(gc, bulletW, bulletH)

        }

        for (BFL <- BFLBullets) {

          BFL.display(gc, laserW, laserH)

        }

        for (bullet <- eBullets) {

          bullet.display(gc, bulletW, bulletH)

        }

        for (explosion <- explosions) {

          explosion.display(gc, 30, 30)

        }

        for (power <- powerUps) {

          power.display(gc, 30, 30)

        }

        if (playerLives <= -1 || (player1Lives <= -1 && player2Lives <= -1)) {
          //Game Over

          if (coop == true) {

            player1Lives = -1
            player2Lives = -1

          } else {

            playerLives = -1

          }

          energy = 0
          score = 0
          slow = false
          speedCounter = 0
          running = false
          coop = false
          endCount += 1
          showBoss = false
          bossEnemy.moveTo(new Vec2(center, -200))

          if (endCount > 30) {
            displayEnd()
          }

        }

      })

      def restorePreviousState() {

        playReverseAudio()

        var lastState = gameStates.pop()

        player = lastState.playerState
        playerOne = lastState.playerOneState
        playerTwo = lastState.playerTwoState
        bossEnemy = lastState.bossState
        startGame = lastState.runState

        //Stars
        constellation = lastState.stars

        //Enemies
        enemySwarm = lastState.enemies

        eBullets = lastState.enemyBullets
        pBullets = lastState.playerBullets
        BFLBullets = lastState.BFLBullets
        explosions = lastState.explosions
        powerUps = lastState.powerUps

        score = lastState.scoreState
        energy = lastState.energyState
        running = lastState.runState
        startGame = lastState.startState
        playerLives = lastState.playerLivesState
        player1Lives = lastState.player1LivesState
        player2Lives = lastState.player2LivesState
        bossLives = lastState.bossLivesState
        slow = lastState.speedState

      }

      def storeGameStates() {

        var pBulletC = pBullets.clone()
        for (x <- 0 to pBulletC.length - 1) {

          pBulletC(x) = pBullets(x).clone()

        }
        var BFLBulletC = BFLBullets.clone()
        for (x <- 0 to BFLBulletC.length - 1) {

          BFLBulletC(x) = BFLBullets(x).clone()

        }
        var eBulletC = eBullets.clone()
        for (x <- 0 to eBulletC.length - 1) {

          eBulletC(x) = eBullets(x).clone()

        }
        var explosionC = explosions.clone()
        for (x <- 0 to explosionC.length - 1) {

          explosionC(x) = explosions(x).clone()

        }

        var powerUpC = powerUps.clone()
        for (x <- 0 to powerUpC.length - 1) {

          powerUpC(x) = powerUps(x).clone()

        }

        var currentState = new GameState(startGame, running, score, energy, slow, playerLives, player1Lives, player2Lives, bossLives, player.clone(), playerOne.clone(), playerTwo.clone(), bossEnemy.clone(), enemySwarm.clone(), pBulletC, BFLBulletC, eBulletC, constellation.clone(), explosionC, powerUpC)
        gameStates.push(currentState)

      }

      def displayScoreBoard() {

        gc.fill = Color.Black
        gc.fillRect(0, 0, 1000, 900)
        constellation.display(gc)
        bossEnemy.display(gc, bossW, bossH)
        enemySwarm.display(gc)
        battery.display(gc, 100, 80)
        gc.fill = Color.White
        gc.font = scalafx.scene.text.Font.font(30.0)

        if (coop == true) {

          playerOne.display(gc, playerW, playerH)
          playerTwo.display(gc, player2W, player2H)
          gc.fillText("P1 Lives: " + player1Lives, 20, 60)
          gc.fillText("P2 Lives: " + player2Lives, 20, 110)
          gc.fillText("Score: " + score, 20, 160)

        } else {

          player.display(gc, playerW, playerH)
          gc.fillText("Lives: " + playerLives, 20, 60)
          gc.fillText("Score: " + score, 20, 110)

        }

      }

      def displayStart() {

        endCount = 0
        gc.fillRect(0, 0, 1000, 900)
        gc.drawImage(new Image("file:Mars-Sunrise.png"), 0, 0, 1000, 900)
        gc.fill = Color.LimeGreen
        gc.font = scalafx.scene.text.Font.font(50.0)
        gc.fillText("SpaceX Tesla Roadster VS The Martians", 20, 400)
        gc.font = scalafx.scene.text.Font.font(40.0)
        gc.fillText("Press the 'P' Key to Play!", 260, 450)
        gc.fillText("Press the 'C' Key for Co-Op", 240, 550)
        gc.fillText("Press the 'Q' Key to Quit", 260, 500)

      }

      def displayEnd() {
        timer.stop()
        gc.fillRect(0, 0, 1000, 900)
        gc.drawImage(new Image("file:Mars-Sunrise.png"), 0, 0, 1000, 900)
        gc.fill = Color.Red
        gc.font = scalafx.scene.text.Font.font(60.0)
        gc.fillText("Game Over", 340, 400)
        gc.font = scalafx.scene.text.Font.font(40.0)
        gc.fillText("Press the 'P' Key to Play Again!", 220, 450)
        gc.fillText("Press the 'C' Key for Co-Op", 240, 550)
        gc.fillText("Press the 'Q' Key to Quit", 260, 500)

      }

      def playLaserAudio() {

        var audioFile = "Laser-SoundBible.com-602495617.wav"
        var sound = new Media(new File(audioFile).toURI().toString());
        var mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

      }

      def playAlienLaserAudio() {

        var audioFile = "Flash-laser-03.wav"
        var sound = new Media(new File(audioFile).toURI().toString());
        var mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

      }

      def playBFLAudio() {

        var audioFile = "Laser-effector-02.wav"
        var sound = new Media(new File(audioFile).toURI().toString());
        var mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

      }

      def playExplosionAudio() {

        var audioFile = "Explosion+1.wav"
        var sound = new Media(new File(audioFile).toURI().toString());
        var mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

      }

      def playPowerUpAudio() {

        var audioFile = "Power-Up-KP-1879176533.wav"
        var sound = new Media(new File(audioFile).toURI().toString());
        var mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

      }

      def playReverseAudio() {

        var audioFile = "Time Travel Clip-SoundBible.com-397408270.wav"
        var sound = new Media(new File(audioFile).toURI().toString());
        var mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

      }

    }
  }
}