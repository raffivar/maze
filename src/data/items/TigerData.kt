package data.items

import items.Tiger.TigerStatus

class TigerData(name: String,
                val status: TigerStatus,
                val timesPeeked: Int,
                val facingSouth: Boolean,
                val currentRoomId: String) : ItemData(name)