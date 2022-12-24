import java.util.*
import java.io.*
import java.math.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val L = input.nextInt()
    val H = input.nextInt()
    System.err.println("$L, $H")
    if (input.hasNextLine()) {
        input.nextLine()
    }
    val T = input.nextLine()
    val refAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val alphabet = MutableList(H) { emptyList<String>() }
    System.err.println("Text: $T")
    for (i in 0 until H) {
        val row = input.nextLine()
        alphabet[i] = row.toString().chunked(L)
    }
    //System.err.println(alphabet.toString())

    var response = ""
    for (line in 0 until H){
        for(letter in T.toCharArray()) {
            //System.err.println("Calcul ($T): " + (letter.code - 'A'.code).toString())
            if(refAlphabet.find { char -> char == letter.uppercaseChar() } != null)
                response += alphabet[line][letter.uppercaseChar().code - 'A'.code]
            else
                response += alphabet[line][alphabet[line].size]
        }
        response += '\n'
    }
    println(response)
}

