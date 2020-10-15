###Virtual memory
This is program for the implementation of algorithms for managing virtual memory and comparing them with each other.  

The program accepts one or several files.   
**Description of correct format of input data:**
* the file must contain the top three lines and only them
* the top line shows the number of pages in the process's address space (1 natural number)
* the second line should indicate the number of memory frames (1 natural number)
* the third line should contain a sequence of natural numbers (page references). They must be separated by spaces. 

The results of the program are displayed on the screen.  
**Description of output:**
1. The file was not in the correct format:
    * the program will fail
    * a message with error description will be displayed on the screen
    * the error description is written to the log
2. The file is in the correct format:
    * the result of each algorithm is displayed
        >This is the result of the algorithm ... : [...]
        * if a free frame is replaced, then the answer is the frame number with * 
        * if an already filled frame is replaced, then the answer is the frame number
        * if the page is already in memory, then the answer is +  
        
        P.s. this output is arranged for the convenience of the user.                                          
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

File file.txt processing result:
There were invalid calls in the call sequence. These calls will be ignored.

This is the result of the algorithm FIFO: [1*, 2*, 3*, 1, +, 2, +, 3, 1]
This is the result of the algorithm LRU: [1*, 2*, 3*, 1, +, 2, +, 1, 2]
This is the result of the algorithm OPT: [1*, 2*, 3*, 2, +, 2, +, +, 1]

Algorithms sorted by the number of answers of the second type:
OPT: 6
FIFO: 7
LRU: 7


````
##### file.txt
````
10
3
22 4 8 7 1 1 2 7 4 10
````

###Answers for integration tests
example1.txt (incorrect format: RAM size not specified):
````shell script
File data/example1.txt processing result:
me.maria.IncorrectFormat: RAM size is not in the correct format in line 2 in file data/example1.txt. Natural number is expected.
````

example2.txt (incorrect format):
````shell script
File data/example2.txt processing result:
me.maria.IncorrectFormat: Process address space size is not in the correct format in line 1 in file data/example2.txt. Natural number is expected.
````

example3.txt (incorrect format: too many lines):
````shell script
File data/example3.txt processing result:
me.maria.IncorrectFormat: Expected exactly 3 lines in file data/example3.txt
````

example4.txt (small example):
````shell script
File data/example4.txt processing result:
This is the result of the algorithm FIFO: [1*, +, 1, 1, 1, 1]
This is the result of the algorithm LRU: [1*, +, 1, 1, 1, 1]
This is the result of the algorithm OPT: [1*, +, 1, 1, 1, 1]

Algorithms sorted by the number of answers of the second type:
FIFO: 5
LRU: 5
OPT: 5
````

example5.txt (big example):
````shell script
File data/example5.txt processing result:
This is the result of the algorithm FIFO: [1*, +, 2*, 3*, 1, 2, +, 3, 1, 2]
This is the result of the algorithm LRU: [1*, +, 2*, 3*, 1, 2, +, 1, +, 2]
This is the result of the algorithm OPT: [1*, +, 2*, 3*, 1, 1, +, +, +, 3]

Algorithms sorted by the number of answers of the second type:
OPT: 6
LRU: 7
FIFO: 8
````

example6.txt (with invalid calls):
````shell script
File data/example6.txt processing result:
There were invalid calls in the call sequence. These calls will be ignored.

This is the result of the algorithm FIFO: [1*, 2*, 3*, 1, 2, 3, 1, 2, 3, 1, 2, +]
This is the result of the algorithm LRU: [1*, 2*, 3*, 1, 2, 3, 1, 2, 3, 1, 2, +]
This is the result of the algorithm OPT: [1*, 2*, 3*, 2, 2, +, +, 1, 1, 2, +, +]

Algorithms sorted by the number of answers of the second type:
OPT: 8
FIFO: 11
LRU: 11
````

example7.txt (invalid calls):
````shell script
File data/example7.txt processing result:
me.maria.IncorrectFormat: Sequence of requests has not valid calls in line 3 in file data/example7.txt. Sequence of natural number is expected.
````