package puzzle

import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val program = TheFall(input)
    program.run()
}
class TheFall(val input: Scanner){
    private val maze = mutableListOf<List<Int>>()
    data class Pos(val x:Int, val y: Int, val side: String)
    operator fun Pos.plus(move: Move): Pos = copy(x = x + move.dx, y = y + move.dy, side = side)

    data class Move(val dx:Int, val dy:Int)
    enum class Pieces(val move: Move) {
        DOWN(Move(0, 1)),
        RIGHT(Move(1,0)),
        LEFT(Move(-1,0));
        companion object {
            fun fromInt(value: Int, side: String): Pieces {
                return when (value) {
                    1, 3, 4, 5, 7, 8, 9, 12, 13 -> DOWN
                    2, 6 -> if (side == "RIGHT") LEFT else RIGHT
                    4 -> if (side == "TOP") LEFT else DOWN
                    5 -> if (side == "TOP") RIGHT else DOWN
                    11 -> RIGHT
                    10 -> LEFT
                    else -> DOWN
                }
            }
        }
    }
    fun run() {
        val columns = input.nextInt() // number of columns.
        val rows = input.nextInt() // number of rows.
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until rows) {
            val line = input.nextLine()
            maze.add(line.split(' ').map { it.toInt() }) // represents a line in the grid and contains W integers. Each integer represents one room of a given type.
        }
        val exitPosition = input.nextInt() // the coordinate along the X axis of the exit (not useful for this first mission, but must be read).
        // game loop
        while (true) {
            val indyPosition = Pos(input.nextInt(), input.nextInt(), input.next())
            System.err.println("Indy is $indyPosition, current piece is " + maze[indyPosition.y][indyPosition.x].toString())
            val nextPosition = (indyPosition + Pieces.fromInt(maze[indyPosition.y][indyPosition.x], indyPosition.side).move)
            println(nextPosition.x.toString() + " " + nextPosition.y)
        }
    }
}

