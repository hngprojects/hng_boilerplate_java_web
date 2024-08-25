#!/bin/bash

# Define the output directory and file name
# OUTPUT_DIR="newman-reports"
# OUTPUT_FILE="$OUTPUT_DIR/report-$(date +%Y%m%d%H%M%S).html"

# Create the directory if it doesn't exist
# mkdir -p $OUTPUT_DIR

# Run Newman with HTML reporter
newman run hng_boilerplate_java_web/qa_tests/regression/Spartacus_Java_Boilerplate_Copy.postman_collection.json -r htmlextra
