#!/bin/bash
mkdir -p bin
find src -name '*.java' > sources.txt
javac -d bin -cp src @sources.txt
java -cp bin chess.online.ChessServer