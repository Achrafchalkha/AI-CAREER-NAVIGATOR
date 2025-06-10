# OpenRouter.ai Integration Guide

## Overview
This project has been updated to use OpenRouter.ai instead of direct OpenAI API calls. OpenRouter provides access to hundreds of AI models through a single unified API, including OpenAI models and many others.

## Configuration

### API Key Setup
Your OpenRouter API key is configured in `application.properties`:
```properties
openrouter.api.key=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
```

### Available Configuration Options
```properties
# OpenRouter AI Service Configuration
openrouter.api.key=your-api-key-here
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.api.models-url=https://openrouter.ai/api/v1/models
openrouter.default.model=openai/gpt-3.5-turbo
openrouter.default.max-tokens=2000
openrouter.default.temperature=0.7
openrouter.site.url=http://localhost:8035
openrouter.site.name=AI Career Navigator
```

## Available Endpoints

### New OpenRouter Endpoints

#### 1. Test Connection
```
GET /api/openrouter/test
```
Tests the connection to OpenRouter API.

#### 2. Generate Text
```
POST /api/openrouter/generate
Content-Type: application/json

{
  "input": "Your prompt here",
  "model": "openai/gpt-4" // Optional, uses default if not specified
}
```

#### 3. Get Available Models
```
GET /api/openrouter/models
```
Returns all available models with detailed information.

#### 4. Get Simple Model List
```
GET /api/openrouter/models/simple
```
Returns a simplified list of models (just id and name).

#### 5. Chat with Specific Model
```
POST /api/openrouter/chat/{model}
Content-Type: application/json

{
  "message": "Your message here"
}
```

### Legacy Endpoints (Updated to use OpenRouter)

#### 1. Legacy OpenAI Test
```
GET /api/openai/test
```

#### 2. Legacy OpenAI Generate
```
POST /api/openai/generate
Content-Type: application/json

{
  "input": "Your prompt here"
}
```

#### 3. Legacy Unicorn Story
```
GET /ask-unicorn
```

#### 4. Career Chat
```
POST /api/career/chat
Content-Type: application/json

"Your career question here"
```

## Available Models

OpenRouter provides access to many models including:
- OpenAI models: `openai/gpt-4`, `openai/gpt-3.5-turbo`, `openai/gpt-4-turbo`
- Anthropic models: `anthropic/claude-3-sonnet`, `anthropic/claude-3-haiku`
- Google models: `google/gemini-pro`
- Meta models: `meta-llama/llama-2-70b-chat`
- And many more!

Use the `/api/openrouter/models` endpoint to get the current list of available models.

## Code Structure

### Main Classes
- `OpenRouterConfig`: Configuration for OpenRouter WebClient
- `OpenRouterService`: Main service for interacting with OpenRouter API
- `OpenRouterController`: New REST controller for OpenRouter endpoints
- `AIRecommendationService`: Updated to use OpenRouter instead of OpenAI

### DTOs
- `OpenRouterRequest`: Request format for OpenRouter API
- `OpenRouterResponse`: Response format from OpenRouter API
- `OpenRouterModel`: Model information from OpenRouter

## Migration Notes

1. **API Compatibility**: OpenRouter uses the same API format as OpenAI, so existing code is mostly compatible.
2. **Model Names**: Models are prefixed with provider names (e.g., `openai/gpt-4` instead of just `gpt-4`).
3. **Headers**: OpenRouter supports additional headers for site tracking and analytics.
4. **Fallbacks**: OpenRouter automatically handles model fallbacks and provider routing.

## Benefits of OpenRouter

1. **Multiple Providers**: Access to models from OpenAI, Anthropic, Google, Meta, and more
2. **Automatic Fallbacks**: If one provider is down, OpenRouter automatically tries alternatives
3. **Cost Optimization**: Automatically routes to the most cost-effective provider
4. **Unified API**: Single API for all models, no need to manage multiple API keys
5. **Rate Limiting**: Built-in rate limiting and queue management

## Testing

To test the integration:

1. Start the application
2. Test connection: `GET http://localhost:8035/api/openrouter/test`
3. Generate text: `POST http://localhost:8035/api/openrouter/generate` with JSON body
4. List models: `GET http://localhost:8035/api/openrouter/models/simple`

## Troubleshooting

1. **API Key Issues**: Ensure your OpenRouter API key is valid and has sufficient credits
2. **Model Not Found**: Check available models using the models endpoint
3. **Rate Limiting**: OpenRouter has built-in rate limiting, wait and retry if needed
4. **Network Issues**: Check your internet connection and firewall settings

## Security Notes

- API keys are stored in application.properties (consider using environment variables in production)
- OpenRouter provides privacy controls and logging options
- All requests are logged by default (can be disabled in OpenRouter settings)
