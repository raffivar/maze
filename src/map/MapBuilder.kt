package map

import items.dog.Dog
import game.Constraint
import items.dog.DogRouteNode
import items.Bonzo

class MapBuilder(private val gameThreads: ArrayList<Thread>) {
    val rooms = HashMap<String, Room>()

    fun build(): Room {
        //Build rooms
        val room1 = FirstRoom()
        val room2 = Room("room2")
        val room3 = Room("room3")
        val dogRoom2 = Room("dogRoom2")
        val dogRoom1 = Room("dogRoom1")
        val room5 = Room("room5")
        val bonzo = Bonzo()
        val roomWithBowl = RoomWithBowl(bonzo)
        val roomWithGuard = RoomWithGuard()
        val exit = Exit()

        //Link rooms together
        room1.addRoom(Direction.WEST, room2)
        room2.addRoom(Direction.EAST, room1)

        room2.addRoom(Direction.NORTH, room3)
        room3.addRoom(Direction.SOUTH, room2)

        room3.addRoom(Direction.NORTH, dogRoom2)
        dogRoom2.addRoom(Direction.SOUTH, room3)

        dogRoom2.addRoom(Direction.NORTH, dogRoom1)
        dogRoom1.addRoom(Direction.SOUTH, dogRoom2)

        dogRoom2.addRoom(Direction.EAST, room5)
        room5.addRoom(Direction.WEST, dogRoom2)

        room5.addRoom(Direction.NORTH, roomWithBowl)
        roomWithBowl.addRoom(Direction.SOUTH, room5)

        dogRoom1.addRoom(Direction.EAST, roomWithBowl)
        roomWithBowl.addRoom(Direction.WEST, dogRoom1)

        dogRoom1.addRoom(Direction.WEST, roomWithGuard)
        roomWithGuard.addRoom(Direction.EAST, dogRoom1)

        dogRoom1.addRoom(Direction.NORTH, exit)

        //Set dog
        val node1 = DogRouteNode(dogRoom1.roomId, dogRoom1, null)
        val node2 = DogRouteNode(dogRoom2.roomId, dogRoom2, node1)
        node1.next = node2
        val dog = Dog(node1, gameThreads)
        dog.setStoppingItem(bonzo)
        dogRoom1.addConstraint(Direction.NORTH, Constraint(dog::isMoving, "Try distracting the dog first!"))
        dog.startMoving()

        //Save rooms as a HashMap
        addRoomToMap(room1)
        addRoomToMap(room2)
        addRoomToMap(room3)
        addRoomToMap(dogRoom2)
        addRoomToMap(dogRoom1)
        addRoomToMap(room5)
        addRoomToMap(roomWithBowl)
        addRoomToMap(roomWithGuard)
        addRoomToMap(exit)

        return room1
    }

    private fun addRoomToMap(room: Room) {
        rooms[room.roomId] = room
    }
}