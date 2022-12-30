import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val message = input.nextLine()
    val sequence = message.toCharArray().map { char -> char.code.toString(2).padStart(7, '0') }.joinToString("").toCharArray()
    var i = 0
    var step=0
    var result = ""
    for(word in sequence)
        while (i < sequence.size && step < 100){
            if(sequence[i] == '0') {
                result += " 00 0"
                i++
                while (i < sequence.size && sequence[i] == '0') {
                    result += "0"
                    i++
                }
            }else if(sequence[i] == '1') {
                result += " 0 0"
                i++
                while (i < sequence.size && sequence[i] == '1') {
                    result += "0"
                    i++
                }
            }
            step++
        }
    println(result.substring(1))
}