package map

import game.Dog
import java.util.*

class MapBuilder {
    lateinit var playerFirstRoom: Room

    fun build(): MapBuilder {
        //Build rooms
        val room1 = FirstRoom()
        val room2 = Room()
        val room3 = Room()
        val room4 = Room()
        val dogRoom2 = Room()
        val dogRoom1 = Room()
        val room7 = RoomWithBowl()
        val room8 = Room()
        val exit = Exit()

        //Link rooms together
        room1.addRoom(Direction.WEST, room2)
        room2.addRoom(Direction.EAST, room1)

        room2.addRoom(Direction.NORTH, room3)
        room3.addRoom(Direction.SOUTH, room2)

        room3.addRoom(Direction.NORTH, room4)
        room4.addRoom(Direction.SOUTH, room3)

        room4.addRoom(Direction.NORTH, dogRoom2)
        dogRoom2.addRoom(Direction.SOUTH, room4)

        room4.addRoom(Direction.EAST, dogRoom1)
        dogRoom1.addRoom(Direction.WEST, room4)

        dogRoom1.addRoom(Direction.NORTH, room7)
        room7.addRoom(Direction.SOUTH, dogRoom1)

        dogRoom2.addRoom(Direction.EAST, room7)
        room7.addRoom(Direction.WEST, dogRoom2)

        dogRoom2.addRoom(Direction.WEST, room8)
        room8.addRoom(Direction.EAST, dogRoom2)

        dogRoom2.addRoom(Direction.NORTH, exit)

        //Set player first room
        playerFirstRoom = room1

        //Set dog route
        val dogRoute = arrayListOf<Room>()
        dogRoute.add(dogRoom1)
        dogRoute.add(dogRoom2)

        //set dog
        val dog = Dog(dogRoute)
        dog.startMoving()

        return this
    }
}