package br.com.dea.management.student.repository;

import br.com.dea.management.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    public Optional<Student> findByUniversity(String university);

    @Query("SELECT s FROM Student s WHERE graduation = :graduation")
    public Optional<Student> findByGraduation(String graduation);

    @Query("SELECT s FROM Student s")
    public Page<Student> findAllPaginated(Pageable pageable);

}
