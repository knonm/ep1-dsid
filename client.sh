#!/bin/bash
v_host_name=${1}
v_port="2001"

if [[ -z "${v_host_name}" ]]
then
  v_host_name=localhost
fi

clear
java -classpath ./build br.usp.ep1.dsid.Client ${v_host_name} ${v_port}
clear
