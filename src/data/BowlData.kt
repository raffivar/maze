package data

class BowlData(
    name: String,
    private val wasExamined: Boolean,
    private val isEmpty: Boolean) : ItemData(name)