package puzzle

import java.util.*
import kotlin.math.abs

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val game = CablageReseau()
    game.loadInitData(input)
    println("answer")
}

class CablageReseau{
    private val houses = mutableListOf<Pos>()
    fun loadInitData(input: Scanner){
        val N = input.nextInt()
        for (i in 0 until N) {
            houses.add(Pos(input.nextInt(), input.nextInt()))
        }
        val medX = med(houses.map { it.x.toLong() })
        debug("Median: $medX")
        println(houses.sumOf { abs(it.x - medX )} + houses.maxOf{it.y} - houses.minOf{it.y})
    }

    fun med(list: List<Long>) = list.sorted().let {
        if (it.size % 2 == 0)
            (it[it.size / 2] + it[(it.size - 1) / 2]) / 2
        else
            it[it.size / 2]
    }
}