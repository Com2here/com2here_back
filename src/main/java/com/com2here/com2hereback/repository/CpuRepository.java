package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Cpu;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Long> {
    List<Cpu> findByModelIn(Set<String> models);
}
