package puzzle
import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val program = PerteEnBourse(input)
    program.run()
}
class PerteEnBourse (val input: Scanner) {

    private val valueRange = mutableListOf<Int>()
    fun run() {
        val valuesCount = input.nextInt()
        for (i in 0 until valuesCount) {
            valueRange.add(input.nextInt())
        }

        val maxGap = valueRange
            .reversed()
            .drop(1)
            .fold(valueRange.first()) { acc, currentElement ->
                val currentGap = currentElement - acc
                if (currentGap > acc) currentGap else acc
            }

        val result = if (maxGap < 0) 0 else -maxGap
        println(result)

    }
}

