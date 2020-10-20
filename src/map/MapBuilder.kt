package map

import data.ItemData
import data.MapData
import data.PlayerData
import data.GameData
import game.Player
import items.*

class MapBuilder {
    private val roomDB = RoomMap()
    private val itemDB = ItemMap()
    lateinit var player: Player

    fun build(): Room {
        //Floor #1
        val firstRoom = FirstRoom()
        val poison = Poison()
        val roomWithCloset = RoomWithCloset(poison)

        val roomBeforeTiger = Room("roomBeforeTiger", "This room is completely empty. Like your life.\nIt does make you think about your past actions, and what led you here.\nFrom time to time it's good to... reflect. ")
        val tiger = Tiger()
        val roomWithTiger = RoomWithTiger("roomWithTiger", tiger, Bowl(poison))
        roomWithTiger.addItem(tiger)
        tiger.currentRoomId = roomWithTiger.roomId

        val ladder = Ladder()
        val roomWithLadder = RoomWithLadder(ladder)

        val hatch = Hatch(ladder)
        val roomBelowHatch = RoomWithHatch("roomBelowHatch", "A regular room with a hatch in the ceiling.", hatch)

        val possibleTigerDeathRooms: HashMap<String, Room> = hashMapOf(
            roomWithTiger.roomId to roomWithTiger,
            roomBeforeTiger.roomId to roomBeforeTiger,
            roomWithTiger.roomId to roomWithTiger,
            roomBelowHatch.roomId to roomBelowHatch)

        val forbiddenToLeaveTigerRooms: HashMap<String, Room> = hashMapOf(
            roomWithTiger.roomId to roomWithTiger,
            roomBelowHatch.roomId to roomBelowHatch)

        tiger.possibleDeathRooms = possibleTigerDeathRooms
        tiger.forbiddenToLeaveRooms = forbiddenToLeaveTigerRooms

        val rope = Rope()
        val roomWithRope = RoomWithRope(rope)
        val roomWithGuard1 = RoomWithGuard()

        //Floor #2
        val roomAboveHatch = RoomWithHatch("roomAboveHatch", "A regular room with a hatch in the floor.", hatch)
        val roomWithGuard2 = RoomWithGuard()
        val escapeRoom = EscapeRoom(rope, tiger)
        val boringRoom1 = Room("boringRoom1", "This is an extremely boring room.")
        val boringRoom2 = Room("boringRoom2", "This room is even more boring. It doesn't even lead anywhere.")
        val exit = Exit()

        //Link rooms together
        firstRoom.addRoom(Direction.WEST, roomWithCloset)
        roomWithCloset.addRoom(Direction.EAST, firstRoom)

        roomWithCloset.addRoom(Direction.NORTH, roomBeforeTiger)
        roomBeforeTiger.addRoom(Direction.SOUTH, roomWithCloset)

        roomBeforeTiger.addRoom(Direction.NORTH, roomWithTiger)
        roomWithTiger.addRoom(Direction.SOUTH, roomBeforeTiger)

        roomWithTiger.addRoom(Direction.NORTH, roomBelowHatch)
        roomBelowHatch.addRoom(Direction.SOUTH, roomWithTiger)

        roomWithTiger.addRoom(Direction.EAST, roomWithLadder)
        roomWithLadder.addRoom(Direction.WEST, roomWithTiger)

        roomWithLadder.addRoom(Direction.NORTH, roomWithRope)
        roomWithRope.addRoom(Direction.SOUTH, roomWithLadder)

        roomBelowHatch.addRoom(Direction.EAST, roomWithRope)
        roomWithRope.addRoom(Direction.WEST, roomBelowHatch)

        roomBelowHatch.addRoom(Direction.WEST, roomWithGuard1)
        roomWithGuard1.addRoom(Direction.EAST, roomBelowHatch)

        roomBelowHatch.addRoom(Direction.UP, roomAboveHatch)
        roomAboveHatch.addRoom(Direction.DOWN, roomBelowHatch)

        roomAboveHatch.addRoom(Direction.WEST, roomWithGuard2)
        roomWithGuard2.addRoom(Direction.EAST, roomAboveHatch)

        roomAboveHatch.addRoom(Direction.SOUTH, escapeRoom)
        escapeRoom.addRoom(Direction.NORTH, roomAboveHatch)

        escapeRoom.addRoom(Direction.SOUTH, boringRoom1)
        boringRoom1.addRoom(Direction.NORTH, escapeRoom)

        boringRoom1.addRoom(Direction.SOUTH, boringRoom2)
        boringRoom2.addRoom(Direction.NORTH, boringRoom1)

        escapeRoom.addRoom(Direction.DOWN, exit)

        //Save room data
        saveRoomToDB(firstRoom)
        saveRoomToDB(roomWithCloset)
        saveRoomToDB(roomBeforeTiger)
        saveRoomToDB(roomWithTiger)
        saveRoomToDB(roomWithLadder)
        saveRoomToDB(roomWithRope)
        saveRoomToDB(roomBelowHatch)
        saveRoomToDB(roomAboveHatch)
        saveRoomToDB(escapeRoom)
        saveRoomToDB(boringRoom1)
        saveRoomToDB(boringRoom2)
        return firstRoom
    }

    private fun saveRoomToDB(room: Room) {
        roomDB.add(room)
        if (room is SavableRoom) {
            room.saveRoomDataToDB(itemDB)
        }
    }

    fun collectBaseData(): GameData {
        val playerData = player.getBaseData()
        val roomsData = MapData(ArrayList())
        for (room in roomDB.values) {
            roomsData.roomsData.add(room.getBaseData())
        }
        return GameData(playerData, roomsData)
    }

    fun loadData(gameData: GameData) {
        loadPlayerData(gameData.playerData)
        loadMapData(gameData.mapData)
    }

    private fun loadPlayerData(playerData: PlayerData) {
        //Current room
        val room = roomDB[playerData.currentRoomId]
        room?.let {
            player.currentRoom = room
        }

        //Inventory
        player.inventory.clear()
        val itemsData = playerData.inventoryData
        for (itemData in itemsData) {
            val item = itemDB[itemData.name]
            if (item is SavableItem) {
                item.loadItem(itemData as ItemData)
            }
            item?.let {
                player.inventory.addItem(item)
            }
        }
    }

    private fun loadMapData(mapData: MapData) {
        for (roomData in mapData.roomsData) {
            val room = roomDB[roomData.roomId]
            room?.let {
                if (room is SavableRoom) {
                    room.loadFromDB(roomData, itemDB)
                }
            }
        }
    }
}