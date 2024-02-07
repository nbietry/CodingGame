package puzzle
import java.util.*

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val program = LaBataille(input)
    program.run()
}

data class Card(val value: Int, val color: Char){
    companion object {
        val possibleValue = listOf("2", "3", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
    }
}
class LaBataille(val input: Scanner) {
    private val player1Cards = LinkedList<Card>()
    private val player2Cards = LinkedList<Card>()
    private val boardPlayer1 = LinkedList<Card>()
    private val boardPlayer2 = LinkedList<Card>()
    //init games counter
    private var gamesCount = 0
    fun run() {

        //Load input data
        loadInputData()

        //poll a card for both players
        while(player1Cards.isNotEmpty() && player2Cards.isNotEmpty()) {
            drawCards(1)
            var winner = checkBoardCards()
            while(winner == 0) {
                debug("Count1: " + player1Cards.count() + " count2: " + player2Cards.count())
                if(player1Cards.count() < 4 || player2Cards.count() < 4) winner = -1
                else {
                    drawCards(3)
                    drawCards(1)
                    winner = checkBoardCards()
                }
            }
            collectBoard(winner)
            gamesCount++
        }

        if(player2Cards.isEmpty())
            if(player1Cards.isEmpty())
                println("PAT")
            else
                println("1 $gamesCount")
        else
            println("2 $gamesCount")

    }
    private fun drawCards(numberOfCard: Int) {
        //debug("Draw $numberOfCard cards")
        //debug("player1: $player1Cards")
        //debug("player2: $player2Cards")
        repeat(numberOfCard) {
            boardPlayer1.add(player1Cards.remove())
            boardPlayer2.add(player2Cards.remove())
        }
    }
    private fun collectBoard(playerWinner: Int){
        if (playerWinner == 0) return
        if(playerWinner == -1) {
            player1Cards.clear()
            player2Cards.clear()
        }else{
            if (playerWinner == 2) {
                player2Cards.addAll(boardPlayer1)
                player2Cards.addAll(boardPlayer2)
            } else {
                player1Cards.addAll(boardPlayer1)
                player1Cards.addAll(boardPlayer2)
            }
        }
        //Clear the boards
        boardPlayer1.clear()
        boardPlayer2.clear()
    }
    private fun checkBoardCards(): Int {
        //Check bataille (values are equals
        if(boardPlayer1.last.value == boardPlayer2.last.value) return 0
        return if (boardPlayer1.last.value < boardPlayer2.last.value) {
            2
        } else {
            1
        }
    }
    private fun loadInputData() {
        val player1CardsCount = input.nextInt() // the number of cards for player 1
        for (i in 0 until player1CardsCount) {
            val card = input.next()
            player1Cards.add(Card(Card.possibleValue.indexOf(card.dropLast(1)), card.last()))
        }
        val player2CardsCount = input.nextInt() // the number of cards for player 2
        for (i in 0 until player2CardsCount) {
            val card = input.next() // the m cards of player 2
            player2Cards.add(Card(Card.possibleValue.indexOf(card.dropLast(1)), card.last()))
        }
        debug("start player1: $player1Cards")
        debug("start player2: $player2Cards")
    }
    private fun debug(message: String) {
        System.err.println(message)
    }
}