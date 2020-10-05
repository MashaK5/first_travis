package me.maria

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This class indicates that the file is not in the correct format.
 */
class IncorrectFormat(message: String): Exception(message)


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
            sequenceOfCalls.add (call.toInt())
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

fun main(args: Array<String>) {
    val logFile = createLogFile()
    for (filename in args) {
        try {
            readFile(filename)
        } catch (e: Exception) {
            /**
             * Writing errors to a log file.
             */
            logFile.appendText("$filename:$e\n")
        }
    }
}