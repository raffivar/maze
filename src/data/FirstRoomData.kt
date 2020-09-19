package data

class FirstRoomData (roomId: String,
                     itemsData: ArrayList<ItemData>,
                     val wasExaminedBefore: Boolean,
                     val baseDescription: String): RoomData(roomId, itemsData)