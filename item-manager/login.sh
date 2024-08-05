#!/bin/bash

# Define the API endpoints and JSON payload
REGISTER_URL="http://localhost:8080/api/users/register"
LOGIN_URL="http://localhost:8080/api/users/login"
JSON_PAYLOAD='{"username": "testadmin4", "password": "1234"}'

# Register the user
echo "Registering user..."
REGISTER_RESPONSE=$(curl -s -X POST -H "Content-Type: application/json" -d "$JSON_PAYLOAD" $REGISTER_URL)

# Check if the registration was successful
if echo "$REGISTER_RESPONSE" | grep -q "User already exists"; then
  echo "User already exists. Proceeding to login..."
else
  echo "Registration response: $REGISTER_RESPONSE"
fi

# Log in the user
echo "Logging in user..."
LOGIN_RESPONSE=$(curl -s -X POST -H "Content-Type: application/json" -d "$JSON_PAYLOAD" $LOGIN_URL)

# Extract the token from the login response
BEARER_TOKEN=$(echo $LOGIN_RESPONSE | awk -F ' ' '{print $2}')

# Output the bearer token
if [ -n "$BEARER_TOKEN" ]; then
  echo "Bearer token: Bearer $BEARER_TOKEN"
else
  echo "Failed to retrieve bearer token. Login response: $LOGIN_RESPONSE"
fi
