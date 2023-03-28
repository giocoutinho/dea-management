package br.com.dea.management.academyclass.service;

import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.exceptions.NotFoundException;
import br.com.dea.management.position.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AcademyClassService {

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private PositionRepository positionRepository;

    public Page<AcademyClass> findAllAcademyClassPaginated(Integer page, Integer pageSize) {
        return this.academyClassRepository.findAllPaginated(PageRequest.of(page, pageSize, Sort.by("startDate").ascending()));
    }

    public AcademyClass findAcademyClassById(Long id) {
        return this.academyClassRepository.findById(id).orElseThrow(() -> new NotFoundException(AcademyClass.class, id));
    }

}
