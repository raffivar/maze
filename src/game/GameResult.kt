package game

class GameResult(val gameResultCode: GameResultCode, val message: String) {
    constructor(resultCode: GameResultCode) : this(resultCode, "")

    fun createByPrefix(prefix : String) : GameResult {
        return GameResult(gameResultCode, "$prefix\n$message")
    }
}