class Room {
    companion object {
        var roomId = 0
    }

    var desc: String
        private set

    init {
        roomId++
        desc = "This is room #$roomId"
    }

    val rooms = HashMap<Direction, Room>()

    fun addRoom(direction: Direction, room: Room) {
        rooms[direction] = room
    }
}