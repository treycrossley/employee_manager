# Define the API endpoints and JSON payload
$REGISTER_URL = "http://localhost:8080/api/users/register"
$LOGIN_URL = "http://localhost:8080/api/users/login"
$JSON_PAYLOAD = '{"username": "testadmin4", "password": "1234"}'

# Register the user
Write-Host "Registering user..."
$REGISTER_RESPONSE = Invoke-RestMethod -Uri $REGISTER_URL -Method Post -ContentType "application/json" -Body $JSON_PAYLOAD

# Check if the registration was successful
if ($REGISTER_RESPONSE -like "*User already exists*") {
    Write-Host "User already exists. Proceeding to login..."
} else {
    Write-Host "Registration response: $REGISTER_RESPONSE"
}

# Log in the user
Write-Host "Logging in user..."
$LOGIN_RESPONSE = Invoke-RestMethod -Uri $LOGIN_URL -Method Post -ContentType "application/json" -Body $JSON_PAYLOAD

# If the login response is just a plain string token
if ($LOGIN_RESPONSE -and $LOGIN_RESPONSE -ne "") {
    $BEARER_TOKEN = $LOGIN_RESPONSE
    Write-Host "Bearer token: Bearer $BEARER_TOKEN"
} else {
    Write-Host "Failed to retrieve bearer token. Login response: $LOGIN_RESPONSE"
}
