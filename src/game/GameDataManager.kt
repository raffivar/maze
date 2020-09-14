package game

import com.google.gson.GsonBuilder
import data.RoomData
import data.SavableObject
import map.Room
import java.io.File


class GameDataManager(private val player: Player, private val rooms: HashMap<String, Room>) {
    private val fileName = "save.txt"

    private fun addSavableObject(savableObject: SavableObject) {}

    fun save(): GameResult {
        // Collect gameData
        val playerData = player.getData()
        val roomsData = ArrayList<RoomData>()
        for (room in rooms.values) {
            roomsData.add(room.getData())
        }
        val gameData = GameData(playerData, roomsData)

        // Convert to json
        val gson = GsonBuilder().setPrettyPrinting().create()
        val gameDataAsJson = gson.toJson(gameData)

        // Save json to file
        val file = File(fileName)
        file.writeText(gameDataAsJson)
        return GameResult(GameResultCode.SUCCESS, "Game saved.")
    }

    fun load(): GameResult {
        val file = File(fileName)
        val dataLines = file.readLines()
        //TODO: implement
        return GameResult(GameResultCode.SUCCESS, "Game loaded.")
    }
}