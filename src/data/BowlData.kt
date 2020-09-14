package data

class BowlData(
    name: String,
    description: String,
    private val wasExamined: Boolean,
    private val isEmpty: Boolean) : ItemData(name, description)