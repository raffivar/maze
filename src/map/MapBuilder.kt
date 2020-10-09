package map

import data.ItemData
import data.MapData
import data.PlayerData
import game.GameData
import game.Player
import items.*
import javax.management.openmbean.KeyAlreadyExistsException

class MapBuilder {
    private val rooms = MazeMap()
    private val gameItems = ItemMap()
    lateinit var player: Player

    fun build(): Room {
        //Floor #1
        val firstRoom = FirstRoom()
        val roomSouthToTiger = Room("roomSouthToTiger")
        val poison = Poison()
        val tiger = Tiger()
        val roomWithCloset = RoomWithCloset(poison)
        val roomWithTiger = RoomWithTiger("roomWithTiger", tiger, Bowl(poison))
        roomWithTiger.addItem(tiger)
        val ladder = Ladder()
        val hatch = Hatch(ladder)
        val roomBelowHatch = RoomWithHatch("roomBelowHatch", hatch)

        val rope = Rope()
        val roomWithRope = RoomWithRope(rope)
        val roomWithLadder = RoomWithLadder(ladder)
        val roomWithGuard1 = RoomWithGuard()

        //Floor #2
        val roomAboveHatch = RoomWithHatch("roomAboveHatch", hatch)
        val roomWithGuard2 = RoomWithGuard()
        val roomWithWindow = RoomWithWindow(rope)
        val boringRoom1 = Room("boringRoom1", "This is an extremely boring room.")
        val boringRoom2 = Room("boringRoom2", "This room is even more boring. It doesn't even lead anywhere.")
        val exit = Exit()

        //Link rooms together
        firstRoom.addRoom(Direction.WEST, roomWithCloset)
        roomWithCloset.addRoom(Direction.EAST, firstRoom)

        roomWithCloset.addRoom(Direction.NORTH, roomSouthToTiger)
        roomSouthToTiger.addRoom(Direction.SOUTH, roomWithCloset)

        roomSouthToTiger.addRoom(Direction.NORTH, roomWithTiger)
        roomWithTiger.addRoom(Direction.SOUTH, roomSouthToTiger)

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

        roomAboveHatch.addRoom(Direction.SOUTH, roomWithWindow)
        roomWithWindow.addRoom(Direction.NORTH, roomAboveHatch)

        roomWithWindow.addRoom(Direction.SOUTH, boringRoom1)
        boringRoom1.addRoom(Direction.NORTH, roomWithWindow)

        boringRoom1.addRoom(Direction.SOUTH, boringRoom2)
        boringRoom2.addRoom(Direction.NORTH, boringRoom1)

        roomWithWindow.addRoom(Direction.DOWN, exit)

        //Save room data
        saveRoom(firstRoom)
        saveRoom(roomWithCloset)
        saveRoom(roomWithTiger)
        saveRoom(roomSouthToTiger)
        saveRoom(roomBelowHatch)
        saveRoom(roomWithRope)
        saveRoom(roomWithLadder)

        saveRoom(roomAboveHatch)
        saveRoom(roomWithWindow)
        saveRoom(boringRoom1)
        saveRoom(boringRoom2)

        return firstRoom
    }

    private fun saveRoom(room: Room) {
        if (rooms.containsKey(room.roomId)) {
            throw KeyAlreadyExistsException("Failed to add room to room list - roomId already exists")
        }
        rooms.add(room)
        if (room is SavableRoom) {
            room.saveRoom(gameItems)
        }
    }

    fun collectDataToSave(): GameData {
        val playerData = player.getData()
        val mapData = MapData(ArrayList())
        for (room in rooms.values) {
            mapData.roomsData.add(room.getData())
        }
        return GameData(playerData, mapData)
    }

    fun loadData(gameData: GameData) {
        loadPlayerData(gameData.playerData)
        loadMapData(gameData.mapData)
    }

    private fun loadPlayerData(playerData: PlayerData) {
        //Current room
        val room = rooms[playerData.currentRoomId]
        room?.let {
            player.currentRoom = room
        }

        //Inventory
        player.inventory.clear()
        val itemsData = playerData.inventoryData
        for (itemData in itemsData) {
            val item = gameItems[itemData.name]
            if (item is SavableItem) {
                item.loadItem(itemData as ItemData)
            }
            item?.let {
                player.inventory.add(item)
            }
        }
    }

    private fun loadMapData(mapData: MapData) {
        val roomsData = mapData.roomsData
        for (roomData in roomsData) {
            val room = rooms[roomData.roomId]
            room?.let {
                if (room is SavableRoom) {
                    room.loadRoom(roomData, gameItems)
                }
            }
        }
    }
}