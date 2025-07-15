package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.ProgramRSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRSpecRepository extends JpaRepository<ProgramRSpec, Long> {

}
