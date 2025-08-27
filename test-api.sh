#!/bin/bash

# Weather API Test Script
# This script tests the weather API endpoints

BASE_URL="http://localhost:8080/api/weather"

echo "🌤️  Weather API Test Script"
echo "================================"

# Test 1: Get weather by coordinates (Berlin)
echo
echo "📍 Test 1: Get weather by coordinates (Berlin)"
echo "URL: $BASE_URL/current?latitude=52.5200&longitude=13.4050"
curl -s "$BASE_URL/current?latitude=52.5200&longitude=13.4050" | jq '.' || echo "❌ Test failed"

# Test 2: Get weather by city name
echo
echo "🏙️  Test 2: Get weather by city name (Berlin)"
echo "URL: $BASE_URL/current?city=Berlin"
curl -s "$BASE_URL/current?city=Berlin" | jq '.' || echo "❌ Test failed"

# Test 3: Invalid request - missing parameters
echo
echo "❌ Test 3: Invalid request - missing parameters"
echo "URL: $BASE_URL/current"
curl -s "$BASE_URL/current" | jq '.' || echo "Expected error response"

# Test 4: Invalid request - both parameters provided
echo
echo "❌ Test 4: Invalid request - both parameters provided"
echo "URL: $BASE_URL/current?latitude=52.5200&longitude=13.4050&city=Berlin"
curl -s "$BASE_URL/current?latitude=52.5200&longitude=13.4050&city=Berlin" | jq '.' || echo "Expected error response"

# Test 5: Invalid coordinates
echo
echo "❌ Test 5: Invalid coordinates"
echo "URL: $BASE_URL/current?latitude=100&longitude=200"
curl -s "$BASE_URL/current?latitude=100&longitude=200" | jq '.' || echo "Expected error response"

# Test 6: Non-existent city
echo
echo "❌ Test 6: Non-existent city"
echo "URL: $BASE_URL/current?city=NonExistentCityName123"
curl -s "$BASE_URL/current?city=NonExistentCityName123" | jq '.' || echo "Expected error response"

echo
echo "✅ Test script completed!"
echo "💡 Note: Make sure the application is running on port 8080"
