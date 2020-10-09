package me.maria

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * This class indicates that the file is not in the correct format.
 */
class IncorrectFormat(message: String): Exception(message)

data class FrameOfMemory(val numberOfFrame: Int, val numberOfPage: Int)


fun readFile(filename: String): Triple<Int, Int, List<Int>> {
    val lines: List<String> = File(filename).readLines()
    if (lines.size > 3) {
        throw IncorrectFormat("Too many lines in file $filename")
    }

    val processSize: Int = lines[0].toInt()  //N
        ?: throw IncorrectFormat("Process address space size not specified in file $filename")
    val memorySize: Int = lines[1].toInt()   //M
        ?: throw IncorrectFormat("RAM size not specified in file $filename")
    val calls: List<String> = lines[2].split(" ")
        ?: throw IncorrectFormat("Sequence of requests not specified in file $filename")

    val sequenceOfCalls: MutableList<Int> = mutableListOf()
    for (call in calls) {
        if (call.toInt() <= processSize) {
            sequenceOfCalls.add (call.toInt())   //подумать, что стоит выводить, что были недопустимые обращения
        }
    }
    return Triple(processSize, memorySize, sequenceOfCalls)
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

fun increasingAge(oldMemory: MutableMap<Int, FrameOfMemory>): MutableMap<Int, FrameOfMemory> {
    val newMemory: MutableMap<Int, FrameOfMemory> = mutableMapOf()
    for ((age, frameAndPage) in oldMemory) {
        val newAge = age + 1
        newMemory[newAge] = frameAndPage
    }
    oldMemory.clear()
    return newMemory
}


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

fun reductionOfTimeToCall(oldMemory: MutableMap<Int, FrameOfMemory>): MutableMap<Int, FrameOfMemory> {
    val newMemory: MutableMap<Int, FrameOfMemory> = mutableMapOf()
    for ((timeToCall, frameAndPage) in oldMemory) {
        val newTimeToCall = timeToCall - 1
        newMemory[newTimeToCall] = frameAndPage
    }
    oldMemory.clear()
    return newMemory
}

fun installOfTimeToCall(memory: MutableMap<Int, FrameOfMemory>, desiredPage: Int, sequenceOfCalls: List<Int>, currentCall: Int): MutableMap<Int, FrameOfMemory> {
    var mutableTimeToCall = sequenceOfCalls.size + 10
    var mutableFrame = FrameOfMemory(sequenceOfCalls.size + 10, sequenceOfCalls.size + 10)
    for ((timeToCall, frameAndPage) in memory) {
        if (frameAndPage.numberOfPage == desiredPage) {
            mutableTimeToCall = timeToCall
            mutableFrame = frameAndPage
        }
    }
    memory[newTimeToCall(sequenceOfCalls, currentCall, desiredPage)] = mutableFrame
    memory.remove(mutableTimeToCall)
    return memory
}

fun newTimeToCall(sequenceOfCalls: List<Int>, currentCall: Int, desiredPage: Int): Int {
    val nextCall: Int = sequenceOfCalls.drop(currentCall + 1).indexOfFirst{it == desiredPage}
    val timeToCall: Int
    timeToCall = if (nextCall == -1) {
        sequenceOfCalls.size + 10
    } else {
        nextCall + 1
    }
    return timeToCall
}

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

fun main(args: Array<String>) {
    val logFile = createLogFile()
    for (filename in args) {
        try {
            val (_, memorySize, sequenceOfCalls) = readFile(filename)
            println("This is the result of the algorithm FIFO: ${fifoAlgorithm(memorySize, sequenceOfCalls)}")
            println("This is the result of the algorithm LRU: ${lruAlgorithm(memorySize, sequenceOfCalls)}")
            println("This is the result of the algorithm OPT: ${optAlgorithm(memorySize, sequenceOfCalls)}")

        } catch (e: Exception) {
            logFile.appendText("$filename:$e\n")
            println("An error occurred while executing the file $filename: see error description in log")
        }
        println("")
    }
}