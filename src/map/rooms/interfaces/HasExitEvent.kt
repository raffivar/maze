package map.rooms.interfaces

import game.GameResult
import map.directions.Direction
import player.Player

interface HasExitEvent {
    fun onExited(direction: Direction, player: Player): GameResult?
}