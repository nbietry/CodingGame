import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val W = input.nextInt() // width of the building.
    val H = input.nextInt() // height of the building.
    val N = input.nextInt() // maximum number of turns before game over.
    val X0 = input.nextInt()
    val Y0 = input.nextInt()
    var batman = Pos(X0, Y0)
    var (minX:Int, maxX:Int) = Pair(0,W)
    var (minY:Int, maxY:Int) = Pair(0,H)

    // game loop
    while (true) {
        val bombDir = input.next() // the direction of the bombs from batman's current location (U, UR, R, DR, D, DL, L or UL)

        if(Direction.valueOf(bombDir).move.dx > 0) {
            minX = batman.x
            batman = Pos(batman.x + (maxX-batman.x) / 2, batman.y)
        }
        if(Direction.valueOf(bombDir).move.dx < 0) {
            maxX = batman.x
            batman = Pos(batman.x-1 - (batman.x - minX) / 2, batman.y)
        }
        if(Direction.valueOf(bombDir).move.dy > 0) {
            minY = batman.y
            batman = Pos(batman.x, batman.y + (maxY - batman.y) / 2)
        }
        if(Direction.valueOf(bombDir).move.dy < 0) {
            maxY = batman.y
            batman = Pos(batman.x, batman.y-1 - (batman.y - minY) / 2 )
        }

        println(batman.toString())


    }
}

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