package unitTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import me.maria.*


internal class FifoAlgorithm {

    @Test
    fun oneFrame() {
        val expected = listOf<String>("1*", "+", "1", "1")
        val actual = fifoAlgorithm(1, listOf(2, 2, 1, 5))
        assertEquals(expected, actual)
    }

    @Test
    fun otherSize() {
        val expected = listOf<String>("1*", "+", "2*", "3*", "1", "2", "+", "3", "1", "2")
        val actual = fifoAlgorithm(3, listOf(6, 6, 1, 2, 3, 8, 2, 1, 2, 10))
        assertEquals(expected, actual)
    }
}


internal class IncreasingAge {

    @Test
    fun zeroMap() {
        val expected = mutableMapOf<Int, FrameOfMemory>()
        val actual = increasingAge(mutableMapOf())
        assertEquals(expected, actual)
    }

    @Test
    fun notZeroMap() {
        val memory = mutableMapOf<Int, FrameOfMemory>(6 to FrameOfMemory(2, 4), 1 to FrameOfMemory(3, 6))
        val expected = mutableMapOf<Int, FrameOfMemory>(7 to FrameOfMemory(2, 4), 2 to FrameOfMemory(3, 6))
        val actual = increasingAge(memory)
        assertEquals(expected, actual)
    }
}


internal class ResettingAge {

    @Test
    fun resetting() {
        val memory = mutableMapOf<Int, FrameOfMemory>(6 to FrameOfMemory(2, 4), 2 to FrameOfMemory(3, 6))
        val expected = mutableMapOf<Int, FrameOfMemory>(0 to FrameOfMemory(2, 4), 2 to FrameOfMemory(3, 6))
        val actual = resettingAge(memory, 4)
        assertEquals(expected, actual)
    }
}


internal class LruAlgorithm {

    @Test
    fun oneFrame() {
        val expected = listOf<String>("1*", "+", "1", "1")
        val actual = lruAlgorithm(1, listOf(2, 2, 1, 5))
        assertEquals(expected, actual)
    }

    @Test
    fun otherSize() {
        val expected = listOf<String>("1*", "+", "2*", "3*", "1", "2", "+", "1", "+", "2")
        val actual = lruAlgorithm(3, listOf(6, 6, 1, 2, 3, 8, 2, 1, 2, 10))
        assertEquals(expected, actual)
    }
}


internal class ReductionOfTimeToCall {

    @Test
    fun zeroMap() {
        val expected = mutableMapOf<Int, FrameOfMemory>()
        val actual = reductionOfTimeToCall(mutableMapOf())
        assertEquals(expected, actual)
    }

    @Test
    fun notZeroMap() {
        val memory = mutableMapOf<Int, FrameOfMemory>(8 to FrameOfMemory(2, 4), 3 to FrameOfMemory(3, 6))
        val expected = mutableMapOf<Int, FrameOfMemory>(7 to FrameOfMemory(2, 4), 2 to FrameOfMemory(3, 6))
        val actual = reductionOfTimeToCall(memory)
        assertEquals(expected, actual)
    }
}


internal class InstallOfTimeToCall {

    @Test
    fun install() {
        val sequenceOfCalls = listOf<Int>(2, 3, 4, 4, 5, 3)
        val memory = mutableMapOf<Int, FrameOfMemory>(6 to FrameOfMemory(2, 4), 2 to FrameOfMemory(5, 3))
        val expected = mutableMapOf<Int, FrameOfMemory>(6 to FrameOfMemory(2, 4), 4 to FrameOfMemory(5, 3))
        val actual = installOfTimeToCall(memory, 3, sequenceOfCalls, 1)
        assertEquals(expected, actual)
    }
}


internal class NewTimeToCall {

    @Test
    fun willBeCall() {
        val sequenceOfCalls = listOf<Int>(2, 3, 4, 4, 5, 3)
        val expected = 4
        val actual = newTimeToCall(sequenceOfCalls, 1, 3)
        assertEquals(expected, actual)
    }

    @Test
    fun willBeNotCall() {
        val sequenceOfCalls = listOf<Int>(2, 3, 4, 4, 5, 2)
        val expected = 18
        val actual = newTimeToCall(sequenceOfCalls, 1, 3)
        assertEquals(expected, actual)
    }
}


internal class OptAlgorithm {

    @Test
    fun oneFrame() {
        val expected = listOf<String>("1*", "+", "1", "1")
        val actual = optAlgorithm(1, listOf(2, 2, 1, 5))
        assertEquals(expected, actual)
    }

    @Test
    fun otherSize() {
        val expected = listOf<String>("1*", "+", "2*", "3*", "1", "1", "+", "+", "+", "3")
        val actual = optAlgorithm(3, listOf(6, 6, 1, 2, 3, 8, 2, 1, 2, 10))
        assertEquals(expected, actual)
    }
}


internal class NumberOfAnswersOfTheSecondType() {

    @Test
    fun allAnswersOfSecondType() {
        val sequenceOfAnswer = listOf<String>("1*", "2*", "3*", "1", "3")
        val expected = Pair(5, "ALGO")
        val actual = numberOfAnswersOfTheSecondType(sequenceOfAnswer, "ALGO")
        assertEquals(expected, actual)
    }

    @Test
    fun notAllAnswersOfSecondType() {
        val sequenceOfAnswer = listOf<String>("1*", "+", "2*", "3*", "1", "1", "+", "+", "+", "3")
        val expected = Pair(6, "ALGO")
        val actual = numberOfAnswersOfTheSecondType(sequenceOfAnswer, "ALGO")
        assertEquals(expected, actual)
    }
}


internal class SortAlgorithms() {

    @Test
    fun notEqualRes() {
        val resFIFO = listOf<String>("1*", "+", "2*", "3*", "1", "2", "+", "3", "1", "2")
        val resLRU = listOf<String>("1*", "+", "2*", "3*", "1", "1", "+", "+", "2", "3")
        val resOPT = listOf<String>("1*", "+", "2*", "3*", "1", "1", "+", "+", "+", "3")

        val actual = sortAlgorithms(resFIFO, resLRU, resOPT)
        val expected = mutableListOf<Pair<Int, String>>(Pair(6, "OPT"), Pair(7, "LRU"), Pair(8, "FIFO"))
        assertEquals(expected, actual)
    }

    @Test
    fun equalRes() {
        val resFIFO = listOf<String>("1*", "+", "2*", "3*", "1",)
        val resLRU = listOf<String>("1*", "+", "2*", "3*", "1")
        val resOPT = listOf<String>("1*", "+", "2*", "3*", "2")

        val actual = sortAlgorithms(resFIFO, resLRU, resOPT)
        val expected = mutableListOf<Pair<Int, String>>(Pair(4, "FIFO"), Pair(4, "LRU"), Pair(4, "OPT"))
        assertEquals(expected, actual)
    }
}