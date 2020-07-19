package game

class GameResult(val gameResultCode: GameResultCode, val message: String) {
    constructor(resultCode: GameResultCode) : this(resultCode, "")
}