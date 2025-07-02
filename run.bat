@echo off
echo SECURITY NOTICE: This file no longer contains hardcoded API keys
echo Please set your environment variables manually or use a .env file (not committed to git)
echo.
echo Required environment variables:
echo - MONGODB_URI
echo - MONGODB_DATABASE
echo - JWT_SECRET
echo - JWT_EXPIRATION
echo - OPENROUTER_API_KEY
echo.
echo Example (set these in your system or create a local .env file):
echo set "MONGODB_URI=your_mongodb_uri_here"
echo set "MONGODB_DATABASE=aicareernavigator"
echo set "JWT_SECRET=your_jwt_secret_here"
echo set "JWT_EXPIRATION=86400000"
echo set "OPENROUTER_API_KEY=your_openrouter_api_key_here"
echo.
echo Starting AI Career Navigator...

call mvnw.cmd spring-boot:run
pause
