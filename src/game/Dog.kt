package game

import items.Item
import map.Room
import kotlin.collections.ArrayList

class Dog(private val dogRoute: ArrayList<Room>) : Item("Dog", "This is a dog") {
    enum class DogDirection { FORWARD, BACKWARD }

    private var currentRoomIndex = -1
    private var dogDirection = DogDirection.FORWARD
    private var isMoving = false

    fun startMoving() {
        if (dogRoute.isEmpty()) {
            return
        }
        currentRoomIndex = 0
        dogRoute[currentRoomIndex].addItem(this)
        println("Dog placed")

        isMoving = true
        while (isMoving) {
            Thread.sleep(1000)
            moveToNextRoom()
        }
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
                    println("Dog moved ${dogDirection.name}")
                    dogDirection = DogDirection.BACKWARD
                }
                currentRoomIndex < dogRoute.lastIndex -> {
                    currentRoomIndex++
                    nextRoom = dogRoute[currentRoomIndex]
                    prevRoom.removeItem(this)
                    nextRoom.addItem(this)
                    println("Dog moved ${dogDirection.name}")
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
                    println("Dog moved ${dogDirection.name}")
                    dogDirection = DogDirection.FORWARD
                }
                currentRoomIndex > 0 -> {
                    currentRoomIndex--
                    nextRoom = dogRoute[currentRoomIndex]
                    prevRoom.removeItem(this)
                    nextRoom.addItem(this)
                    println("Dog moved ${dogDirection.name}")
                }
                else -> {
                    dogDirection = DogDirection.FORWARD
                    moveToNextRoom()
                }
            }
        }
    }

    fun stopMoving() {
        isMoving = false
    }
}