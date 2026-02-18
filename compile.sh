#!/bin/bash
echo "Compiling Chess Pro..."
mkdir -p bin
find src -name "*.java" > /tmp/sources.txt
javac -d bin -cp src @/tmp/sources.txt
if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
else
    echo "✗ Compilation failed."
    exit 1
fi
