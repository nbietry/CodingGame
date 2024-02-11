package puzzle

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

fun main() {

    val program = ChevauxDeCourses()
    program.run()
}

class ChevauxDeCourses {

    fun run() {
        val input = Scanner(System.`in`)
        val N = input.nextInt()
        var puissanceList = ArrayList<Int>(N)
        for (i in 0 until N) {
           puissanceList.add(input.nextInt())
        }
        puissanceList.sort()
        val puissanceEcart = puissanceList.indices.map{i->abs(puissanceList[i] - puissanceList.getOrElse(i+1)
        { 0
        }) }
        debug("$puissanceList $puissanceEcart")
        println(puissanceEcart.min().toString())
    }

    private fun debug(message: String){System.err.println(message)}

}