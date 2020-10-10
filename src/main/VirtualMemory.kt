package me.maria

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * This program implements the work of algorithms for working with memory FIFO, LRU, OPT and compares their results.
 * @param file.txt (or some files)
 */

/**
 * This class indicates that the file is not in the correct format.
 */
class IncorrectFormat(message: String): Exception(message)

/**
 * This class stores information about which page is in the memory frame.
 */
data class FrameOfMemory(val numberOfFrame: Int, val numberOfPage: Int)


/**
 * This function reads data from the transferred file and checks the correctness of the file format.
 *  @return correct data for algorithms
 */
fun readFile(filename: String): List<String> {
    val lines: List<String> = File(filename).readLines()
    if (lines.size > 3) {
        throw IncorrectFormat("Too many lines in file $filename")
    }

    if (lines[0] == "") {
        throw IncorrectFormat("Process address space size not specified in file $filename")
    }
    if (lines[1] == "") {
        throw IncorrectFormat("RAM size not specified in file $filename")
    }
    if (lines[2] == "") {
        throw IncorrectFormat("Sequence of requests not specified in file $filename")
    }
    return lines
}

/**
 * This function processes data from the correct file and translates it into a convenient format.
 */
fun dataProcessing(lines: List<String>): Pair<Int, List<Int>> {
    val processSize: Int = lines[0].toInt()  //N
    val memorySize: Int = lines[1].toInt()   //M
    val calls: List<String> = lines[2].split(" ")

    /**
     * This loop checks to see if there are any references to nonexistent pages.
     * If there are invalid calls, they are ignored and a warning is displayed.
     */
    val sequenceOfCalls: MutableList<Int> = mutableListOf()
    var ifTooBigCall = false
    for (call in calls) {
        if (call.toInt() <= processSize) {
            sequenceOfCalls.add (call.toInt())
        }
        else {
            if (!ifTooBigCall) {
                println("There were invalid calls in the call sequence.\nThese calls will be ignored.\n")
                ifTooBigCall = true
            }
        }
    }
    return Pair(memorySize, sequenceOfCalls)
}

/**
 * This function generates a log file with the exact time in the name in the folder "logs".
 * @return log file
 * The log file will contain explanations of the type of error.
 */
fun createLogFile(): File {
    val logsDir = File("logs/")
    logsDir.mkdir()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.now().format(formatter)
    val logName = "$dateTime.log"
    val logFile = File(logsDir, logName)
    return logFile
}

/**
 * This function implements the operation of the FIFO algorithm.
 * @return sequence of answers of FIFO algorithm.
 */
fun fifoAlgorithm(memorySize: Int, sequenceOfCalls:List<Int>): List<String> {
    val sequenceOfAnswers: MutableList<String> = mutableListOf()
    val occupiedMemory: Queue<FrameOfMemory> = LinkedList()

    for (desiredPage in sequenceOfCalls) {
        if (occupiedMemory.map { it.numberOfPage }.contains(desiredPage)) {
            sequenceOfAnswers.add("+")
        }
        else {
            if (occupiedMemory.size < memorySize) {
                val frame = occupiedMemory.size
                occupiedMemory.add(FrameOfMemory(frame, desiredPage))
                sequenceOfAnswers.add("${frame + 1}*")
            }
            else {
                val frame = occupiedMemory.element().numberOfFrame
                occupiedMemory.remove()
                occupiedMemory.add(FrameOfMemory(frame, desiredPage))
                sequenceOfAnswers.add("${frame + 1}")
            }
        }
    }
    return sequenceOfAnswers
}

/**
 * This function implements increasing the pages age in memory by 1 for the LRU algorithm.
 */
fun increasingAge(oldMemory: MutableMap<Int, FrameOfMemory>): MutableMap<Int, FrameOfMemory> {
    val newMemory: MutableMap<Int, FrameOfMemory> = mutableMapOf()
    for ((age, frameAndPage) in oldMemory) {
        val newAge = age + 1
        newMemory[newAge] = frameAndPage
    }
    oldMemory.clear()
    return newMemory
}

/**
 * This function implements the resetting of the age of the function when called if it is already in memory
 * for the LRU algorithm.
 */
fun resettingAge(memory: MutableMap<Int, FrameOfMemory>, desiredPage: Int): MutableMap<Int, FrameOfMemory> {
    var nullableAge = -1
    var nullableFrame = FrameOfMemory(-1, -1)
    for ((age, frameAndPage) in memory) {
        if (frameAndPage.numberOfPage == desiredPage) {
            nullableAge = age
            nullableFrame = frameAndPage
        }
    }
    memory[0] = nullableFrame
    memory.remove(nullableAge)
    return memory
}

/**
 * This function implements the operation of the LRU algorithm.
 * @return sequence of answers of LRU algorithm.
 * Uses functions [increasingAge], [resettingAge].
 */
fun lruAlgorithm(memorySize: Int, sequenceOfCalls:List<Int>): List<String> {
    val sequenceOfAnswers: MutableList<String> = mutableListOf()
    var occupiedMemory: MutableMap<Int, FrameOfMemory> = mutableMapOf()

    for (desiredPage in sequenceOfCalls) {
        occupiedMemory = increasingAge(occupiedMemory)

        if (occupiedMemory.map { it.value.numberOfPage }.contains(desiredPage)) {
            occupiedMemory = resettingAge(occupiedMemory, desiredPage)
            sequenceOfAnswers.add("+")
        }
        else {
            if (occupiedMemory.size < memorySize) {
                val frame = occupiedMemory.size
                occupiedMemory[0] = FrameOfMemory(frame, desiredPage)
                sequenceOfAnswers.add("${frame + 1}*")
            }
            else {
                val maxAge: Int? = occupiedMemory.keys.toList().maxByOrNull { it }
                val frame = occupiedMemory[maxAge]!!.numberOfFrame
                occupiedMemory.remove(maxAge)
                occupiedMemory[0] = FrameOfMemory(frame, desiredPage)
                sequenceOfAnswers.add("${frame + 1}")
            }
        }

    }
    return sequenceOfAnswers
}

/**
 * This function implements a decrease in the time to call pages in memory by 1 for the OPT algorithm.
 */
fun reductionOfTimeToCall(oldMemory: MutableMap<Int, FrameOfMemory>): MutableMap<Int, FrameOfMemory> {
    val newMemory: MutableMap<Int, FrameOfMemory> = mutableMapOf()
    for ((timeToCall, frameAndPage) in oldMemory) {
        val newTimeToCall = timeToCall - 1
        newMemory[newTimeToCall] = frameAndPage
    }
    oldMemory.clear()
    return newMemory
}

/**
 * This function sets a new time for the called page already in memory for the OPT algorithm.
 * Uses the function [newTimeToCall].
 */
fun installOfTimeToCall(memory: MutableMap<Int, FrameOfMemory>, desiredPage: Int, calls: List<Int>, currentCall: Int): MutableMap<Int, FrameOfMemory> {
    var mutableTimeToCall = calls.size * 3
    var mutableFrame = FrameOfMemory(calls.size * 3, calls.size * 3)
    for ((timeToCall, frameAndPage) in memory) {
        if (frameAndPage.numberOfPage == desiredPage) {
            mutableTimeToCall = timeToCall
            mutableFrame = frameAndPage
        }
    }
    memory[newTimeToCall(calls, currentCall, desiredPage)] = mutableFrame
    memory.remove(mutableTimeToCall)
    return memory
}

/**
 * This function calculates the time until the page is called for the OPT algorithm.
 */
fun newTimeToCall(sequenceOfCalls: List<Int>, currentCall: Int, desiredPage: Int): Int {
    val nextCall: Int = sequenceOfCalls.drop(currentCall + 1).indexOfFirst{it == desiredPage}
    val timeToCall: Int
    timeToCall = if (nextCall == -1) {
        sequenceOfCalls.size * 3
    } else {
        nextCall + 1
    }
    return timeToCall
}

/**
 * This function implements the operation of the OPT algorithm.
 * @return sequence of answers of OPT algorithm.
 * Uses functions [reductionOfTimeToCall], [installOfTimeToCall], [newTimeToCall].
 */
fun optAlgorithm(memorySize: Int, sequenceOfCalls:List<Int>): List<String> {
    val sequenceOfAnswers: MutableList<String> = mutableListOf()
    var occupiedMemory: MutableMap<Int, FrameOfMemory> = mutableMapOf()

    for ((index, desiredPage) in sequenceOfCalls.withIndex()) {
        occupiedMemory = reductionOfTimeToCall(occupiedMemory)

        if (occupiedMemory.map { it.value.numberOfPage }.contains(desiredPage)) {
            occupiedMemory = installOfTimeToCall(occupiedMemory, desiredPage, sequenceOfCalls, index)
            sequenceOfAnswers.add("+")
        }
        else {
            if (occupiedMemory.size < memorySize) {
                val frame = occupiedMemory.size
                occupiedMemory[newTimeToCall(sequenceOfCalls, index, desiredPage)] = FrameOfMemory(frame, desiredPage)
                sequenceOfAnswers.add("${frame + 1}*")
            }
            else {
                val maxTimeToCall: Int? = occupiedMemory.keys.toList().maxByOrNull { it }
                val frame = occupiedMemory[maxTimeToCall]!!.numberOfFrame
                occupiedMemory.remove(maxTimeToCall)
                occupiedMemory[newTimeToCall(sequenceOfCalls, index, desiredPage)] = FrameOfMemory(frame, desiredPage)
                sequenceOfAnswers.add("${frame + 1}")
            }
        }

    }
    return sequenceOfAnswers
}

/**
 * This function counts the number of responses of the second type for the algorithm.
 */
fun numberOfAnswersOfTheSecondType(resultOfAlgorithm: List<String>, nameOfAlgorithm: String): Pair<Int, String> {
    val number = resultOfAlgorithm.filter { it != "+" }.size
    return Pair(number, nameOfAlgorithm)
}

/**
 * This function sorts the algorithms by the number of responses of the second type.
 * Uses the function [numberOfAnswersOfTheSecondType].
 */
fun sortAlgorithms(resultFIFO: List<String>, resultLRU: List<String>, resultOPT: List<String>): MutableList<Pair<Int, String>> {
    val fifo = numberOfAnswersOfTheSecondType(resultFIFO, "FIFO")
    val lru  = numberOfAnswersOfTheSecondType(resultLRU, "LRU")
    val opt  = numberOfAnswersOfTheSecondType(resultOPT, "OPT")
    val sortedAlgorithms: MutableList<Pair<Int, String>> = mutableListOf(fifo, lru, opt)
    sortedAlgorithms.sortBy { it.first }
    return sortedAlgorithms
}

/**
 * This function displays the results of the algorithms and compares their results.
 */
fun outputOnDisplay(fifo: List<String>, lru: List<String>, opt: List<String>) {
    println("This is the result of the algorithm FIFO: $fifo")
    println("This is the result of the algorithm LRU: $lru")
    println("This is the result of the algorithm OPT: $opt")
    println("")

    println("Algorithms sorted by the number of answers of the second type:")
    val sortedAlgorithms = sortAlgorithms(fifo, lru, opt)
    sortedAlgorithms.forEach {
        println("${it.second}: ${it.first}")
    }
    return
}

/**
 * This function implements the work of the program.
 * @param files
 */
fun main(args: Array<String>) {
    val logFile = createLogFile()
    for (filename in args) {
        try {
            val lines = readFile(filename)
            val (memorySize, sequenceOfCalls) = dataProcessing(lines)

            val fifo = fifoAlgorithm(memorySize, sequenceOfCalls)
            val lru = lruAlgorithm(memorySize, sequenceOfCalls)
            val opt = optAlgorithm(memorySize, sequenceOfCalls)

            outputOnDisplay(fifo, lru, opt)

        } catch (e: Exception) {
            /**
             * This block prints an error message to the screen and writes a description of the error to log.
             */
            logFile.appendText("$filename:$e\n")
            println("Error occurred while executing the file $filename: see error description in log.")
        }
        println("\n")
    }
}