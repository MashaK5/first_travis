#!/bin/bash

gradle jar

for (( test = 0; test < 10; test++ ))
do
java -jar build/libs/project_2.jar "data/example${test}.txt" output.txt
diff -q "data/results/example${test}.txt" output.txt
if [ $? -eq 1 ]
then
echo "test $test failed"
exit $test
fi
done
