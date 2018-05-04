package cs2.game

import cs2.util.Vec2

class GameState(startGame: Boolean, running: Boolean, score: Int, energy: Int, speed: Boolean, playerLives: Int, player1Lives: Int, player2Lives: Int, bossLives: Int, player: Player, playerOne: Player, playerTwo: Player, boss: Boss, var enemies: EnemySwarm, var playerBullets: collection.mutable.ListBuffer[Bullet], var BFLBullets: collection.mutable.ListBuffer[Bullet] ,var enemyBullets: collection.mutable.ListBuffer[Bullet], var stars: Constellation, var explosions: collection.mutable.ListBuffer[Explosion], var powerUps: collection.mutable.ListBuffer[PowerUp]) {

  var startState = startGame
  var runState = running
  var scoreState = score
  var energyState = energy
  var speedState = speed
  var playerLivesState = playerLives
  var player1LivesState = player1Lives
  var player2LivesState = player2Lives
  var bossLivesState = bossLives
  var playerState = player
  var playerOneState = playerOne
  var playerTwoState = playerTwo
  var bossState = boss

}
