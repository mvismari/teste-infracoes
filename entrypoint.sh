#!/bin/sh
echo "Starting application with JAVA_OPTS: $JAVA_OPTS"
echo "Current time: $(date)"
echo "Working directory: $(pwd)"

# Unbuffer output and redirect stderr to stdout
exec java $JAVA_OPTS -jar app.jar 2>&1