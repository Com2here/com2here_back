package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Spec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecRepository extends JpaRepository<Spec, Long>{
    
}
