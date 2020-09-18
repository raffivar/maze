package data

class BowlData(
    name: String,
    private val wasExamined: Boolean,
    private val isFull: Boolean) : SavableItemData(name)