package game

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import data.RoomData
import data.SavableItem
import data.SavableItemAdapter
import map.MapBuilder
import java.io.File


class GameDataManager(private val player: Player, private val mapBuilder: MapBuilder) {
    private val fileName = "save.txt"
    private var gson: Gson

    init {
        val builder = GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization()
        builder.registerTypeAdapter(SavableItem::class.java, SavableItemAdapter())
        gson = builder.setPrettyPrinting().create()
    }

    fun save(): GameResult {
        // Collect gameData
        val playerData = player.getData()
        val roomsData = ArrayList<RoomData>()
        for (room in mapBuilder.rooms.values) {
            roomsData.add(room.getData())
        }
        val gameData = GameData(playerData, roomsData)

        // Convert to json
        val gameDataAsJson = gson.toJson(gameData)

        // Save json to file
        val file = File(fileName)
        file.writeText(gameDataAsJson)
        return GameResult(GameResultCode.SUCCESS, "Game saved.")
    }

    fun load(): GameResult {
        val items = mapBuilder.items
        val file = File(fileName)
        val data = file.readText()
        val gameData = gson.fromJson(data, GameData::class.java)

        val inventoryData = gameData.playerData.inventoryData
        for (itemData in inventoryData) {
            val item = items[itemData.name]
            item?.let {
                player.inventory[item.name] = item
            }
        }

        return GameResult(GameResultCode.SUCCESS, "Game loaded.")
    }
}