package bot

import java.util.*
import java.util.stream.Collectors
import kotlin.math.pow
import kotlin.math.sqrt

class Game(val width: Int, val height: Int) {
    var myMatter: Int = 0
    var oppMatter: Int = 0
    val battleground = Array(height) { arrayOfNulls<Cell>(width) }
    val myCells: MutableList<Cell> = ArrayList()
    val freeCells: MutableList<Cell> = ArrayList()
    val myRobots: MutableList<Robot> = ArrayList()
    fun newTurn() {
        this.myCells.clear()
        this.freeCells.clear()
        this.myRobots.clear()
    }
}

enum class Ownership(val code: Int) {
    ME(1), OPP(0), FREE(-1);

    companion object {
        private val map = Ownership.values().associateBy { it.code }
        operator fun get(value: Int) = map[value]
    }
}

class Cell(val x: Int, val y: Int,val scrapAmount: Int, owner: Int,val units: Int, val recycler: Int, canBuild: Int, canSpawn: Int,private val inRangeOfRecycler: Int,var distance: Int) {
    val owner = Ownership[owner]
    val canBuild = canBuild.toBoolean()
    val canSpawn = canSpawn.toBoolean()
    override fun toString(): String {
        return "($x,$y), Owmer: $owner, Recycler: $recycler, Can build: $canBuild, Can spawn: $canSpawn"
    }

    override fun equals(other: Any?): Boolean =
        other is Cell && (this.x == other.x && this.y == other.y)

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + scrapAmount
        result = 31 * result + units
        result = 31 * result + recycler
        result = 31 * result + inRangeOfRecycler
        result = 31 * result + distance
        result = 31 * result + (owner?.hashCode() ?: 0)
        result = 31 * result + canBuild.hashCode()
        result = 31 * result + canSpawn.hashCode()
        return result
    }
}
class Robot(val x: Int, val y: Int, private var target: Cell?) {
    override fun toString(): String {
        return "Robot ($x,$y), target: $target"
    }
}

fun Int.toBoolean() = this == 1

fun debug(message: String) {
    System.err.println(message)
}

fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
    val dist = sqrt((x2 - x1).toDouble().pow(2.0) + (y2 - y1).toDouble().pow(2.0))
    return dist.toInt()
}

fun findClosestFreeCell(robot: Robot, cells: MutableList<Cell>): Cell {
    val availableCells: MutableList<Cell> = ArrayList()
    for (cell in cells) {
        val distanceFromRobot = distance(robot.x, robot.y, cell.x, cell.y)
        cell.distance = distanceFromRobot
        availableCells.add(cell)
    }
    return availableCells.sortedWith(compareBy { it.distance }).first()
}

// New function to manage border robots
fun manageBorderRobots(game: Game) {
    // Implement logic for managing robots defining the territory border here
    // Update the `game.myRobots` list accordingly
}

// New function to manage interior robots
fun manageInteriorRobots(game: Game) {
    // Implement logic for managing robots exploring and occupying the interior here
    // Update the `game.myRobots` list accordingly
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val game = Game(input.nextInt(), input.nextInt())
    val actions: MutableList<String> = ArrayList()

    // game loop
    while (true) {
        game.newTurn()
        actions.clear()
        game.myMatter = input.nextInt()
        game.oppMatter = input.nextInt()

        run buildGameData@{
            for (i in 0 until game.height) {
                for (j in 0 until game.width) {
                    val currentCell = Cell(
                        j,
                        i,
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        100
                    )
                    game.battleground[i][j] = currentCell
                    if (currentCell.owner == Ownership.ME) currentCell.let { game.myCells.add(it) }
                    if (currentCell.owner == Ownership.FREE && currentCell.scrapAmount > 0 && currentCell.units == 0) currentCell.let {
                        game.freeCells.add(
                            it
                        )
                    }
                    if (currentCell.owner == Ownership.ME && currentCell.units >= 1) currentCell.let {
                        game.myRobots.add(
                            Robot(j, i, currentCell)
                        )
                    }
                    if (currentCell.owner == Ownership.OPP) currentCell.let { game.freeCells.add(it) }
                }
            }
        }

        run manageSpawnAndBuild@{
            for (tile in game.myCells) {
                if (tile.canBuild) {
                    val shouldBuild = (tile.x == game.width / 2)
                    if (shouldBuild) {
                        actions.add(java.lang.String.format("BUILD %d %d", tile.x, tile.y))
                    }
                }
                if (tile.canSpawn) {
                    val amount = 1
                    if (amount > 0) {
                        actions.add(java.lang.String.format("SPAWN %d %d %d", amount, tile.x, tile.y))
                    }
                }
            }
        }

        run manageRobots@{
            // Separate robots for managing border and interior
            val borderRobots = game.myRobots.subList(0, 2)
            val interiorRobots = game.myRobots.subList(2, game.myRobots.size)

            // Manage border robots
            manageBorderRobots(game)

            // Manage interior robots
            manageInteriorRobots(game)
        }

        debug(actions.toString())
        if (actions.isEmpty()) {
            println("WAIT")
        } else {
            println(actions.stream().collect(Collectors.joining(";")))
        }
    }
}
