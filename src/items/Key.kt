package items

import game.GameResult
import game.GameResultCode
import player.Player

class Key : Item("Key", "A tiny key, covered in dust.") {
    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}]. By how dusty it is, looks like it's been laying there for quite a while.")
    }
}