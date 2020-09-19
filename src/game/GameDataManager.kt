package game

import com.google.gson.GsonBuilder
import data.ItemData
import data.MazeSerializer
import map.MapBuilder
import java.io.File

class GameDataManager(private val player: Player, private val mapBuilder: MapBuilder) {
    private val fileName = "save/file.txt"
    private val file = File(fileName)
    private val builder = GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemData::class.java, MazeSerializer<ItemData>())
    private val gson = builder.create()

    fun save(): GameResult {
        val gameData = mapBuilder.getDataToSave()
        val roomToJson = gson.toJson(gameData)
        file.writeText(roomToJson)
        return GameResult(GameResultCode.SUCCESS, "Game saved.")
    }

    fun load(): GameResult {
        val file = File(fileName)
        val data = file.readText()
        val gameData = gson.fromJson(data, GameData::class.java)
        mapBuilder.loadData(gameData)
        return GameResult(GameResultCode.SUCCESS, "Game loaded.")
    }
}
