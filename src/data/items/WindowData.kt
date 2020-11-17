package data.items

import data.items.RopeDependedItemData

class WindowData(name: String,
                 hasRopeAttached: Boolean,
                 val isClosed: Boolean) : RopeDependedItemData(name, hasRopeAttached)