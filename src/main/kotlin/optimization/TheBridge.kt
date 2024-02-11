package optimization

import java.util.*
import kotlin.random.Random

fun main() {
    val program = TheBridge()
    program.run()
}


class TheBridge {
    private val input = Scanner(System.`in`)
    private val laneCount = 4
    private val motorBikeCount = input.nextInt() // the amount of motorbikes to control
    private val conditionToWin = input.nextInt() // the minimum amount of motorbikes that must survive
    private val roadInit = arrayOf(input.next(), input.next(), input.next(), input.next()).map { line -> line.split("").toMutableList() }
    private val motorbikes = mutableListOf<Moto>()
    private val moveMap = mapOf(0 to "SPEED", 1 to "SLOW", 2 to "JUMP", 3 to "WAIT", 4 to "UP", 5 to "DOWN")

    fun run() {
        debug(roadInit.map { line -> line.toString() + '\n' }.toString())

        // game loop
        while (true) {
            motorbikes.clear()
            val currentRoad = roadInit
            //for (line in currentRoad) line.addAll(Array(10) { "." }) //Add ten more step to each line to avoid index out of bound
            val motoSpeed = input.nextInt() // the motorbikes' speed

            for (i in 0 until motorBikeCount) {
                motorbikes.add(Moto(motoSpeed, input.nextInt()+1, input.nextInt(), (input.nextInt() == 1)))
                currentRoad[motorbikes[i].y][motorbikes[i].x] = "x"
            }



            val testTheBridge = GeneticAlgorithm(200, 0.1, 0.95, 10, 10, currentRoad as List<MutableList<String>>, motorbikes)
            var population = testTheBridge.initPopulation(5)
            //debug(population.individuals.map { i -> i.toString() })
            testTheBridge.evalPopulation(population)
            for(generation in 0 .. 20) {
                population = testTheBridge.crossoverPopulation(population);
                population = testTheBridge.mutatePopulation(population);
                testTheBridge.evalPopulation(population)
            }
            debug("Best solution: " + population.getFittest(0).toString() + " with score: " + population.getFittest(0).fitness.toString())

            //debug(motorbikes.toString())
            debug(currentRoad.map { line -> line.toString() + '\n' }.toString())

            // keywords: SPEED, SLOW, JUMP, WAIT, UP, DOWN.
            println(moveMap[population.getFittest(0).chromosome[0]])
        }
    }
    private fun debug(message: String) {
        System.err.println(message)
    }
}

class Moto(var speed: Int, var x: Int, var y: Int, var isActive: Boolean) {
    override fun toString(): String {
        return "($x, $y) - speed: $speed - active: $isActive"
    }
    fun isNextMoveSafe(currentRoad: List<MutableList<String>>): Boolean {
        for (i in x until (x + speed)) {
            if (currentRoad[y][i] == "0") return false
        }
        return true
    }
    fun executeMove(instruction: Int, lanes: List<MutableList<String>>) {
        //Gene dictionary: 0 -> "SPEED", 1 -> "SLOW", 2 -> "JUMP", 3 -> "WAIT", 4 -> "UP", 5 -> "DOWN"
        when (instruction) {
            0 -> {
                speed++
                for(i in 1 .. speed)
                    if(lanes[y][(x+i).coerceAtMost(lanes[y].size-1)] == "0") isActive = false
            }
            1 -> {
                speed = (speed-1).coerceAtLeast(0)
                for(i in 1 .. speed)
                    if(lanes[y][(x+i).coerceAtMost(lanes[y].size-1)] == "0") isActive = false
            }
            3 -> {
                for(i in 0 until speed)
                    if(lanes[y][(x+i).coerceAtMost(lanes[y].size-1)] == "0") isActive = false
            }
            4 -> {
                if(y-1 < 0) isActive = false
                y = (y - 1).coerceAtLeast(0)
                for(i in 0 until speed) {
                    if (lanes[y][(x + i).coerceAtMost(lanes[y].size - 1)] == "0") isActive = false
                    if (lanes[y+1][(x + i).coerceAtMost(lanes[y].size - 1)] == "0") isActive = false
                }
            }
            5 -> {
                if(y+1 >= lanes[y].size) isActive = false
                y = (y + 1).coerceAtMost(3)
                for(i in 0 until speed) {
                    if (lanes[y][(x + i).coerceAtMost(lanes[y].size - 1)] == "0") isActive = false
                    if (lanes[y-1][(x + i).coerceAtMost(lanes[y].size - 1)] == "0") isActive = false
                }
            }
        }
        if(lanes[y][(x+speed).coerceAtMost(lanes[y].size-1)] == "0") isActive = false
        x+=speed
    }

}
class GeneticAlgorithm(
    private val populationSize: Int,
    private val mutationRate: Double,
    private val crossoverRate: Double,
    private val elitismCount: Int,
    val tournamentSize: Int,
    private val road: List<MutableList<String>>,
    private val motorbikes: List<Moto>
) {
    fun initPopulation(chromosomeLength: Int): Population {
        return Population(
            populationSize,
            chromosomeLength
        )
    }
    private fun calcFitness(individual: Individual): Double {
        // Track number of correct genes
        var correctGenes = 0
        val motobikesTest: List<Moto> = motorbikes.map { moto -> Moto(moto.speed, moto.x, moto.y, moto.isActive) }
        // Loop over individual's genes
        for (geneIndex in 0 until individual.getChromosomeLength()) {
            // Add one fitness point for step without falling in a hole
            for(moto in motobikesTest) {
                moto.executeMove(individual.getGene(geneIndex), road)
                if(moto.isActive)
                    if(moto.x >= road[0].size)
                        correctGenes += 1000
                    else if(road[moto.y][moto.x] != "0") correctGenes += moto.x
                    else if(road[moto.y][moto.x] == "0") {
                        moto.isActive = false
                        correctGenes -= 1000
                    }
            }
        }
        // Store fitness
        individual.fitness = correctGenes.toDouble()
        return individual.fitness
    }
    fun evalPopulation(population: Population) {
        var populationFitness = 0.0
        for (individual in population.individuals) {
            populationFitness += calcFitness(individual)
        }
        population.populationFitness = populationFitness
    }
    fun isTerminationConditionMet(population: Population): Boolean {
        for (individual in population.individuals) {
            if (individual.fitness > 100.0) {
                return true
            }
        }
        return false
    }
    private fun selectParent(population: Population): Individual {
        // Get individuals
        val individuals: Array<Individual> = population.individuals
        // Spin roulette wheel
        val populationFitness: Double = population.populationFitness
        val rouletteWheelPosition = Math.random() * populationFitness
        // Find parent
        var spinWheel = 0.0
        for (individual in individuals) {
            spinWheel += individual.fitness
            if (spinWheel >= rouletteWheelPosition) {
                return individual
            }
        }
        return individuals[population.size() - 1]
    }
    fun crossoverPopulation(population: Population): Population {
        // Create new population
        val newPopulation = Population(population.size())
        // Loop over current population by fitness
        for (populationIndex in 0 until population.size()) {
            val parent1 = population.getFittest(populationIndex)
            // Apply crossover to this individual?
            if (crossoverRate > Math.random() && populationIndex > elitismCount) {
                // Initialize offspring
                val offspring = Individual(parent1.getChromosomeLength())
                // Find second parent
                val parent2 = selectParent(population)
                // Loop over genome
                for (geneIndex in 0 until parent1.getChromosomeLength()) {
                    // Use half of parent1's genes and half of parent2's genes
                    if (0.5 > Math.random()) {
                        offspring.setGene(
                            geneIndex,
                            parent1.getGene(geneIndex)
                        )
                    } else {
                        offspring.setGene(
                            geneIndex,
                            parent2.getGene(geneIndex)
                        )
                    }
                }
                // Add offspring to new population
                newPopulation.setIndividual(
                    populationIndex,
                    offspring
                )
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1)
            }
        }
        return newPopulation
    }
    fun mutatePopulation(population: Population): Population {
        // Initialize new population
        val newPopulation = Population(populationSize)
        // Loop over current population by fitness
        for (populationIndex in 0 until population.size()) {
            val individual = population.getFittest(populationIndex)
            // Loop over individual's genes
            for (geneIndex in 0 until individual.getChromosomeLength()) {
                // Skip mutation if this is an elite individual
                if (populationIndex >= elitismCount) {
                    // Does this gene need mutation?
                    if (mutationRate > Math.random()) {
                        // Get new gene
                        val newGene = 0
                        // Mutate gene
                        individual.setGene(geneIndex, newGene)
                    }
                }
            }
            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual)
        }
        // Return mutated population
        return newPopulation
    }
}
class Population {
    var individuals: Array<Individual>
    var populationFitness = -1.0
    constructor(populationSize: Int) {
        individuals = Array(populationSize){ Individual(populationSize) }
    }
    constructor(populationSize: Int, chromosomeLength: Int) {
        individuals = Array(populationSize){ Individual(chromosomeLength) }
        for (individualCount in 0 until populationSize) {
            val individual = Individual(chromosomeLength)
            individuals[individualCount] = individual
        }
    }
    fun getFittest(offset: Int): Individual {
        Arrays.sort(individuals, Comparator { o1, o2 ->
            if (o1.fitness > o2.fitness) {
                return@Comparator -1
            } else if (o1.fitness < o2.fitness) {
                return@Comparator 1
            }
            0
        })
        return individuals[offset]
    }
    fun size(): Int {
        return individuals.size
    }
    fun setIndividual(offset: Int, individual: Individual): Individual {
        return individual.also { individuals[offset] = it }
    }
    fun getIndividual(offset: Int): Individual {
        return individuals[offset]
    }
    fun shuffle() {
        for (i in individuals.size - 1 downTo 1) {
            val index = Random.nextInt(i + 1)
            val a = individuals[index]
            individuals[index] = individuals[i]
            individuals[i] = a
        }
    }
}
class Individual {
    //Gene dictionary: 0 -> "SPEED", 1 -> "SLOW", 2 -> "JUMP", 3 -> "WAIT", 4 -> "UP", 5 -> "DOWN"
    var chromosome: IntArray
    var fitness = 0.0
    constructor(chromosome: IntArray) {
        // Create individual chromosome
        this.chromosome = chromosome
    }
    constructor(chromosomeLength: Int) {
        chromosome = IntArray(chromosomeLength)
        for (gene in 0 until chromosomeLength) {
            setGene(gene,  Random.nextInt(0, 6))
        }
    }
    fun getChromosomeLength(): Int {
        return chromosome.size
    }
    fun setGene(offset: Int, gene: Int) {
        chromosome[offset] = gene
    }
    fun getGene(offset: Int): Int {
        return chromosome[offset]
    }
    override fun toString(): String {
        var output = ""
        for (gene in chromosome.indices) {
            output += chromosome[gene]
        }
        return output
    }
}