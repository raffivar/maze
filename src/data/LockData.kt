package data

import items.Item

class LockData(
    name: String,
    private val whatOpens: Item
) : SavableItemData(name)