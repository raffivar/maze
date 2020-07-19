package map

class MapBuilder {
    fun build(): Room {
        //Build rooms
        val room1 = FirstRoom()
        val room2 = Room()
        val room3 = Room()
        val room4 = Room()
        val room5 = Room()
        val room6 = Room()
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

        room4.addRoom(Direction.NORTH, room5)
        room5.addRoom(Direction.SOUTH, room4)

        room4.addRoom(Direction.EAST, room6)
        room6.addRoom(Direction.WEST, room4)

        room6.addRoom(Direction.NORTH, room7)
        room7.addRoom(Direction.SOUTH, room6)

        room5.addRoom(Direction.EAST, room7)
        room7.addRoom(Direction.WEST, room5)

        room5.addRoom(Direction.WEST, room8)
        room8.addRoom(Direction.EAST, room5)

        room5.addRoom(Direction.NORTH, exit)

        return room1
    }
}