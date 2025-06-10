#!/usr/bin/env python3
"""
Simple test script to verify OpenRouter API integration
"""
import requests
import json

# Your OpenRouter API key
API_KEY = "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"
API_URL = "https://openrouter.ai/api/v1/chat/completions"

def test_openrouter_api():
    """Test the OpenRouter API directly"""
    
    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json",
        "HTTP-Referer": "http://localhost:8036",
        "X-Title": "AI Career Navigator"
    }
    
    payload = {
        "model": "openai/gpt-3.5-turbo",
        "messages": [
            {
                "role": "user",
                "content": "Write a short story about AI in one sentence."
            }
        ],
        "max_tokens": 100,
        "temperature": 0.7
    }
    
    try:
        print("Testing OpenRouter API...")
        print(f"URL: {API_URL}")
        print(f"Model: {payload['model']}")
        print(f"Prompt: {payload['messages'][0]['content']}")
        print("-" * 50)
        
        response = requests.post(API_URL, headers=headers, json=payload, timeout=30)
        
        print(f"Status Code: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print("✅ SUCCESS!")
            print(f"Response ID: {data.get('id', 'N/A')}")
            print(f"Model Used: {data.get('model', 'N/A')}")
            
            if 'choices' in data and len(data['choices']) > 0:
                content = data['choices'][0]['message']['content']
                print(f"Generated Text: {content}")
            
            if 'usage' in data:
                usage = data['usage']
                print(f"Tokens Used: {usage.get('total_tokens', 'N/A')}")
            
            return True
        else:
            print("❌ FAILED!")
            print(f"Error: {response.text}")
            return False
            
    except requests.exceptions.RequestException as e:
        print("❌ NETWORK ERROR!")
        print(f"Error: {str(e)}")
        return False
    except Exception as e:
        print("❌ UNEXPECTED ERROR!")
        print(f"Error: {str(e)}")
        return False

def test_spring_boot_endpoint():
    """Test the Spring Boot endpoint"""
    
    endpoint = "http://localhost:8036/simple/generate"
    
    payload = {
        "prompt": "Write a short story about AI"
    }
    
    try:
        print("\nTesting Spring Boot OpenRouter endpoint...")
        print(f"URL: {endpoint}")
        print(f"Prompt: {payload['prompt']}")
        print("-" * 50)
        
        response = requests.post(endpoint, json=payload, timeout=30)
        
        print(f"Status Code: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print("✅ SUCCESS!")
            print(f"Status: {data.get('status', 'N/A')}")
            print(f"Response: {data.get('response', 'N/A')}")
            return True
        elif response.status_code == 403:
            print("❌ FORBIDDEN (403)")
            print("This is the issue you're experiencing!")
            print("The Spring Security is blocking the request.")
            print("Check the SecurityConfig to ensure /simple/** endpoints are permitted.")
            return False
        else:
            print("❌ FAILED!")
            print(f"Error: {response.text}")
            return False
            
    except requests.exceptions.ConnectionError:
        print("❌ CONNECTION ERROR!")
        print("Spring Boot application is not running on port 8036")
        return False
    except Exception as e:
        print("❌ UNEXPECTED ERROR!")
        print(f"Error: {str(e)}")
        return False

if __name__ == "__main__":
    print("🚀 OpenRouter Integration Test")
    print("=" * 60)
    
    # Test 1: Direct API call
    api_success = test_openrouter_api()
    
    # Test 2: Spring Boot endpoint
    spring_success = test_spring_boot_endpoint()
    
    print("\n" + "=" * 60)
    print("📊 SUMMARY:")
    print(f"Direct OpenRouter API: {'✅ WORKING' if api_success else '❌ FAILED'}")
    print(f"Spring Boot Endpoint: {'✅ WORKING' if spring_success else '❌ FAILED'}")
    
    if api_success and not spring_success:
        print("\n💡 DIAGNOSIS:")
        print("OpenRouter API is working, but Spring Boot integration has issues.")
        print("Most likely causes:")
        print("1. Spring Security blocking the request (403 Forbidden)")
        print("2. Application not running")
        print("3. Wrong endpoint URL")
    elif api_success and spring_success:
        print("\n🎉 EVERYTHING IS WORKING!")
        print("Your OpenRouter integration is successful!")
    elif not api_success:
        print("\n⚠️  API KEY OR NETWORK ISSUE")
        print("Check your OpenRouter API key and internet connection.")
