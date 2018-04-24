package cs2.game

import cs2.util.Vec2

class GameState(startGame: Boolean, running: Boolean, score: Int, playerLives: Int, bossLives: Int, player: Player, boss: Boss, var enemies: EnemySwarm, var playerBullets: collection.mutable.ListBuffer[Bullet], var enemyBullets: collection.mutable.ListBuffer[Bullet], var stars: Constellation, var explosions: collection.mutable.ListBuffer[Explosion]) {

  var startState = startGame
  var runState = running
  var scoreState = score
  var playerLivesState = playerLives
  var bossLivesState = bossLives
  var playerState = player
  var bossState = boss

}
