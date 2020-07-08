class GameResult (val gameResultCode: GameResultCode, val message: String) {
    enum class GameResultCode {OK, ERROR, GAME_OVER}
}