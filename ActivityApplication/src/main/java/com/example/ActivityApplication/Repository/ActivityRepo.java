package com.example.ActivityApplication.Repository;

import com.example.ActivityApplication.Entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepo extends JpaRepository<Activity,Long> {
}
