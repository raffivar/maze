package map.rooms.interfaces

import game.GameResult
import player.Player

interface HasEnterEvent {
    fun onEntered(defaultResult: GameResult, player: Player): GameResult
}