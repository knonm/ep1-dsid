#!/bin/bash
v_host_name="127.0.0.1"
v_port="2001"

clear
java -classpath ./build br.usp.ep1.dsid.Client ${v_host_name} ${v_port}
clear
