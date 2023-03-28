package br.com.dea.management.academyclass.repository;

import br.com.dea.management.academyclass.domain.AcademyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyClassRepository extends JpaRepository<AcademyClass, Long> {

    @Query("SELECT a FROM AcademyClass a")
    @EntityGraph(attributePaths = {"instructor", "instructor.user", "instructor.position"})
    public Page<AcademyClass> findAllPaginated(Pageable pageable);

}
