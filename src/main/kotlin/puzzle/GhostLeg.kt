package puzzle

import java.util.*

fun main() {
    val input = Scanner(System.`in`)
    val program = GhostLeg(input)
    program.run()
}
class GhostLeg(val input: Scanner) {
    private var width = 0
    private var height = 0
    private var diagram = mutableListOf<MutableList<Char>>()
    private var path = mutableSetOf<Position>()
    fun run(){
        width = input.nextInt()
        height = input.nextInt()
        diagram = MutableList(height){ MutableList(width){' '} }
        var counter = 0
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until height) {
            diagram[counter++] = input.nextLine().toCharArray().toMutableList()
        }
        System.err.println(diagram.map{line -> line + '\n'})
        for(columns in 0 .. (width-1)/3){
            path.clear()
            println(diagram[0][columns*3].toString() + diagramTraversal(Position(columns*3,0)))
        }
    }

    private fun diagramTraversal(position: Position): String{
        path.add(position)
        val nextMove = findNextMove(position)
        //System.err.println("Return value: " + nextMove.col + "," + nextMove.lin)
        return if(nextMove.lin < height - 1) diagramTraversal(nextMove)
        else return diagram[nextMove.lin][nextMove.col].toString()
    }

    private fun findNextMove(position: Position): Position {
        //System.err.println("findNextMove: " + position.col + "," + position.lin)
        if(position.col-1 >=0 && !path.contains(Position(position.col-3, position.lin)) && diagram[position.lin][position.col-1] == '-') return Position(position.col-3, position.lin)
        if(position.col+1 < width && !path.contains(Position(position.col+3, position.lin)) && diagram[position.lin][position.col+1] == '-') return Position(position.col+3, position.lin)
        return Position(position.col, position.lin+1)
    }

    data class Position(val col:Int, val lin:Int)
}