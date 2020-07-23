package game

class GameThread(private val runnable: Runnable, private val onInterruptMessage: String) : Thread() {
    override fun run() {
        try {
            runnable.run()
        } catch (e: InterruptedException) {
            println("[${e.message}] - $onInterruptMessage")
        }
    }
}