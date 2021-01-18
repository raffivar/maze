package map.rooms.interfaces

import game.GameResult
import player.Player

interface EnterEventRoom {
    fun onRoomEntered(defaultResult: () -> GameResult, player: Player): GameResult
}