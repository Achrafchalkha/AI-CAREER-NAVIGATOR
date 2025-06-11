package com.example.aicareernavigator.repository;

import com.example.aicareernavigator.model.CareerGuidance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareerGuidanceRepository extends MongoRepository<CareerGuidance, String> {
    
    // Find all career guidance for a specific user
    List<CareerGuidance> findByUserIdOrderByCreatedAtDesc(String userId);
    
    // Find by user email
    List<CareerGuidance> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    
    // Find the latest career guidance for a user
    Optional<CareerGuidance> findFirstByUserIdOrderByCreatedAtDesc(String userId);
    
    // Find career guidance by status
    List<CareerGuidance> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, String status);
    
    // Find career guidance within a date range
    List<CareerGuidance> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
        String userId, Date startDate, Date endDate);
    
    // Count total guidance sessions for a user
    long countByUserId(String userId);
    
    // Find by specific career field/interest
    @Query("{ 'userId': ?0, 'userProfile.desiredField': { $regex: ?1, $options: 'i' } }")
    List<CareerGuidance> findByUserIdAndDesiredFieldContaining(String userId, String desiredField);
    
    // Find by experience level
    @Query("{ 'userId': ?0, 'userProfile.experienceLevel': ?1 }")
    List<CareerGuidance> findByUserIdAndExperienceLevel(String userId, String experienceLevel);
    
    // Delete old records (for cleanup)
    void deleteByCreatedAtBefore(Date date);
}
