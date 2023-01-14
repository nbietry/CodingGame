package puzzle

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class RockPaperScissorsLizardSpockTest {

    private val testEnv = RockPaperScissorsLizardSpock(Scanner(System.`in`))

    @Test
    fun `should return Scissors winner against Paper`() {
        val player1 = RockPaperScissorsLizardSpock.Player(1, "C")
        val player2 = RockPaperScissorsLizardSpock.Player(2, "P")
        assertEquals(testEnv.whoWin(player1, player2), RockPaperScissorsLizardSpock.Player(1, "C"))
    }
}