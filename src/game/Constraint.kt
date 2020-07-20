package game

class Constraint (val isConstraining: () -> Boolean, val message: String)