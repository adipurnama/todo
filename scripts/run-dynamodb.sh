#!/bin/bash

# Set DYNAMODB_HOME
DYNAMODB_HOME=~/Applications/dynamodb

cd $DYNAMODB_HOME
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -inMemory
