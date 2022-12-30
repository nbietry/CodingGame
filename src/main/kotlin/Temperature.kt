import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt() // the number of temperatures to analyse
    val listTemperature = ArrayList<Int>(n)
    for (i in 0 until n) {
        val t = input.nextInt()
        listTemperature.add(t)
    }
    val listEcart = listTemperature.toList().map { x->abs(x) }
    var finalList = ArrayList<Int>()
    for((index, value) in listEcart.withIndex())
        if(value == listEcart.min()) finalList.add(index)
    var solution = listTemperature.filterIndexed { index, i -> finalList.contains(index) }
    if(solution.isEmpty()) println("0")
    else println(solution.max().toString())
}