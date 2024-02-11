import java.util.*

enum class KitchenElement(val symbol: Char) {
    EMPTY('.'),
    CHEF_1('0'),
    CHEF_2('1'),
    WORK_TABLE('#'),
    DISHWASHER('D'),
    WINDOW('W'),
    BLUEBERRIES('B'),
    ICE_CREAM('I'),
    CHOPPED_STRAWBERRIES('S'),
    CROISSANT('H'),
    CUTTING_BOARD('C'),
    OVEN('O')
}
data class Customer(val receipt: String, val award: Int)
data class Player(val x: Int, val y: Int, val item: String)
data class Item(val x: Int, val y: Int, val name: String)
data class Oven(val contents: String, val timer: Int)
class KitchenGame {
    private val input = Scanner(System.`in`)

    private var turnsRemaining: Int = 0
    private lateinit var player: Player
    private lateinit var partner: Player
    private val tablesWithItems = mutableListOf<Item>()
    private lateinit var oven: Oven
    private val customers = mutableListOf<Customer>()


    private fun readKitchenMap(): List<List<KitchenElement>> {
        val rows = mutableListOf<List<KitchenElement>>()
        repeat(7) {
            val row = input.next().toList().map { char ->
                KitchenElement.values().firstOrNull { it.symbol == char } ?: KitchenElement.EMPTY
            }
            rows.add(row)
        }
        return rows
    }
    private fun readCustomerList(): MutableList<Customer>{
        val customers = mutableListOf<Customer>()
        val numAllCustomers = input.nextInt()
        repeat(numAllCustomers) {
            val customerItem = input.next()
            val customerAward = input.nextInt()
            customers.add(Customer(customerItem, customerAward))
        }
        return customers
    }
    private fun readGameRound() {
        turnsRemaining = input.nextInt()
        player = Player(input.nextInt(), input.nextInt(), input.next())
        partner = Player(input.nextInt(), input.nextInt(), input.next())

        val numTablesWithItems = input.nextInt()
        tablesWithItems.clear()
        repeat(numTablesWithItems) {
            val tableX = input.nextInt()
            val tableY = input.nextInt()
            val itemName = input.next()
            tablesWithItems.add(Item(tableX, tableY, itemName))
        }

        oven = Oven(input.next(), input.nextInt())

        val numCustomers = input.nextInt()
        customers.clear()
        repeat(numCustomers) {
            val customerItem = input.next()
            val customerAward = input.nextInt()
            customers.add(Customer(customerItem, customerAward))
        }
    }
    private fun getLeastValuableReceipt(): Customer {
        return customers.minBy { it.award }
    }
    private fun getItemLocation(itemName: String): Pair<Int, Int> {
        val itemOnTable = tablesWithItems.find { it.name == itemName }
        return itemOnTable!!.let { Pair(it.x, it.y) }
    }
    private fun performActions() {
        val leastValuableReceipt = getLeastValuableReceipt()

        val receiptItems = leastValuableReceipt.receipt.split("-")
        for (item in receiptItems) {
            println("MOVE " + getItemLocation(item).first + " " + getItemLocation(item).second)
        }
    }
    fun playGame() {
        val customerList = readCustomerList()
        val kitchenMap = readKitchenMap()

        // Game loop
        while (true) {
            readGameRound()
            performActions()
        }
    }
}

fun main() {
    val game = KitchenGame()
    game.playGame()
}
