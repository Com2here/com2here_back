package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.common.ProgramPurpose;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    // Optional<Program> findByMainProgramIgnoreCaseAndPurpose(String mainProgram, ProgramPurpose purpose);

    @Query("""
        select p
        from Program p
        where (:search is null or p.program like %:search%)
        and (:purpose is null or p.purpose = :purpose)
    """)
    Page<Program> findPage(@Param("search") String search,
        @Param("purpose") ProgramPurpose purpose,
        Pageable pageable);
}
