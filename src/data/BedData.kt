package data

class BedData(
    name: String,
    description: String,
    private val wasExamined: Boolean) : ItemData(name, description)