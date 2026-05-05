#!/bin/bash

echo "========================================="
echo "Testing JAR file structure"
echo "========================================="

JAR_FILE="build/libs/webapp-1.0-SNAPSHOT.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR file not found: $JAR_FILE"
    echo "Run: ./gradlew clean build"
    exit 1
fi

echo "✅ JAR file found: $JAR_FILE"
JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
echo "📦 JAR size: $JAR_SIZE"
echo ""

# Check if it's a fat JAR (should be > 50MB)
JAR_SIZE_BYTES=$(stat -f%z "$JAR_FILE" 2>/dev/null || stat -c%s "$JAR_FILE" 2>/dev/null)
if [ "$JAR_SIZE_BYTES" -lt 52428800 ]; then
    echo "⚠️  WARNING: JAR size < 50MB - might not be a fat JAR!"
    echo "   Expected: > 50MB (with all dependencies)"
    echo "   Actual: $JAR_SIZE"
fi

echo ""
echo "Checking webapp resources in JAR..."
echo "-----------------------------------"

# Count webapp files
WEBAPP_COUNT=$(jar tf "$JAR_FILE" | grep "^webapp/" | wc -l)
echo "📁 Webapp files found: $WEBAPP_COUNT"

if [ "$WEBAPP_COUNT" -lt 10 ]; then
    echo "❌ Too few webapp files! Expected > 10"
else
    echo "✅ Webapp files OK"
fi

echo ""
echo "Sample webapp files:"
jar tf "$JAR_FILE" | grep "^webapp/" | head -10

echo ""
echo "Checking critical files:"
echo "-----------------------------------"

# Check specific files
CRITICAL_FILES=(
    "webapp/index.jsp"
    "webapp/WEB-INF/web.xml"
    "vn/edu/hcmuaf/fit/Main.class"
    "org/apache/tomcat/embed/core/StandardServer.class"
    "com/mysql/cj/jdbc/Driver.class"
)

ALL_OK=true
for file in "${CRITICAL_FILES[@]}"; do
    if jar tf "$JAR_FILE" | grep -q "^$file$"; then
        echo "✅ $file"
    else
        echo "❌ $file - MISSING!"
        ALL_OK=false
    fi
done

echo ""
echo "========================================="
if [ "$ALL_OK" = true ]; then
    echo "✅ JAR file looks good!"
    echo "You can test it with:"
    echo "   java -jar $JAR_FILE"
else
    echo "❌ JAR file has issues!"
    echo "Run: ./gradlew clean build"
fi
echo "========================================="
