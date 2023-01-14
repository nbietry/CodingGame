package puzzle

import java.util.*
import kotlin.math.cos
import kotlin.math.sqrt
import kotlin.math.pow

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val program = Defibrillator(input)
    program.run()
}

class Defibrillator(val input: Scanner) {
    var longitude = 0.0
    var latitude = 0.0
    var defibrillatorCount = 0
    val defibrillatorList = mutableListOf<MutableList<String>>()
    fun run() {
        longitude = input.next().replace(',','.').toDouble()
        latitude = input.next().replace(',','.').toDouble()
        defibrillatorCount = input.nextInt()
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until defibrillatorCount) {
            defibrillatorList.add(input.nextLine().split(";").toMutableList())
        }
        println(defibrillatorList.minBy { d -> getDistance(latitude, longitude, d[5].replace(',','.').toDouble(), d[4].replace(',','.').toDouble()) }[1])
    }

    private fun getDistance(latA: Double, lonA: Double, latB: Double, lonB: Double ): Double{
        val x = (lonB - lonA) * cos((latA + latB) / 2)
        val y = (latB - latA)
        return sqrt(x.pow(2.0) + y.pow(2.0)) * 6371
    }
}