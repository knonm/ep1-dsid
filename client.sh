#!/bin/bash
v_serverid=${1}
v_host_name="127.0.0.1"
v_port="2001"
v_tmp="./tmp"

if [[ ! "${v_serverid}" -ge "1" ]]
then
  v_serverid=1
fi

if [[ ! -z "$(ls ${v_tmp} | grep ^${v_serverid}$)" ]]
then
  java -classpath ./build br.usp.ep1.dsid.Client ${v_host_name} ${v_port} ${v_serverid}
fi
