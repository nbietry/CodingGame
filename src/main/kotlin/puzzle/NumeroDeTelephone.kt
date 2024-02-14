package puzzle

import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)

    val N = input.nextInt()
    val telephones = mutableListOf<String>()
    for (i in 0 until N) {
        telephones.add(input.next())
    }

    val telephone = "0467123456"
    val graph = Node('R', mutableListOf())

    for(tel in telephones){
        traverseGraph(graph, tel.toList())
    }

    println((countNodesInGraph(graph)-1).toString())
}

data class Node(val num: Char, val children: MutableList<Node>){
    constructor(num: Char): this(num, mutableListOf())
}

fun traverseGraph(currentNode: Node, telephone: List<Char>){
    val number = telephone.toList().first()
    var newNode = currentNode.children.firstOrNull { it.num == number }
    if( newNode == null){
        newNode = Node(number)
        currentNode.children.add(newNode)
    }
    if(telephone.size > 1)
        traverseGraph(newNode, telephone.drop(1))
}

fun countNodesInGraph(root: Node): Int {
    var count = 1 // Count the root node
    for (child in root.children) {
        count += countNodesInGraph(child)
    }
    return count
}