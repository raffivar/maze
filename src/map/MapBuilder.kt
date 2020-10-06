package map

import data.MapData
import data.PlayerData
import data.ItemData
import game.Constraint
import game.GameData
import game.Player
import items.*

class MapBuilder {
    private val rooms = MazeMap()
    private val gameItems = ItemMap()
    lateinit var player: Player

    fun build(): Room {
        //Build rooms
        val firstRoom = FirstRoom()
        val poison = Poison()
        val roomWithCloset = RoomWithCloset(poison)

        //Tiger Rooms
        val roomWithTiger = Room("roomWithTiger")
        val roomSouthToTiger = Room("roomNorthToTiger")
        val roomEastToTiger = Room("roomEastToTiger")
        val roomNorthToTiger = Room("roomSouthToTiger")
        val tiger = Tiger(poison)
        roomNorthToTiger.addConstraint(Direction.NORTH, Constraint(tiger::isAlive, "Try distracting the [$tiger] first!"))
        roomWithTiger.addItem(tiger)

        //The rest of the rooms
        val rope = Rope()
        val roomWithRope = RoomWithRope(rope)
        val roomWithGuard = RoomWithGuard()
        val exit = Exit()

        //Link rooms together
        firstRoom.addRoom(Direction.WEST, roomWithCloset)
        roomWithCloset.addRoom(Direction.EAST, firstRoom)

        roomWithCloset.addRoom(Direction.NORTH, roomSouthToTiger)
        roomSouthToTiger.addRoom(Direction.SOUTH, roomWithCloset)

        roomSouthToTiger.addRoom(Direction.NORTH, roomWithTiger)
        roomWithTiger.addRoom(Direction.SOUTH, roomSouthToTiger)

        roomWithTiger.addRoom(Direction.NORTH, roomNorthToTiger)
        roomNorthToTiger.addRoom(Direction.SOUTH, roomWithTiger)

        roomWithTiger.addRoom(Direction.EAST, roomEastToTiger)
        roomEastToTiger.addRoom(Direction.WEST, roomWithTiger)

        roomEastToTiger.addRoom(Direction.NORTH, roomWithRope)
        roomWithRope.addRoom(Direction.SOUTH, roomEastToTiger)

        roomNorthToTiger.addRoom(Direction.EAST, roomWithRope)
        roomWithRope.addRoom(Direction.WEST, roomNorthToTiger)

        roomNorthToTiger.addRoom(Direction.WEST, roomWithGuard)
        roomWithGuard.addRoom(Direction.EAST, roomNorthToTiger)

        roomNorthToTiger.addRoom(Direction.NORTH, exit)

        //Save room data
        saveRoom(firstRoom)
        saveRoom(roomWithCloset)
        saveRoom(roomWithTiger)
        saveRoom(roomSouthToTiger)
        saveRoom(roomEastToTiger)
        saveRoom(roomNorthToTiger)
        saveRoom(roomWithRope)
        return firstRoom
    }

    private fun saveRoom(room: Room) {
        rooms.add(room)
        if (room is SavableRoom) {
            room.saveRoom(gameItems)
        }
    }

    fun collectDataToSave() : GameData {
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