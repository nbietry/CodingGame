import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val width = input.nextInt() // the number of cells on the X axis
    val height = input.nextInt() // the number of cells on the Y axis
    if (input.hasNextLine()) {
        input.nextLine()
    }
    val inputLines = Array(height){CharArray(width){' '}}
    System.err.println("Widht: $width, Height: $height")
    for (i in 0 until height) {
        inputLines[i] = input.nextLine().toCharArray()
        System.err.println(inputLines[i])
    }

    for (i in 0 until height) {
        for(j in 0 until width) {
            if(inputLines[i].elementAt(j) != '.')
                println(j.toString() + " " + i.toString() + " " +
                        checkRight(inputLines[i], j+1, i) + " " +
                        checkDown(inputLines, i+1, j)
                )
        }
    }
}

fun checkRight(input: CharArray, indexX: Int, indexY: Int): String{
    return if(input.getOrNull(indexX) == null ) "-1 -1"
    else if (input.getOrNull(indexX) == '.') checkRight(input, indexX+1, indexY)
    else "$indexX $indexY"
}
fun checkDown(input: Array<CharArray>, indexX: Int, indexY:Int): String{
    return if(input.getOrNull(indexX)?.getOrNull(indexY) == null) "-1 -1"
    else if (input.getOrNull(indexX)?.getOrNull(indexY) == '.') checkDown(input, indexX+1, indexY)
    else "$indexY $indexX"
}