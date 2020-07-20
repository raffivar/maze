package game

import items.Item
import map.Room
import kotlin.collections.ArrayList

class Dog(private val dogRoute: ArrayList<Room>) : Item("Dog", "This is a dog") {
    enum class DogDirection { FORWARD, BACKWARD }

    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()
    private var currentRoomIndex = -1
    private var dogDirection = DogDirection.FORWARD
    private var isMoving = false

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    fun setItemToStop(itemUsed: Item) {
        itemsToFunctions[itemUsed] = this::stop
    }

    fun startMoving() {
        if (dogRoute.isEmpty()) {
            return
        }
        currentRoomIndex = 0
        dogRoute[currentRoomIndex].addItem(this)
        println("Dog placed")
        isMoving = true
        Thread {
            while (isMoving) {
                moveToNextRoom()
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun moveToNextRoom() {
        val prevRoom = dogRoute[currentRoomIndex]
        val nextRoom: Room

        if (dogDirection == DogDirection.FORWARD) {
            when {
                dogRoute.size == 1 -> {
                    nextRoom = prevRoom
                    prevRoom.removeItem(this)
                    nextRoom.addItem(this)
                    //println("Dog moved ${dogDirection.name}")
                    dogDirection = DogDirection.BACKWARD
                }
                currentRoomIndex < dogRoute.lastIndex -> {
                    currentRoomIndex++
                    nextRoom = dogRoute[currentRoomIndex]
                    prevRoom.removeItem(this)
                    nextRoom.addItem(this)
                    //println("Dog moved ${dogDirection.name}")
                }
                else -> {
                    dogDirection = DogDirection.BACKWARD
                    moveToNextRoom()
                }
            }
        } else if (dogDirection == DogDirection.BACKWARD) {
            when {
                dogRoute.size == 1 -> {
                    nextRoom = prevRoom
                    prevRoom.removeItem(this)
                    nextRoom.addItem(this)
                    //println("Dog moved ${dogDirection.name}")
                    dogDirection = DogDirection.FORWARD
                }
                currentRoomIndex > 0 -> {
                    currentRoomIndex--
                    nextRoom = dogRoute[currentRoomIndex]
                    prevRoom.removeItem(this)
                    nextRoom.addItem(this)
                    //println("Dog moved ${dogDirection.name}")
                }
                else -> {
                    dogDirection = DogDirection.FORWARD
                    moveToNextRoom()
                }
            }
        }
    }

    private fun stop(player: Player, itemUsed: Item): GameResult {
        if (!isMoving) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] is already busy with [${itemUsed.name}]")
        }
        isMoving = false
        player.inventory.remove(itemUsed.name)
        return GameResult(GameResultCode.SUCCESS, "[${this.name}] is eating [${itemUsed.name}] and has stopped moving")
    }
}