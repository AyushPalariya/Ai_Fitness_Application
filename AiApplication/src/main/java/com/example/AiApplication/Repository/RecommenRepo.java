package com.example.AiApplication.Repository;

import com.example.AiApplication.Entities.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommenRepo extends JpaRepository<Recommendation,Long> {

    List<Recommendation> findByUserId(Long userId);

    Optional<Recommendation> findByActivityId(Long id);
}
