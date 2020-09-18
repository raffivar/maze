package data

class BedData(
    name: String,
    private val wasExaminedBefore: Boolean) : SavableItemData(name)