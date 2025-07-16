package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Program;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findAllByProgramIn(List<String> programs);
}
