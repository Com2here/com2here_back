package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.GpuDetailedMatches;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpuDetailedMatchesRepository extends JpaRepository<GpuDetailedMatches, Long> {
    List<GpuDetailedMatches> findByChipsetIn(Set<String> chipsets);
}
