###Virtual memory
This is program for the implementation of algorithms for managing virtual memory and comparing them with each other.  

The program accepts one or several files.   
**Description of correct format of input data:**
* the file must contain the top three lines and only them
* the top line shows the number of pages in the process's address space (1 number without spaces)
* the second line should indicate the number of memory frames (1 number without spaces)
* the third line should contain a sequence of numbers (page references). 
They must be separated by spaces. 
The line starts and ends with a number.

The results of the program are displayed on the screen.  
**Description of output:**
1. The file was not in the correct format:
    * the program will fail
    * a message will be displayed on the screen
        >Error occurred while executing the file ... : see error description in log.
    * the error description is written to the log
2. The file is in the correct format:
    * the result of each algorithm is displayed
        >This is the result of the algorithm ... : ...
    * the result of the comparison is displayed
        >Algorithms sorted by the number of answers of the second type: ...
3. Among the calls there are inadmissible:
    * a warning is displayed before the output of the program
        >There were invalid calls in the call sequence.
         These calls will be ignored.
    * invalid calls are ignored


### Example
````shell script
$./gradlew run --args="file.txt"

There were invalid calls in the call sequence.
These calls will be ignored.

This is the result of the algorithm FIFO: [1*, 2*, 3*, 1, +, 2, +, 3, 1]
This is the result of the algorithm LRU: [1*, 2*, 3*, 1, +, 2, +, 1, 2]
This is the result of the algorithm OPT: [1*, 2*, 3*, 2, +, 2, +, +, 1]

Algorithms sorted by the number of answers of the second type:
OPT: 6
FIFO: 7
LRU: 7


````
#### file.txt
````
10
3
22 4 8 7 1 1 2 7 4 10
````