package puzzle

import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList

fun main() {

    val program = LeLabyrinthe()
    program.run()
}

class LeLabyrinthe {

    private var initialPosition: Tile? = null
    private var targetPosition: Tile?  = null
    private var currentTile: Tile? = null
    private var R = 0 // number of rows.
    private var C = 0 // number of columns.
    private var A = 0 // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.
    private var state = State.SEARCH
    private val openSet = ArrayList<Tile>()
    private val checkedSet = ArrayList<Tile>()
    private val finalPath = Stack<Tile>()

    fun run() {
        val input = Scanner(System.`in`)
        R = input.nextInt() // number of rows.
        C = input.nextInt() // number of columns.
        A = input.nextInt() // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.
        val tileBoard  = List(R){ arrayOfNulls<Tile>(C).toMutableList()}.toMutableList()

        for (row in 0 until R)
            for(col in 0 until C)
                tileBoard[row][col] = Tile(col, row, '?', null)

        // game loop
        while (true) {
            //Add current position to the stack of visited tiles and board
            val currentPosition = Pair(input.nextInt(), input.nextInt())

            for (row in 0 until R) {
                val rowValues = input.next().toCharArray()
                for(col in 0 until tileBoard[row].size){
                    if(initialPosition != null && targetPosition!= null) calculateDistance(initialPosition!!, tileBoard[row][col]!!, targetPosition!!)
                    tileBoard[row][col]!!.value = rowValues[col]
                    if(tileBoard[row][col]!!.value == '#') tileBoard[row][col]!!.solid = true
                    if(tileBoard[row][col]!!.value == 'T') {
                        tileBoard[row][col]!!.start = true
                        initialPosition = tileBoard[row][col]
                    }
                    if(tileBoard[row][col]!!.value == 'C') {
                        tileBoard[row][col]!!.target = true
                        targetPosition = tileBoard[row][col]
                    }
                    if(row == currentPosition.first && col==currentPosition.second) tileBoard[row][col]!!.visited = true
                }
            }
            currentTile = tileBoard[currentPosition.first][currentPosition.second]

            //debug(tileBoard.map{row -> row.map { tile -> tile!!.value.toString() }.toString() + '\n'}.toString())
            //if(initialPosition != null && targetPosition!= null) debug(tileBoard.map{row -> row.map { tile -> tile!!.fCost.toString() + "," + tile.gCost.toString()}.toString() + '\n'}.toString())

            if(currentTile!!.target) {
                state = State.BACK
                search(tileBoard)
                /*
                val debugBoard = tileBoard.map{it}
                for (row in 0 until R) {
                    for (col in 0 until C)
                        if (finalPath.contains(debugBoard[row][col])) debugBoard[row][col]!!.value = 'o'
                    debug(debugBoard[row].joinToString(","))
                }*/
                //debug("Path: " + finalPath.size + "steps, " + finalPath.map{tile -> tile!!.parent.toString() })
            }
            var action = ""
            if(state == State.SEARCH) {
                val rightTile = tileBoard.getOrNull(currentPosition.first)!!.getOrNull(currentPosition.second+1)!!
                val leftTile = tileBoard.getOrNull(currentPosition.first)!!.getOrNull(currentPosition.second-1)!!
                val upTile = tileBoard.getOrNull(currentPosition.first-1)!!.getOrNull(currentPosition.second)!!
                val downTile = tileBoard.getOrNull(currentPosition.first+1)!!.getOrNull(currentPosition.second)!!
                if(!rightTile.solid && !rightTile.visited){
                    currentTile!!.direction = Direction.RIGHT
                    rightTile.previous = currentTile
                    action = getDirection(currentTile!!, rightTile)
                }
                else if(!leftTile.solid && !leftTile.visited){
                    currentTile!!.direction = Direction.LEFT
                    leftTile.previous = currentTile
                    action = getDirection(currentTile!!, leftTile)
                }
                else if(!downTile.solid && !downTile.visited){
                    currentTile!!.direction = Direction.DOWN
                    downTile.previous = currentTile
                    action = getDirection(currentTile!!, downTile)
                }
                else if(!upTile.solid && !upTile.visited){
                    currentTile!!.direction = Direction.UP
                    upTile.previous = currentTile
                    action = getDirection(currentTile!!, upTile)
                }
                if(action == "") action = OppositeDirection.valueOf(currentTile!!.previous!!.direction.toString()).direction

            }
            else {
                val nextTile = finalPath.pop()
                action = getDirection(currentTile!!, nextTile)
            }
            println(action)
        }

    }

    private fun search(tileBoard: MutableList<MutableList<Tile?>>){
        var step = 0
        while (!currentTile!!.start && step < 3000){
            val col = currentTile!!.x
            val row = currentTile!!.y
            currentTile!!.setAsChecked()
            checkedSet.add(currentTile!!)
            openSet.remove(currentTile)
            if(col+1 < C && tileBoard[row][col+1]!!.value != '?') tileBoard[row][col+1]?.let { openNode(it) } //Open right node
            if(col-1 >= 0 && tileBoard[row][col-1]!!.value != '?') tileBoard[row][col-1]?.let { openNode(it) } //Open left node
            if(row-1 >= 0 && tileBoard[row-1][col]!!.value != '?') tileBoard[row-1][col]?.let { openNode(it) } //Open up node
            if(row+1 < R && tileBoard[row+1][col]!!.value != '?') tileBoard[row+1][col]?.let { openNode(it) } //Open down node

            var bestNodeIndex = 0
            var bestNodeFCost = 1000
            for(i in 0 until openSet.size){
                if(openSet[i].fCost < bestNodeFCost) {
                    bestNodeIndex = i
                    bestNodeFCost = openSet[i].fCost
                }else if(openSet[i].fCost == bestNodeFCost){
                    if(openSet[i].gCost < openSet[bestNodeIndex].gCost) bestNodeIndex = i
                }
            }
            currentTile = openSet[bestNodeIndex]
            step++
        }
        trackPathBack()
    }
    private fun openNode(node: Tile){
        if(!node.open && !node.checked && !node.solid){
            node.setAsOpen()
            node.parent = currentTile
            openSet.add(node)
        }
    }
    private fun trackPathBack(){
        while(!currentTile!!.target){
            finalPath.add(currentTile!!)
            currentTile = currentTile!!.parent!!
        }
    }
    private fun calculateDistance(initialPosition: Tile, currentPosition: Tile, targetPosition: Tile) {
        var distX = abs(initialPosition.x - currentPosition.x)
        var disyY = abs(initialPosition.y - currentPosition.y)
        currentPosition.hCost = distX + disyY
        distX = abs(targetPosition.x - currentPosition.x)
        disyY = abs(targetPosition.y - currentPosition.y)
        currentPosition.gCost = distX + disyY
        currentPosition.fCost = currentPosition.gCost + currentPosition.hCost
    }
    data class Tile(val x:Int, val y:Int, var value: Char, var previous: Tile?){
        var gCost: Int = 0
        var hCost: Int = 0
        var fCost: Int = 10000
        var start = false
        var target = false
        var open = false
        var checked = false
        var solid = false
        var visited = false
        var parent: Tile? = null
        var direction = Direction.START
        override fun toString(): String {
            return "$value"//"(x:$x,y:$y)"
        }
        override fun equals(other: Any?): Boolean {
            return ((other as Tile).x == this.x && (other as Tile).y == this.y)
        }
        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
        fun setAsOpen(){open = true}
        fun setAsChecked(){checked = true}

    }
    private fun debug(message: String){System.err.println(message)}
    enum class State {SEARCH, BACK}
    enum class OppositeDirection(val direction: String){
        LEFT("RIGHT"),
        RIGHT("LEFT"),
        UP("DOWN"),
        DOWN("UP")
    }
    enum class Direction{RIGHT, LEFT, UP, DOWN, START}
    private fun getDirection(currentTile: Tile, nextTile: Tile):String {
        val diffX = nextTile.x - currentTile.x
        val diffY = nextTile.y - currentTile.y
        if(diffX > 0) return "RIGHT"
        if(diffX < 0) return "LEFT"
        if(diffY > 0) return "DOWN"
        if(diffY < 0) return "UP"
        return "ERROR"
    }
}

