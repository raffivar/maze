package game

class Constraint (val constrainingParty: () -> Boolean, val message: String)