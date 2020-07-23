package game

import items.Item

class Dog(private var currentNode: DogRouteNode, private val gameThreads: ArrayList<Thread>) : Item("Dog", "This is a dog") {
    var isMoving = false
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    init {
        currentNode.room.addItem(this)
    }

    fun startMoving() {
        isMoving = true
        val runnable = Runnable {
            while (isMoving) {
                moveToNextRoom()
                Thread.sleep(1000)
            }
        }
        val dogThread = GameThread(runnable, "Dog killed.")
        gameThreads.add(dogThread)
        dogThread.start()
    }

    private fun moveToNextRoom() {
        val nextNode = currentNode.next ?: return
        currentNode.room.removeItem(this)
        nextNode.room.addItem(this)
        currentNode = nextNode
    }


    fun setStoppingItem(itemUsed: Item) {
        itemsToFunctions[itemUsed] = this::stop
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    private fun stop(player: Player, itemUsed: Item): GameResult {
        if (!isMoving) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] is already busy with [${itemUsed.name}]")
        }
        isMoving = false
        return GameResult(GameResultCode.SUCCESS, "[${this.name}] is eating [${itemUsed.name}] and has stopped moving")
    }
}