package items

import data.ItemData
import data.RopeDependedItemData
import game.GameResult
import game.GameResultCode
import game.Player

class Ladder : Item("Ladder", "A standard, metal, relatively big ladder."), SavableItem {
    private var isInUse = false

    override fun take(player: Player): GameResult {
        if (isInUse) {
            return GameResult(GameResultCode.SUCCESS, "Cannot take [${this.name}], it's clearly in use.")
        }
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }

    override fun useOn(player: Player, itemUsedOn: Item): GameResult {
        val useResult = itemUsedOn.usedBy(player, this)
        if (useResult.gameResultCode == GameResultCode.SUCCESS) {
            player.inventory.removeItem(this)
            player.currentRoom.addItem(this)
            isInUse = true
        }
        return useResult
    }

    override fun getData() : ItemData {
        return RopeDependedItemData(name, isInUse)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as RopeDependedItemData
        isInUse = data.hasNothingAttached
    }
}