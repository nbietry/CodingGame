package puzzle

import java.math.BigInteger
import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    var R = listOf("2")
    val L = 10

    val numberList = mutableListOf<Pair<String, Int>>()
    repeat(L-1) {
        numberList.clear()
        var currentNumber: String? = null
        var counter = 0
        for (pointer in R.indices) {
            if (R[pointer] != currentNumber) {
                if (pointer > 0) numberList.add(Pair(currentNumber!!, counter))
                currentNumber = R[pointer]
                counter = 1
            } else {
                counter++
            }
        }
        numberList.add(Pair(currentNumber!!, counter))
        R = numberList.flatMap { listOf(it.second.toString(),it.first) }
    }
    if(numberList.size == 0) println(R.first())
    else println(numberList.joinToString(" ") { it.second.toString() + " " + it.first })
}

