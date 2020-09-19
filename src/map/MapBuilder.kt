package map

import data.MapData
import data.PlayerData
import data.ItemData
import items.dog.Dog
import game.Constraint
import game.GameData
import game.Player
import items.dog.DogRouteNode
import items.Bonzo
import items.ItemMap
import items.SavableItem
import items.dog.DogRoute

class MapBuilder(private val gameThreads: ArrayList<Thread>) {
    private val rooms = MazeMap()
    private val gameItems = ItemMap()
    lateinit var player: Player
    lateinit var dog: Dog

    fun build(): Room {
        //Build rooms
        val room1 = FirstRoom()
        val room2 = Room("room2")
        val room3 = Room("room3")
        val dogRoom2 = DogRoom("dogRoom2")
        val dogRoom1 = DogRoom("dogRoom1")
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
        val dogRoute = DogRoute(node1, node2)
        val dog = Dog(dogRoute, node1, gameThreads)
        dog.setStoppingItem(bonzo)
        dogRoom1.addConstraint(Direction.NORTH, Constraint(dog::isMoving, "Try distracting the dog first!"))
        dog.startMoving()

        //Save room data
        saveRoom(room1)
        saveRoom(roomWithBowl)
        saveRoom(dogRoom1)
        saveRoom(dogRoom2)
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