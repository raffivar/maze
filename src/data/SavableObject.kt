package data

import game.GameData

interface SavableObject {
    fun saveIntoGameData(gameData: GameData)
}