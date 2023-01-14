package puzzle

import java.util.*

fun main() {
    val input = Scanner(System.`in`)
    val program = RockPaperScissorsLizardSpock(input)
    program.run()
}

class RockPaperScissorsLizardSpock (val input: Scanner){
    var playerCount = 0
    var playerList = mutableListOf<Player>()
    private var tournament = mutableListOf<Player>()
    private var pairList = mutableListOf<Pair<Int, Int>>()
    fun run(){
        playerCount = input.nextInt()
        for (i in 0 until playerCount) playerList.add(Player(input.nextInt(), input.next()))
        tournament = playerList
        while (tournament.size > 1){
            pairList.addAll(tournament.chunked(2).map{round -> Pair(round[0].id, round[1].id)})
            tournament = tournament.chunked(2).map { round -> whoWin(round[0], round[1]) }.toMutableList()
            //System.err.println(tournament)
        }
        println(tournament[0].id)
        println(getOpponents(pairList, tournament[0]))
    }
    data class Player(val id:Int, val sign:String)
    fun whoWin(player1:Player, player2: Player): Player {
        if(player1.sign == "C" && player2.sign == "P") return player1
        if(player1.sign == "P" && player2.sign == "R") return player1
        if(player1.sign == "R" && player2.sign == "L") return player1
        if(player1.sign == "L" && player2.sign == "S") return player1
        if(player1.sign == "S" && player2.sign == "C") return player1
        if(player1.sign == "C" && player2.sign == "L") return player1
        if(player1.sign == "L" && player2.sign == "P") return player1
        if(player1.sign == "P" && player2.sign == "S") return player1
        if(player1.sign == "S" && player2.sign == "R") return player1
        if(player1.sign == "R" && player2.sign == "C") return player1

        if(player2.sign == "C" && player1.sign == "P") return player2
        if(player2.sign == "P" && player1.sign == "R") return player2
        if(player2.sign == "R" && player1.sign == "L") return player2
        if(player2.sign == "L" && player1.sign == "S") return player2
        if(player2.sign == "S" && player1.sign == "C") return player2
        if(player2.sign == "C" && player1.sign == "L") return player2
        if(player2.sign == "L" && player1.sign == "P") return player2
        if(player2.sign == "P" && player1.sign == "S") return player2
        if(player2.sign == "S" && player1.sign == "R") return player2
        if(player2.sign == "R" && player1.sign == "C") return player2
        return if(player1.id < player2.id) player1 else player2
    }
    fun getOpponents(roundList: MutableList<Pair<Int, Int>>, player: Player): String{
        val playerRounds = roundList.filter { pair -> pair.first == player.id || pair.second == player.id }
        return playerRounds.map { pair -> if(pair.first == player.id) pair.second else pair.first }.joinToString("")
    }
}