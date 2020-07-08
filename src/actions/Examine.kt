package actions

import GameResult
import Player

class Examine(args: List<String>) : Action("Examine", args) {
    override fun execute(player: Player): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResult.GameResultCode.ERROR, "Please choose either the room or an item to examine")
        }
        val itemName = args[0]
        if (itemName == "room") {
            return GameResult(GameResult.GameResultCode.OK, player.currRoom.getDescription())
        }
        var item = player.inventory[itemName]
        if (item != null) {
            return GameResult(GameResult.GameResultCode.OK, item.desc)
        }
        item = player.currRoom.items[itemName]
        if (item != null) {
            return GameResult(GameResult.GameResultCode.OK, item.desc)
        }
        return GameResult(GameResult.GameResultCode.OK, "Item [$itemName] does not exist in your inventory or in the current room")
    }

}