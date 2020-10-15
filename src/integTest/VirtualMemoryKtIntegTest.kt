package integTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import me.maria.*
import java.io.File


internal class VirtualMemoryKtIntegTest {

    @Test
    fun `incorrect format of RAM size`() {
        main(arrayOf("data/example1.txt"))
        val expected = File("data/results of examples/res1.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `incorrect format of address space`() {
        main(arrayOf("data/example2.txt"))
        val expected = File("data/results of examples/res2.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `incorrect number of lines`() {
        main(arrayOf("data/example3.txt"))
        val expected = File("data/results of examples/res3.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `small example`() {
        main(arrayOf("data/example4.txt"))
        val expected = File("data/results of examples/res4.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `big example`() {
        main(arrayOf("data/example5.txt"))
        val expected = File("data/results of examples/res5.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `with some invalid calls`() {
        main(arrayOf("data/example6.txt"))
        val expected = File("data/results of examples/res6.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `only invalid calls`() {
        main(arrayOf("data/example7.txt"))
        val expected = File("data/results of examples/res7.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `not existing file`() {
        main(arrayOf("dddd"))
        val expected = listOf(
            "File dddd processing result:",
            "java.io.FileNotFoundException: dddd (Нет такого файла или каталога)",
            "",
            "")
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `some files`() {
        main(arrayOf("data/example1.txt", "data/example4.txt", "data/example6.txt", "xxxxxx"))
        val res1 = File("data/results of examples/res1.txt").readLines()
        val res2 = File("data/results of examples/res4.txt").readLines()
        val res3 = File("data/results of examples/res6.txt").readLines()
        val res4 = listOf(
            "File xxxxxx processing result:",
            "java.io.FileNotFoundException: xxxxxx (Нет такого файла или каталога)",
            "",
            "")

        var expected = mutableListOf<String>().plus(res1).plus(res2).plus(res3).plus(res4)
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

}