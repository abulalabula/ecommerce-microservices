#!/bin/bash

GRAPHQL_URL="http://localhost:8080/api/items/graphql"
HEADER="Content-Type: application/json"

# Function to send GraphQL requests
send_request() {
    echo "----------------------------------------------------"
    echo "Request: $1"
    curl -X POST "$GRAPHQL_URL" -H "$HEADER" -d "$1"
    echo -e "\n----------------------------------------------------\n"
}

# Get All Items
#GET_ALL_ITEMS='{
#  "query": "query { getAllItems { id name upc price imageUrls stock } }"
#}'
#send_request "$GET_ALL_ITEMS"
##
## Get Item by ID
GET_ITEM_BY_ID='{
  "query": "query { getItem(id: \"67bebc8abb9fc25ddf6af926=0\") { id name upc price imageUrls stock } }"
}'
send_request "$GET_ITEM_BY_ID"
#
# Get Available Stock
#GET_AVAILABLE_STOCK='{
#  "query": "query { getAvailableStock(id: \"67bec05573a4553fe268cc1f\") }"
#}'
#send_request "$GET_AVAILABLE_STOCK"
#
## Create a New Item
#CREATE_ITEM='{
#  "query": "mutation { createItem(name: \"Gaming Mouse\", upc: \"123456789012\", price: 49.99, imageUrls: [\"https://example.com/mouse1.jpg\", \"https://example.com/mouse2.jpg\"], stock: 100) { id name upc price imageUrls stock } }"
#}'
#send_request "$CREATE_ITEM"
#
# Update an Item
#UPDATE_ITEM='{
#  "query": "mutation { updateItem(id: \"67bec05573a4553fe268cc1f\", name: \"Wireless Gaming Mouse\", upc: \"123456789012\", price: 59.99, imageUrls: [\"https://example.com/mouse1.jpg\", \"https://example.com/mouse2.jpg\"], stock: 90) { id name upc price imageUrls stock } }"
#}'
#send_request "$UPDATE_ITEM"
#
# Delete an Item
#DELETE_ITEM='{
#  "query": "mutation { deleteItem(id: \"67bec05573a4553fe268cc1f\") }"
#}'
#send_request "$DELETE_ITEM"
## Update Stock
#UPDATE_STOCK='{
#  "query": "mutation { updateStock(id: \"67bec05573a4553fe268cc1f\", quantity: 199) { id stock } }"
#}'
#send_request "$UPDATE_STOCK"
#
#echo  "All GraphQL requests completed!"
