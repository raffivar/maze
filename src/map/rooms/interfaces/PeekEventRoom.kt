package map.rooms.interfaces

import game.GameResult
import player.Player

interface PeekEventRoom {
    fun onRoomPeeked(defaultResult: () -> GameResult, player: Player): GameResult
}