package optimization

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CodeOfTheRingTest{

    @Test
    fun test_space_code(){
        println("Space = " + ' '.code.toString())
        println("A = " + 'A'.code.toString())
        val program = CodeOfTheRing("AAAAAAABBBBBBBBCCCCCCCCCCCCBBBB")
        program.run()
    }

    @Test
    fun `moveToBestStone should return correct string of instructions on right`() {
        val codeOfTheRing = CodeOfTheRing("AAAAAAABBBBBBBBCCCCCCCCCCCCBBBB")
        val bestStone = 10
        val expected = ">>>>>>>>>>"
        val result = codeOfTheRing.moveToBestStone(bestStone)
        assertEquals(expected, result)
    }
    @Test
    fun `moveToBestStone should return correct string of instructions on left`() {
        val codeOfTheRing = CodeOfTheRing("AAAAAAABBBBBBBBCCCCCCCCCCCCBBBB")
        val bestStone = 28
        val expected = "<<"
        val result = codeOfTheRing.moveToBestStone(bestStone)
        assertEquals(expected, result)
    }
    @Test
    fun `generateLoopForLetter should return correct string of instruction`(){
        val codeOfTheRing = CodeOfTheRing(List<Char>(70){'B'}.joinToString(""))
        val result = codeOfTheRing.generateLoopForLetter(List<Char>(70){'B'}, CodeOfTheRing.Instruction(3, ">>"))
        assertEquals("++>-[<.>-]-[<.>-]---------[<.>-]", result.joinToString(""))
    }
    @Test
    fun test_chaine_sans_doublon(){
        println("Space = " + ' '.code.toString())
        println("A = " + 'A'.code.toString())
        val program = CodeOfTheRing("MINAS")
        program.run()
    }

    @Test
    fun should_retur_valid_solution_avec_factorisation(){
        val program = CodeOfTheRing("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
        //assertEquals("++>-[<.>-]-[<.>-]---------[<.>-]")
    }
}

