package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Gpu;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpuRepository extends JpaRepository<Gpu, Integer> {
    List<Gpu> findAll();
}
