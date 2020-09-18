package data

class DoorData(
    name: String,
    private val isClosed: Boolean) : SavableItemData(name)