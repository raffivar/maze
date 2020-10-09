package data

class TigerData(name: String,
                val isAlive: Boolean,
                val isPoisoned: Boolean,
                val facingSouth: Boolean,
                val currentRoomId: String) : ItemData(name)