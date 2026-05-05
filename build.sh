#!/bin/bash
set -e

echo "========================================="
echo "Building application..."
echo "========================================="

# Make gradlew executable
chmod +x gradlew

# Run gradle build
./gradlew clean build -x test --no-daemon

echo "========================================="
echo "Build completed successfully!"
echo "========================================="
