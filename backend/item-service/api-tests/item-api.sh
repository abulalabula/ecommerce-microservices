#!/bin/bash

BASE_URL="http://localhost:8080/api/items"

# Create Item
echo "Creating an item..."
ITEM_ID=$(curl -s -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Mac Mini",
      "upc": "89746546123",
      "price": 699.99,
      "imageUrls": [
        "https://example.com/images/mouse1.jpg",
        "https://example.com/images/mouse2.jpg"
      ],
      "stock": 562,
      "createdAt": "2025-02-24T12:00:00Z",
      "updatedAt": "2025-02-25T12:00:00Z"
    }' | jq -r '.id')

echo "Created Item ID: $ITEM_ID"

# Get All Items
echo "Fetching all items..."
curl -X GET "$BASE_URL"

# Get Item by ID
ITEM_ID="67be92a7162a0b609dd27001"
if [ -n "$ITEM_ID" ]; then
    echo "Fetching item with ID: $ITEM_ID"
    curl -X GET "$BASE_URL/$ITEM_ID"
else
    echo "Item ID not found. Skipping GET by ID."
fi

# Update Item by ID
if [ -n "$ITEM_ID" ]; then
    echo "Updating item with ID: $ITEM_ID"
    curl -X PUT "$BASE_URL/$ITEM_ID" \
        -H "Content-Type: application/json" \
        -d '{
          "name": "Updated Mac mini",
          "upc": "89746546123",
          "price": 649.99,
          "imageUrls": [
            "https://example.com/images/mouse1.jpg",
            "https://example.com/images/mouse2.jpg"
          ],
          "stock": 560,
          "createdAt": "2025-02-22T12:00:00Z",
          "updatedAt": "2025-02-22T14:00:00Z"
        }'
else
    echo "Item ID not found. Skipping update."
fi

# Delete Item by ID
if [ -n "$ITEM_ID" ]; then
    echo "Deleting item with ID: $ITEM_ID"
    curl -X DELETE "$BASE_URL/$ITEM_ID"
else
    echo "Item ID not found. Skipping delete."
fi
