package data

import items.Item

class LockData(name: String,
               val whatOpens: Item) : SavableItemData(name)