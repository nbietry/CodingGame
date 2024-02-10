package puzzle

import java.util.*

fun main(args : Array<String>) {
    val program = DeathFirstSearch()
    program.run()
}

class DeathFirstSearch(){

    data class Node(var code: Int, var visited : Boolean = false, var distance: Int = Int.MAX_VALUE, var parent: Int = -1);
    private val nodes: MutableList<Node> = mutableListOf()
    private val gateways: MutableList<Node> = mutableListOf()
    private val links: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()

    fun run(){
        val input = Scanner(System.`in`)
        val (nodesCount, linksCount, exitCount) = listOf(input.nextInt(), input.nextInt(), input.nextInt()) // the total number of nodes in the level, including the gateways

        (0 until nodesCount).forEach { nodes.add(Node(it)) }
        (0 until nodesCount).forEach { _ -> links.add(mutableListOf()) }

        for (i in 0 until linksCount) {
            val tmpLink = Pair(input.nextInt(), input.nextInt())
            links[tmpLink.first].add(Pair(tmpLink.second, 1))
            links[tmpLink.second].add(Pair(tmpLink.first, 1))
        }

        for (i in 0 until exitCount) {
            gateways.add(Node(input.nextInt()))
        }

        // game loop
        while (true) {
            val agentNode = input.nextInt()
            val listNodes = bfs(agentNode)
            val distanceGateway = mutableListOf<Node>()
            gateways.forEach { gateway ->
                listNodes.firstOrNull { it.code == gateway.code }?.let { distanceGateway.add(it) }
            }
            distanceGateway.sortBy { it.distance }
            var nodeToCut = distanceGateway.first()
            while (nodeToCut.parent != agentNode)
                nodeToCut = nodes[nodeToCut.parent]

            debug(nodeToCut.toString())
            debug("Distance gateway: " + distanceGateway.first().toString())

            println(nodeToCut.code.toString() + " " + nodeToCut.parent.toString())
            links[nodeToCut.code].remove(Pair(nodeToCut.parent, 1))
            links[nodeToCut.parent].remove(Pair(nodeToCut.code, 1))
            nodes.forEach {
                it.visited = false
                it.distance = Int.MAX_VALUE
                it.parent = -1
            }
        }
    }
    fun dfs(u: Int) {
        nodes[u].visited = true
        print(" $u")
        (0 until links[u].size).forEach {
            val tv = links[u][it]
            if(!nodes[tv.first].visited) dfs(tv.first)
        }
    }
    fun bfs(u: Int): List<Node> {
        val queue = mutableListOf<Int>()
        nodes[u].distance = 0
        queue.add(u)

        while(queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            if(!nodes[current].visited) {
                nodes[current].visited = true
                links[current].forEach {
                    if (nodes[it.first].distance == Int.MAX_VALUE) {
                        nodes[it.first].distance = nodes[current].distance + it.second
                        nodes[it.first].parent = nodes[current].code
                        queue.add(it.first)
                    }
                }
            }
        }
        debug(links.toString())
        debug(nodes.filter { it.visited }.toString())
        return nodes.filter { it.visited }
    }

    private fun debug(message: String){
        System.err.println(message)
    }
}

