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
        //Floor #1
        val firstRoom = FirstRoom()
        val roomSouthToTiger = Room("roomSouthToTiger")
        val poison = Poison()
        val tiger = Tiger(poison)
        val roomWithCloset = RoomWithCloset(poison)
        val roomWithTiger = Room("roomWithTiger")
        roomWithTiger.addItem(tiger)

        val ladder = Ladder()
        val hatch = Hatch(ladder)
        val roomWithHatch = RoomWithHatch(hatch)
        roomWithHatch.addConstraint(Direction.NORTH, Constraint(hatch::isAccessible, "This [${hatch.name} is out of reach~!"))

        val rope = Rope()
        val roomWithRope = RoomWithRope(rope)
        val roomWithLadder = RoomWithLadder(ladder)
        val roomWithGuard = RoomWithGuard()

        //Floor #2
        val exit = Exit()

        //Link rooms together
        firstRoom.addRoom(Direction.WEST, roomWithCloset)
        roomWithCloset.addRoom(Direction.EAST, firstRoom)

        roomWithCloset.addRoom(Direction.NORTH, roomSouthToTiger)
        roomSouthToTiger.addRoom(Direction.SOUTH, roomWithCloset)

        roomSouthToTiger.addRoom(Direction.NORTH, roomWithTiger)
        roomWithTiger.addRoom(Direction.SOUTH, roomSouthToTiger)

        roomWithTiger.addRoom(Direction.NORTH, roomWithHatch)
        roomWithHatch.addRoom(Direction.SOUTH, roomWithTiger)

        roomWithTiger.addRoom(Direction.EAST, roomWithLadder)
        roomWithLadder.addRoom(Direction.WEST, roomWithTiger)

        roomWithLadder.addRoom(Direction.NORTH, roomWithRope)
        roomWithRope.addRoom(Direction.SOUTH, roomWithLadder)

        roomWithHatch.addRoom(Direction.EAST, roomWithRope)
        roomWithRope.addRoom(Direction.WEST, roomWithHatch)

        roomWithHatch.addRoom(Direction.WEST, roomWithGuard)
        roomWithGuard.addRoom(Direction.EAST, roomWithHatch)

        roomWithHatch.addRoom(Direction.NORTH, exit)

        //Save room data
        saveRoom(firstRoom)
        saveRoom(roomWithCloset)
        saveRoom(roomWithTiger)
        saveRoom(roomSouthToTiger)
        saveRoom(roomWithHatch)
        saveRoom(roomWithRope)
        saveRoom(roomWithLadder)
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