package map

import game.Constraint
import game.Dog
import items.Bonzo

class MapBuilder {
    fun build(): Room {
        //Build rooms
        val room1 = FirstRoom()
        val room2 = Room()
        val room3 = Room()
        val dogRoom2 = Room()
        val dogRoom1 = Room()
        val room5 = Room()
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

        //Set dog route
        val dogRoute = arrayListOf<Room>()
        dogRoute.add(dogRoom2)
        dogRoute.add(dogRoom1)

        //set dog
        val dog = Dog(dogRoute)
        dog.setItemToStop(bonzo)
        dog.startMoving()
        dogRoom1.addConstraint(Direction.NORTH, Constraint(dog::isMoving, "Try distracting the dog first!"))

        return room1
    }
}