package puzzle

data class Move(val dx:Int, val dy:Int)
data class Pos(val x:Int, val y:Int){
    override fun toString(): String {
        return "$x $y"
    }
}
operator fun Pos.plus(move: Move): Pos = copy(x + move.dx, y + move.dy)
operator fun Move.times(factor: Int): Move = copy(this.dx * factor, this.dy * factor)
enum class Direction(val move: Move){
    U(Move(0, -1)),
    UR(Move(1,-1)),
    R(Move(1, 0)),
    DR(Move(1, 1)),
    D(Move(0, 1)),
    DL(Move(-1, 1)),
    L(Move(-1, 0)),
    UL(Move(-1, -1))
}

fun debug(text: String){
    System.err.println(text)
}