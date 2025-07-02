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
