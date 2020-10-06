package map

import data.MapData
import data.PlayerData
import data.ItemData
import game.Constraint
import game.GameData
import game.Player
import items.Poison
import items.ItemMap
import items.SavableItem
import items.Tiger

class MapBuilder(private val gameThreads: ArrayList<Thread>) {
    private val rooms = MazeMap()
    private val gameItems = ItemMap()
    lateinit var player: Player

    fun build(): Room {
        //Build rooms
        val room1 = FirstRoom()
        val room2 = Room("room2")
        val room3 = Room("room3")
        val roomWithTiger = DogRoom("dogRoom2")
        val room6 = DogRoom("dogRoom1")
        val room5 = Room("room5")
        val poison = Poison()
        val roomWithBowl = RoomWithBowl(poison)
        val roomWithGuard = RoomWithGuard()
        val exit = Exit()

        //Link rooms together
        room1.addRoom(Direction.WEST, room2)
        room2.addRoom(Direction.EAST, room1)

        room2.addRoom(Direction.NORTH, room3)
        room3.addRoom(Direction.SOUTH, room2)

        room3.addRoom(Direction.NORTH, roomWithTiger)
        roomWithTiger.addRoom(Direction.SOUTH, room3)

        roomWithTiger.addRoom(Direction.NORTH, room6)
        room6.addRoom(Direction.SOUTH, roomWithTiger)

        roomWithTiger.addRoom(Direction.EAST, room5)
        room5.addRoom(Direction.WEST, roomWithTiger)

        room5.addRoom(Direction.NORTH, roomWithBowl)
        roomWithBowl.addRoom(Direction.SOUTH, room5)

        room6.addRoom(Direction.EAST, roomWithBowl)
        roomWithBowl.addRoom(Direction.WEST, room6)

        room6.addRoom(Direction.WEST, roomWithGuard)
        roomWithGuard.addRoom(Direction.EAST, room6)

        room6.addRoom(Direction.NORTH, exit)

        //Set dog
        val tiger = Tiger()
        tiger.setStoppingItem(poison)
        room6.addConstraint(Direction.NORTH, Constraint(tiger::isAlive, "Try distracting the [$tiger] first!"))
        roomWithTiger.addItem(tiger)

        //Save room data
        saveRoom(room1)
        saveRoom(roomWithBowl)
        saveRoom(room6)
        saveRoom(roomWithTiger)
        return room1
    }

    private fun saveRoom(room: Room) {
        rooms.add(room)
        if (room is SavableRoom) {
            room.save(gameItems)
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