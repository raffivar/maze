package data.items

class WindowData(name: String,
                 hasRopeAttached: Boolean,
                 val isClosed: Boolean) : RopeDependedItemData(name, hasRopeAttached)