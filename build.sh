#!/bin/bash
v_class_path="./build"
v_source_path="./src"

mkdir -p ${v_class_path} 2>/dev/null

./server.sh stop

for src_java in $(find ${v_source_path} -name "*.java")
do
  javac -sourcepath ${v_source_path} -d ${v_class_path} ${src_java}
done
