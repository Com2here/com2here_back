package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.CpuDetailedMatches;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuDetailedMatchesRepository extends JpaRepository<CpuDetailedMatches, Long> {
    List<CpuDetailedMatches> findByModelIn(Set<String> models);
}
