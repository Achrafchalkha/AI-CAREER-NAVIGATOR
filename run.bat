@echo off
echo Setting environment variables...

set "MONGODB_URI=mongodb+srv://achrafchalkha:PkNMWX1hFMBQ5iAT@cluster0.avnezj3.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
set "MONGODB_DATABASE=aicareernavigator"
set "JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
set "JWT_EXPIRATION=86400000"
set "OPENROUTER_API_KEY=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"

echo Environment variables set successfully!
echo Starting AI Career Navigator...

call mvnw.cmd spring-boot:run
pause
