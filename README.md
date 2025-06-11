# 🚀 AI Career Navigator

An intelligent career guidance platform that provides personalized career recommendations, learning roadmaps, and job market insights using AI-powered analysis.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Usage Examples](#usage-examples)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### 🔐 Authentication & Security
- **JWT-based Authentication** - Secure user registration and login
- **Password Encryption** - BCrypt password hashing
- **Token-based Authorization** - Stateless authentication system

### 🤖 AI-Powered Career Guidance
- **Personalized Recommendations** - Career path suggestions based on user profile
- **Learning Roadmaps** - Step-by-step skill development plans
- **Course Recommendations** - Curated online courses with providers and URLs
- **Project Ideas** - Practical projects matching user interests and level
- **Skills Tracking** - Key skills to develop and monitor
- **Job Market Insights** - Salary data, demand levels, and hiring trends

### 💾 Data Persistence
- **Career History** - Save and retrieve all career guidance sessions
- **User Profiles** - Persistent user data and preferences
- **MongoDB Integration** - Scalable NoSQL database storage

### 📊 Analytics & Management
- **User Statistics** - Track guidance sessions and progress
- **History Management** - View, search, and delete past sessions
- **Data Reprocessing** - Fix and update existing records

## 🛠 Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.5.0** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data MongoDB** - Database integration
- **JWT (JSON Web Tokens)** - Authentication tokens

### Database
- **MongoDB Atlas** - Cloud-hosted NoSQL database
- **SSL/TLS Encryption** - Secure database connections

### AI Integration
- **OpenRouter API** - AI model access and management
- **GPT-4o-mini** - Advanced language model for career guidance
- **JSON Structured Responses** - Consistent data format

### Tools & Libraries
- **Maven** - Dependency management and build tool
- **Lombok** - Code generation and boilerplate reduction
- **Jackson** - JSON processing
- **WebClient** - Reactive HTTP client

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MongoDB Atlas Account** (or local MongoDB instance)
- **OpenRouter API Key**

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/Achrafchalkha/AI-CAREER-NAVIGATOR.git
cd AI-CAREER-NAVIGATOR
```

### 2. Configure Environment Variables
Update `src/main/resources/application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.uri=your_mongodb_connection_string
spring.data.mongodb.database=aicareernavigator

# OpenRouter API Configuration
openrouter.api.key=your_openrouter_api_key

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8036`

## ⚙️ Configuration

### MongoDB Setup
1. Create a MongoDB Atlas account
2. Create a new cluster
3. Add your IP address to the whitelist
4. Create a database user
5. Get the connection string and update `application.properties`

### OpenRouter API Setup
1. Sign up at [OpenRouter.ai](https://openrouter.ai)
2. Generate an API key
3. Add the key to `application.properties`

### SSL Configuration
For MongoDB Atlas SSL connections:
```properties
spring.data.mongodb.ssl.enabled=true
spring.data.mongodb.ssl.invalid-hostname-allowed=true
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

#### Get User Profile
```http
GET /api/auth/profile
Authorization: Bearer {jwt_token}
```

### Career Guidance Endpoints

#### Generate Career Guidance
```http
POST /api/career/guidance
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "experienceLevel": "Intermediate",
    "interests": "Web Development",
    "education": "Bachelor's in Computer Science",
    "age": 23,
    "desiredField": "Frontend Development",
    "softSkills": "Problem-solving, adaptability, teamwork"
}
```

#### Get Career History
```http
GET /api/career/history
Authorization: Bearer {jwt_token}
```

#### Get Latest Career Guidance
```http
GET /api/career/latest
Authorization: Bearer {jwt_token}
```

#### Get Specific Career Guidance
```http
GET /api/career/history/{id}
Authorization: Bearer {jwt_token}
```

#### Get User Statistics
```http
GET /api/career/stats
Authorization: Bearer {jwt_token}
```

#### Delete Career Guidance
```http
DELETE /api/career/history/{id}
Authorization: Bearer {jwt_token}
```

### Utility Endpoints

#### Reprocess Career Guidance
```http
POST /api/career/reprocess/{id}
Authorization: Bearer {jwt_token}
```

#### Reprocess All User Data
```http
POST /api/career/reprocess-all
Authorization: Bearer {jwt_token}
```

#### Public Career Guidance (No Auth)
```http
POST /api/career/guidance/public
Content-Type: application/json

{
    "experienceLevel": "Beginner",
    "interests": "Data Science",
    "education": "High School"
}
```

## 💡 Usage Examples

### Complete User Journey

1. **Register a new user**
2. **Login and get JWT token**
3. **Generate career guidance**
4. **View career history**
5. **Get user statistics**

### Sample Career Guidance Response
```json
{
    "status": "success",
    "user_id": "user123",
    "guidance_id": "guidance456",
    "created_at": "2024-01-15T10:30:00Z",
    "guidance": {
        "career_suggestions": [
            "Frontend Developer",
            "Full-Stack Developer",
            "UI/UX Developer"
        ],
        "roadmap": [
            "Master React and modern JavaScript",
            "Learn TypeScript for better code quality",
            "Study responsive design principles"
        ],
        "top_courses": [
            {
                "title": "Advanced React Development",
                "provider": "Udemy",
                "url": "https://www.udemy.com/course/react-advanced"
            }
        ],
        "project_ideas": [
            "Build a task management app",
            "Create a personal portfolio website"
        ],
        "skills_to_track": [
            "React.js",
            "TypeScript",
            "CSS Grid/Flexbox"
        ],
        "job_market": {
            "average_salary_usd": "$75,000 - $120,000",
            "job_demand_level": "High",
            "top_countries_hiring": [
                "United States",
                "Canada",
                "Germany"
            ]
        }
    }
}
```

## 🗄️ Database Schema

### Users Collection
```javascript
{
    "_id": "ObjectId",
    "email": "user@example.com",
    "password": "hashed_password",
    "firstName": "John",
    "lastName": "Doe",
    "createdAt": "Date"
}
```

### Career Guidance Collection
```javascript
{
    "_id": "ObjectId",
    "userId": "user_id",
    "userEmail": "user@example.com",
    "userProfile": {
        "experienceLevel": "Intermediate",
        "interests": "Web Development",
        // ... other profile fields
    },
    "careerSuggestions": ["Frontend Developer", "..."],
    "roadmap": ["Step 1", "Step 2", "..."],
    "topCourses": [
        {
            "title": "Course Title",
            "provider": "Provider",
            "url": "https://..."
        }
    ],
    "projectIdeas": ["Project 1", "..."],
    "skillsToTrack": ["Skill 1", "..."],
    "jobMarket": {
        "averageSalaryUsd": "$75,000",
        "jobDemandLevel": "High",
        "topCountriesHiring": ["USA", "..."]
    },
    "rawAiResponse": "Full AI response...",
    "createdAt": "Date",
    "updatedAt": "Date",
    "aiModel": "openai/gpt-4o-mini",
    "status": "success"
}
```

## 🔧 Development

### Running in Development Mode
```bash
# Run with auto-restart
mvn spring-boot:run

# Run with debug mode
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### Building for Production
```bash
# Create JAR file
mvn clean package -DskipTests

# Run JAR file
java -jar target/ai-career-navigator-0.0.1-SNAPSHOT.jar
```

### Testing with Postman
1. Import the API collection (if available)
2. Set up environment variables for base URL and JWT token
3. Test authentication endpoints first
4. Use the JWT token for protected endpoints

## 🐛 Troubleshooting

### Common Issues

#### MongoDB Connection Issues
```
Error: SSLException: Received fatal alert: internal_error
```
**Solution:** Update `application.properties`:
```properties
spring.data.mongodb.ssl.invalid-hostname-allowed=true
```

#### OpenRouter 401 Unauthorized
```
Error: 401 Unauthorized from POST https://openrouter.ai/api/v1/chat/completions
```
**Solutions:**
1. Verify your OpenRouter API key is correct
2. Check if you have sufficient credits
3. Ensure the API key has proper permissions

#### JWT Token Issues
```
Error: Invalid or expired token
```
**Solutions:**
1. Check token expiration (default: 24 hours)
2. Verify JWT secret configuration
3. Ensure proper Bearer token format

#### Null Career Guidance Fields
If you see null values in structured fields:
```http
POST /api/career/reprocess-all
Authorization: Bearer {jwt_token}
```

### Logging Configuration
Enable debug logging in `application.properties`:
```properties
logging.level.com.example.aicareernavigator=DEBUG
logging.level.org.springframework.data.mongodb=DEBUG
```

## 🚀 Deployment

### Docker Deployment (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/ai-career-navigator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8036
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables for Production
```bash
export MONGODB_URI="your_production_mongodb_uri"
export OPENROUTER_API_KEY="your_production_api_key"
export JWT_SECRET="your_production_jwt_secret"
```

## 📈 Performance Considerations

- **Connection Pooling:** MongoDB connection pool configured for 50 connections
- **Timeout Settings:** 30-second timeouts for API calls
- **Caching:** Consider implementing Redis for JWT token caching
- **Rate Limiting:** Implement rate limiting for OpenRouter API calls

## 🔒 Security Best Practices

- **Environment Variables:** Never commit API keys or secrets
- **HTTPS:** Use HTTPS in production
- **CORS:** Configure CORS for specific domains in production
- **Input Validation:** Validate all user inputs
- **SQL Injection:** Use parameterized queries (MongoDB is NoSQL but still validate)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding conventions
- Write unit tests for new features
- Update documentation for API changes
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Achraf Chalkha** - *Initial work* - [GitHub](https://github.com/Achrafchalkha)

## 🙏 Acknowledgments

- [OpenRouter.ai](https://openrouter.ai) for AI model access
- [MongoDB Atlas](https://www.mongodb.com/atlas) for database hosting
- [Spring Boot](https://spring.io/projects/spring-boot) for the application framework
- [JWT.io](https://jwt.io) for JWT implementation guidance

## 📞 Support

For support, email achrafchalkha@gmail.com or create an issue in the GitHub repository.

## 🔄 Version History

- **v1.0.0** - Initial release with JWT auth and OpenRouter integration
- **v1.1.0** - Added MongoDB persistence and career history
- **v1.2.0** - Added data reprocessing and improved JSON parsing

---

**Made with ❤️ by Achraf Chalkha**
