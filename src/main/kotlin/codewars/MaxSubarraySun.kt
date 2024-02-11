val alternateValue = mapOf("1" to "124", "2" to "1235", "3" to "236", "4" to "1457", "5" to "24568", "6" to "3569", "7" to "478", "8" to "57890", "9" to "689", "0" to "80")

fun getPINs(observed: String): List<String> {
    val alternateList = observed.toCharArray().map { alternateValue.getOrDefault(it.toString(), "") }
    var result = alternateList[0].toCharArray().map { it.toString() }
    for(i in 1 until alternateList.size)
        result = result.map { elem -> alternateList[i].map{ elem2 -> elem + elem2.toString() } }.flatten()
    return result
}

fun main(){
    println(getPINs("36"))
}