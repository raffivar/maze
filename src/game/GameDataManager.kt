package game

import com.google.gson.GsonBuilder
import data.ItemData
import data.RoomData
import data.ItemDataSerializer
import map.MapBuilder
import java.io.File

class GameDataManager(private val player: Player, private val mapBuilder: MapBuilder) {
    private val fileName = "save.txt"
    private val file = File(fileName)
    private val builder = GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemData::class.java, ItemDataSerializer())
    private val gson = builder.create()

    fun save(): GameResult {
        //Collect game data
        val playerData = player.getData()
        val roomsData = ArrayList<RoomData>()
        for (room in mapBuilder.rooms.values) {
            roomsData.add(room.getData())
        }
        val gameData = GameData(playerData, roomsData)

        //Save to file
        val roomToJson = gson.toJson(gameData)
        file.writeText(roomToJson)

        return GameResult(GameResultCode.SUCCESS, "Game saved.")
    }

    fun load(): GameResult {
        //Load
        val file = File(fileName)
        val data = file.readText()
        val gameData = gson.fromJson(data, GameData::class.java)
        return GameResult(GameResultCode.SUCCESS, "Game loaded.")
    }
}
