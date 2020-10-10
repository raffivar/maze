package data

class WindowData(name: String,
                 hasRopeAttached: Boolean,
                 val isClosed: Boolean) : RopeDependedItemData(name, hasRopeAttached)