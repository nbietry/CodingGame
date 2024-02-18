package puzzle

import java.util.*

fun findLongestPath(associationMap: Map<Int, List<Int>>, start: Int): List<Int> {
    val visited = mutableSetOf<Int>()
    val currentPath = mutableListOf<Int>()
    val longestPath = mutableListOf<Int>()

    dfs(associationMap, start, visited, currentPath, longestPath)

    return longestPath
}

fun dfs(
    associationMap: Map<Int, List<Int>>,
    currentVertex: Int,
    visited: MutableSet<Int>,
    currentPath: MutableList<Int>,
    longestPath: MutableList<Int>
) {
    visited.add(currentVertex)
    currentPath.add(currentVertex)

    val neighbors = associationMap[currentVertex] ?: emptyList()

    for (neighbor in neighbors) {
        if (neighbor !in visited) {
            dfs(associationMap, neighbor, visited, currentPath, longestPath)
        }
    }

    if (currentPath.size > longestPath.size) {
        longestPath.clear()
        longestPath.addAll(currentPath)
    }

    currentPath.removeAt(currentPath.size - 1)
    visited.remove(currentVertex)
}
fun main() {

    val input = Scanner(System.`in`)
    val inputSize = input.nextInt() // the number of relationships of influence
    val associationMap = mutableMapOf<Int, MutableList<Int>>()

    for (i in 0 until inputSize) {
        associationMap.computeIfAbsent(input.nextInt()) { mutableListOf() }.add(input.nextInt())
    }

    var longestPath: List<Int> = emptyList()

    for (vertex in associationMap.keys) {
        val currentPath = findLongestPath(associationMap, vertex)
        if (currentPath.size > longestPath.size) {
            longestPath = currentPath
        }
    }

}
