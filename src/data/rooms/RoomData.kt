package data.rooms

import data.serializables.SerializableItemData
import data.serializables.SerializableRoomData

open class RoomData (roomId: String,
                     itemsData: ArrayList<SerializableItemData>): SerializableRoomData(roomId, itemsData)