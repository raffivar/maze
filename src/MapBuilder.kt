class MapBuilder {
    fun build(): Room {
        val room1 = Room()
        val room2 = Room()
        val room3 = Room()

        room1.addRoom(Direction.EAST, room2)
        room2.addRoom(Direction.WEST, room1)

        room2.addRoom(Direction.EAST, room3)
        room3.addRoom(Direction.WEST, room2)

        return room1
    }
}