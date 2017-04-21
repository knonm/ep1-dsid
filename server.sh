#!/bin/bash
v_cmd=${1}
v_cntservers=${2}
v_host_name="127.0.0.1"
v_port="2001"
v_classpath="$(pwd)/build/"

case "${v_cmd}" in
  start)
    if [[ "${v_cntservers}" -ge "1" ]]
    then
      if [[ -z "$(ls -A ./tmp/ | grep ^0$)" ]]
      then
        echo "Starting rmiregistry ..."
        rmiregistry -J-Djava.rmi.server.hostname="${v_host_name}" -J-Djava.rmi.server.codebase="file://${v_classpath}" ${v_port} 2>/dev/null &
        echo $! 1>./tmp/0
        echo "rmiregistry is running!"
        echo ""
      fi
      echo "Starting server(s) ..."
      v_stubid=$(ls ./tmp/ | awk 'BEGIN{x=-2147483648};$0>x{x=$0};END{print x}')
      for ((v_stubid=v_stubid+1;v_stubid<=${v_cntservers};v_stubid++))
      do
        java -classpath "${v_classpath}" br.usp.ep1.dsid.Server ${v_stubid} &
        echo $! 1>./tmp/${v_stubid}
      done
      echo "Server(s) is/are running!"
    else
      echo "Usage: server [start <number_of_servers >= 1> | stop | list]"
      echo "Example: ./server.sh start 10"
    fi
    ;;
  stop)
    echo "Stopping rmiregistry and server(s) ..."
    for k in $(ls ./tmp/)
    do
      kill -9 $(echo $(cat ./tmp/${k}))
    done
    echo "rmiregistry and server(s) are stopped!"
    rm ./tmp/* 2>/dev/null
    ;;
  list)
    echo "rmiregistry: $(echo $(cat ./tmp/0))"
    for i in $(ls ./tmp/ | egrep -v ^0$)
    do
      echo "server ${i}: $(echo $(cat ./tmp/${i}))"
    done
    ;;
  *)
    echo "Usage: server [start <number_of_servers >= 1> | stop | list]"
    echo "Example: ./server.sh start 10"
    exit 1
    ;;
esac
