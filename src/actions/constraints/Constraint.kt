package actions.constraints

class Constraint (val isConstraining: () -> Boolean, val message: String)