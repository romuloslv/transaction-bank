#!/bin/sh

operation() {
  json=$PWD/resources/request/transactions.json
  echo "Sending request: \n\n$(cat $json)\n"
	response=$(curl -X POST -d @"$json" localhost:3003/transactions --header "Content-Type:application/json";)
}

operation;
