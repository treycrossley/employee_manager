#!/bin/bash


LOGIN_URL="http://localhost:8080/api/users/login"
JSON_PAYLOAD='{"username": "testuser", "password": "testpassword"}'



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
