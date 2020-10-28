#!/bin/bash

for file in $(find data -regex "example[0-9].txt")
do
  java -jar build/libs/project_2.jar data/"$file"
  cmp -s data/results/"$file" output.txt || exit 1
done
