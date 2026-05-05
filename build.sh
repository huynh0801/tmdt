#!/bin/bash
set -e

echo "========================================="
echo "Building application for Railway..."
echo "========================================="

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "ERROR: gradlew not found!"
    echo "Using gradle command instead..."
    GRADLE_CMD="gradle"
else
    chmod +x gradlew
    GRADLE_CMD="./gradlew"
fi

# Clean build
echo "Cleaning previous build..."
$GRADLE_CMD clean --no-daemon || {
    echo "Clean failed, continuing anyway..."
}

# Build without tests
echo "Building JAR..."
$GRADLE_CMD build -x test --no-daemon

# Verify build output
if [ ! -f "build/libs/webapp-1.0-SNAPSHOT.jar" ]; then
    echo "ERROR: JAR file not created!"
    echo "Checking build directory..."
    ls -la build/libs/ || echo "build/libs/ not found"
    exit 1
fi

echo "========================================="
echo "Build completed successfully!"
echo "JAR file:"
ls -lh build/libs/*.jar
echo "========================================="
