package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.ComputerRecommendation;
import com.com2here.com2hereback.domain.ProgramPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRecommendationRepository extends JpaRepository<ComputerRecommendation, Long> {
}
