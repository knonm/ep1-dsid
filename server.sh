#!/bin/bash
v_cmd=${1}
v_cntservers=${2}
v_host_name="127.0.0.1"
v_port="2001"
v_classpath="$(pwd)/build/"
v_tmp="./tmp"

case "${v_cmd}" in
  start)
    mkdir -p ${v_tmp} 2>/dev/null
    if [[ ! "${v_cntservers}" -ge "1" ]]
    then
      v_cntservers=1
    fi
    if [[ -z "$(ls -A ${v_tmp} | grep ^0$)" ]]
    then
      echo "Starting rmiregistry ..."
      rmiregistry -J-Djava.rmi.server.hostname="${v_host_name}" -J-Djava.rmi.server.codebase="file://${v_classpath}" ${v_port} &
      echo $! 1>${v_tmp}/0
      echo "rmiregistry is running!"
      echo ""
    fi
    echo "Starting server(s) ..."
    v_stubmaxid=$(echo $(ls ${v_tmp} | sort -r -n) | cut -d " " -f 1)
    for ((v_stubid=v_stubmaxid+1; v_stubid<=v_stubmaxid+${v_cntservers}; v_stubid++))
    do
      java -classpath "${v_classpath}" br.usp.ep1.dsid.Server ${v_stubid} &
      sleep .5
      echo $! 1>${v_tmp}/${v_stubid}
    done
    echo "Server(s) is/are running!"
    ;;
  stop)
    if [[ -d "${v_tmp}" ]]
    then
      if [[ ! -z "$(ls ${v_tmp} | grep ^${v_cntservers}$)" ]]
      then
        echo "Stopping process number ${v_cntservers} ..."
        kill -9 $(echo $(cat ${v_tmp}/${v_cntservers})) 2>/dev/null
        echo "Process ${v_cntservers} stopped!"
        rm ${v_tmp}/${v_cntservers}
      else
        echo "Stopping rmiregistry and server(s) ..."
        for k in $(ls ${v_tmp})
        do
          kill -9 $(echo $(cat ${v_tmp}/${k})) 2>/dev/null
        done
        echo "rmiregistry and server(s) are stopped!"
        rm -rf ${v_tmp} 2>/dev/null
      fi
    fi
    ;;
  list)
    if [[ -d "${v_tmp}" && ! -z "$(ls -A ${v_tmp})" ]]
    then
      echo "rmiregistry: $(echo $(cat ${v_tmp}/0))"
      for i in $(ls ${v_tmp} | egrep -v ^0$)
      do
        echo "server ${i}: $(echo $(cat ${v_tmp}/${i}))"
      done
    fi
    ;;
  *)
    echo "Usage: server [start <number_of_servers >= 1> | stop | list]"
    echo "Example: ./server.sh start 10"
    exit 1
    ;;
esac
