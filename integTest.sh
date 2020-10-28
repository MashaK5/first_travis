#!/bin/bash

./gradlew jar

for (( test = 0; test < 10; test++ ))
do
java -jar build/libs/project_2.jar "data/example${test}.txt" > output
diff -q "data/results/example${test}.txt" output
if [ $? -eq 1 ]
then
echo "test $test failed"
exit 1
fi
done
