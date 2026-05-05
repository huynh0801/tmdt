#!/bin/bash
set -e

echo "========================================="
echo "Building application for Railway..."
echo "========================================="

# Make gradlew executable
chmod +x gradlew

# Clean build
echo "Cleaning previous build..."
./gradlew clean

# Build without tests
echo "Building JAR..."
./gradlew build -x test --no-daemon

# Ensure webapp directory exists in production
echo "Ensuring webapp directory exists..."
if [ ! -d "src/main/webapp" ]; then
    echo "ERROR: src/main/webapp not found!"
    exit 1
fi

echo "Webapp directory contents:"
ls -la src/main/webapp/ | head -10

echo "========================================="
echo "Build completed successfully!"
echo "JAR file:"
ls -lh build/libs/*.jar
echo "========================================="
