package data

class BowlData(name: String,
               val wasExaminedBefore: Boolean,
               val isFull: Boolean) : SavableItemData(name)