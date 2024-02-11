package puzzle

import optimization.GeneticAlgorithm
import optimization.Moto
import org.junit.jupiter.api.Test

internal class TheBridgeTest {

    @Test
    fun main() {
        val road = listOf(
            listOf(".", ".", ".", ".", ".", "0", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."),
            listOf(".", ".", ".", ".", ".", "0", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."),
            listOf(".", ".", ".", ".", ".", "0", ".", ".", ".", ".", ".", ".", "0", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."),
            listOf(".", ".", ".", ".", ".", "0", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".")
        )

        val motorbikes = mutableListOf<Moto>()
        motorbikes.add(Moto(0, 0, 2, true))

        val moveMap = mapOf(0 to "SPEED", 1 to "SLOW", 2 to "JUMP", 3 to "WAIT", 4 to "UP", 5 to "DOWN")

        val testTheBridge = GeneticAlgorithm(200, 0.1, 0.95, 2, 10, road as List<MutableList<String>>, motorbikes)
        var population = testTheBridge.initPopulation(20)
        println(population.individuals.map { i -> i.toString() })
        testTheBridge.evalPopulation(population)
        println("Best solution: " + population.getFittest(0).toString() + " with score: " + population.getFittest(0).fitness.toString())
        for(generation in 0 .. 10) {
            population = testTheBridge.crossoverPopulation(population);
            population = testTheBridge.mutatePopulation(population);
            testTheBridge.evalPopulation(population)
            println("Best solution Generation $generation: " + population.getFittest(0).toString() + " with score: " + population.getFittest(0).fitness.toString())
        }
        for(instruction in population.getFittest(0).chromosome) {
            print(moveMap[instruction] + " ")
        }
        println()
        println(road.map { line -> line.toString() + '\n' }.toString())
    }
}