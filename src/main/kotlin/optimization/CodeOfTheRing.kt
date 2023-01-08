package optimization

import kotlin.math.*
import java.util.*

fun main() {
    val input = Scanner(System.`in`)
    val program = CodeOfTheRing(input.nextLine())
    program.run()
}
class CodeOfTheRing(private val magicPhrase: String) {
    private val dictionary = LinkedList(arrayListOf(' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' ))
    private val stones = LinkedList(List(30){' '})
    private val response = mutableListOf<Char>()
    private var blubPosition = 0
    fun run() {
        val magicPhraseFactorized = factorizeMagicPhrase()
        for(letter in magicPhraseFactorized){
            if(letter.size < 7) {
                // Number of char need less char than a loop
                val bestStone = findBestStone(letter.first())
                stones[bestStone.stone] = letter.first()
                response.addAll(moveToBestStone(bestStone.stone).toCharArray().toList())
                response.addAll(bestStone.move.toCharArray().toList())
            }else{
                // Generate a loop in the response to optimize number of instructions
                val bestStone = findBestStone(letter.first())
                response.addAll(moveToBestStone(bestStone.stone).toCharArray().toList())
                response.addAll(generateLoopForLetter(letter, bestStone).toList())
            }
        }
        println(response.joinToString(""))
    }
    private fun factorizeMagicPhrase(): MutableList<List<Char>> {
        val result = magicPhrase.toCharArray().fold(mutableListOf<Char>() to mutableListOf<List<Char>>()) { (currentList, allLists), currentItem ->
            if (currentList.isEmpty()) { // Applies only to the very first item
                mutableListOf(currentItem) to allLists
            } else {

                if (currentItem == currentList.first()) { // same char
                    currentList.apply { add(currentItem) } to allLists
                } else {
                    mutableListOf(currentItem) to allLists.apply { add(currentList) } // Next
                }

            }
        }
            .let { it.second.apply { add(it.first) } }
        return result
    }
    fun generateLoopForLetter(letter: List<Char>, bestStone: Instruction): CharArray{
        //TO BE IMPLEMENTED
        val loopStructure = "[<.>-]"
        var response = ""
        for(i in 0 .. letter.size / 26) {
            val currentSize = letter.size - (26 * i)
            val counterChar = if (letter.size < 26) dictionary[letter.size] else dictionary.last.toChar()
        }
        return "".toCharArray()
    }
    fun moveToBestStone(bestStone: Int): String {
        val steps = bestStone - blubPosition
        blubPosition = bestStone
        return if (steps > 0)
            if (steps > 15) "<".repeat(30 - steps)
            else ">".repeat(steps)
        else
            if (abs(steps) > 15)">".repeat(30 - abs(steps))
            else "<".repeat(abs(steps))
    }
    private fun findBestStone(letter: Char) : Instruction {
        val bestResponseForLetter = LinkedList<MutableList<Char>>()

        for((index, stone) in stones.withIndex()) {
            val localResponse = mutableListOf<Char>()
            var test = getGap(letter, stone)
            if (test >= 15) test = (27 - dictionary.indexOf(letter) + dictionary.indexOf(stone)) * -1
            if (test <= -15) test = (27 - dictionary.indexOf(stone) + dictionary.indexOf(letter))
            if (test < 0) {
                for (i in 0 until abs(test)) localResponse.add('-')
                localResponse.add('.')
            }
            if (test > 0) {
                for (i in 0 until abs(test)) localResponse.add('+')
                localResponse.add('.')
            }
            if (test == 0) localResponse.add('.')
            bestResponseForLetter.add(localResponse)
        }
        val (indexBestResponse, bestResponse) = bestResponseForLetter.withIndex().minBy { it.value.size}
        return (Instruction(indexBestResponse, bestResponse.joinToString("")))
    }
    fun getGap(in1: Char, in2: Char): Int{
        return dictionary.indexOf(in1) - dictionary.indexOf(in2)
    }
    data class Instruction(val stone: Int, val move:String)
}