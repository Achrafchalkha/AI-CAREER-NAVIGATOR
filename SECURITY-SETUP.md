# 🔐 Security Setup Guide

## ⚠️ IMPORTANT: API Key Security

This project has been configured to prevent accidental exposure of API keys in the public repository.

## 🚀 Local Development Setup

### Option 1: Use run-local.bat (Recommended)
1. Copy `run-local.bat.example` to `run-local.bat`
2. Edit `run-local.bat` with your actual API keys
3. Run `run-local.bat` to start the application
4. **Note**: `run-local.bat` is automatically ignored by git

### Option 2: Set System Environment Variables
Set these environment variables in your system:
```bash
MONGODB_URI=your_mongodb_connection_string
MONGODB_DATABASE=aicareernavigator
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000
OPENROUTER_API_KEY=your_openrouter_api_key
```

## 🌐 Production Deployment

### ⚠️ IMPORTANT: Never put actual API keys in documentation!

### For Render.com Deployment:
Add these environment variables in the Render dashboard:
- `MONGODB_URI`
- `MONGODB_DATABASE`
- `JWT_SECRET`
- `OPENROUTER_API_KEY`
- `CORS_ALLOWED_ORIGINS`

## 🛡️ Security Best Practices Applied:
- ✅ No hardcoded secrets in repository
- ✅ Local development files ignored by git
- ✅ Environment variables used for all sensitive data
- ✅ Secure deployment configuration

## 📝 Files Modified for Security:
- `run.bat` - Removed all hardcoded values
- `run-local.bat` - Created for local development (git ignored)
- `.gitignore` - Added patterns to ignore sensitive files
- `render.yaml` - Uses `sync: false` for secure variables
