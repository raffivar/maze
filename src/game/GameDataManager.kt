package game

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import data.MazeSerializer
import data.SerializableItemData
import data.SerializableRoomData
import map.MapBuilder
import java.io.File

class GameDataManager(private val player: Player, private val mapBuilder: MapBuilder) {
    private var builder: GsonBuilder = GsonBuilder().setPrettyPrinting()
    private var gson: Gson

    init {
        builder.registerTypeAdapter(SerializableItemData::class.java, MazeSerializer<SerializableItemData>())
        builder.registerTypeAdapter(SerializableRoomData::class.java, MazeSerializer<SerializableRoomData>())
        gson = builder.create()
    }

    fun save(): GameResult {
        val file = getFile()
        val gameData = mapBuilder.collectDataToSave()
        val roomToJson = gson.toJson(gameData)
        file.writeText(roomToJson)
        return GameResult(GameResultCode.SUCCESS, "Game saved.")
    }

    fun load(): GameResult {
        val file = getFile()
        if (!file.exists()) {
            return GameResult(GameResultCode.ERROR, "Error - there is no save file.")
        }
        val data = file.readText()
        val gameData = gson.fromJson(data, GameData::class.java)
        mapBuilder.loadData(gameData)
        return GameResult(GameResultCode.SUCCESS, "Game loaded.")
    }

    private fun getFile(): File {
        val filePath = "save"
        val fileName = "file.txt"
        val directory = File(filePath)
        if (!directory.exists()) {
            directory.mkdir()
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        return File("$filePath//$fileName")
    }
}
