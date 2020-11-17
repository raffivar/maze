package data.player

import data.serializables.SerializableItemData

class PlayerData(val currentRoomId: String,
                 val inventoryData: ArrayList<SerializableItemData>)