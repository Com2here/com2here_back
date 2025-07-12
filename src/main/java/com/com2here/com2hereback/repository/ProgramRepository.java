package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.ProgramPurpose;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    Optional<Program> findByMainProgramIgnoreCaseAndPurpose(String mainProgram, ProgramPurpose purpose);

}
