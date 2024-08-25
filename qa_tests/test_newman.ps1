# Define the output directory and file name
$outputDir = "newman-reports"
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$outputFile = "$outputDir\report-$timestamp.html"
# Create the directory if it doesn't exist
if (-Not (Test-Path -Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir
}
# Run Newman with HTML reporter
newman run hng_boilerplate_java_web/qa_tests/regression/Spartacus_Java_Boilerplate_Copy.postman_collection.json `
    -r html `
    --reporter-html-export $outputFile