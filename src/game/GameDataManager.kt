package game

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import data.ItemData
import data.MazeSerializer
import data.SerializableRoomData
import map.MapBuilder
import java.io.File

class GameDataManager(private val player: Player, private val mapBuilder: MapBuilder) {
    private val fileName = "save/file.txt"
    private val file = File(fileName)
    private var builder: GsonBuilder = GsonBuilder().setPrettyPrinting()
    private var gson: Gson

    init {
        builder.registerTypeAdapter(ItemData::class.java, MazeSerializer<ItemData>())
        builder.registerTypeAdapter(SerializableRoomData::class.java, MazeSerializer<SerializableRoomData>())
        gson = builder.create()

    }

    fun save(): GameResult {
        val gameData = mapBuilder.collectDataToSave()
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
