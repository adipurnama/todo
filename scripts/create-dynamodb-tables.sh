#!/bin/bash

# Create Task and User tables
aws dynamodb create-table --endpoint-url http://localhost:8000 --table-name Task --cli-input-json file://task-table.json
aws dynamodb create-table --endpoint-url http://localhost:8000 --table-name User --cli-input-json file://user-table.json
aws dynamodb create-table --endpoint-url http://localhost:8000 --table-name TaskLookup --cli-input-json file://task-lookup-table.json
# List table
aws dynamodb list-tables --endpoint-url http://localhost:8000