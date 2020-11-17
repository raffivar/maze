package data.rooms

import data.serializables.SerializableItemData

class FirstRoomData (roomId: String,
                     itemsData: ArrayList<SerializableItemData>,
                     val wasExaminedBefore: Boolean,
                     val baseDescription: String): RoomData(roomId, itemsData)