package map.rooms.interfaces

import game.GameResult
import player.Player

interface HasPeekEvent {
    fun onRoomPeeked(defaultResult: () -> GameResult, player: Player): GameResult
}