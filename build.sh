#!/bin/bash
classfiles_path="build"

mkdir ${classfiles_path} 2>/dev/null
mkdir tmp 2>/dev/null

for src_java in $(find ./src -name "*.java")
do
  javac -sourcepath ./src -d ./${classfiles_path} ${src_java}
done
