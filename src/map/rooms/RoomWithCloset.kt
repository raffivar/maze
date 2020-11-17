package map.rooms

import game.GameResult
import game.GameResultCode
import items.*
import items.maps.ItemMap
import map.rooms.Room

class RoomWithCloset(door: Door, private val poison: Poison) : Room("RoomWithCloset") {
    private var closet: Closet

    init {
        closet = Closet(true, this::addPoisonToRoom)
        addItem(closet)
        baseDescription = "This room nothing but a [${closet.name}] in it."
        addItem(door)
    }

    private fun addPoisonToRoom(): GameResult {
        addItem(poison)
        return GameResult(GameResultCode.SUCCESS, "Opened the [${closet.name}]. You discover some [${poison.name}] inside.")
    }

    override fun saveRoomDataToDB(gameItems: ItemMap) {
        super.saveRoomDataToDB(gameItems)
        gameItems.addItem(poison)
    }
}