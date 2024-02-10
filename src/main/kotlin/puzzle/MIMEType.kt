package puzzle

import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val setSize = input.nextInt() // Number of elements which make up the association table.
    val fileSize = input.nextInt() // Number Q of file names to be analyzed.

    val setOfExtention = hashMapOf<String, String>()


    for (i in 0 until setSize) {
        setOfExtention[input.next().lowercase(Locale.getDefault())] = input.next()
    }
    System.err.println(setOfExtention.toString())
    input.nextLine()
    for (i in 0 until fileSize) {
        val FNAME = input.nextLine() // One file name per line.
        var result = FNAME.split(".").lastOrNull()?.let { setOfExtention.getOrDefault(it.lowercase(Locale.getDefault()), "UNKNOWN") }
        if(result == null || FNAME.split(".").size < 2) result = "UNKNOWN"
        System.err.println(FNAME + "=" + result)
        println(result)
    }

}