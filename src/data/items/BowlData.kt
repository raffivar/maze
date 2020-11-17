package data.items

import items.Bowl

class BowlData(name: String,
               val wasExaminedBefore: Boolean,
               val status: Bowl.BowlStatus) : ItemData(name)