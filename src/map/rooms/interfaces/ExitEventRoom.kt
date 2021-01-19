package map.rooms.interfaces

import game.GameResult
import map.directions.Direction
import player.Player

interface ExitEventRoom {
    fun onRoomExited(direction: Direction, player: Player): GameResult?
}