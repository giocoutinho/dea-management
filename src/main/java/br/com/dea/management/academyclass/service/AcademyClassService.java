package br.com.dea.management.academyclass.service;

import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.dto.CreateAcademyClassRequestDto;
import br.com.dea.management.academyclass.dto.UpdateAcademyClassRequestDto;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    public Page<AcademyClass> findAllAcademyClassPaginated(Integer page, Integer pageSize) {
        return this.academyClassRepository.findAllPaginated(PageRequest.of(page, pageSize, Sort.by("startDate").ascending()));
    }

    public AcademyClass findAcademyClassById(Long id) {
        return this.academyClassRepository.findById(id).orElseThrow(() -> new NotFoundException(AcademyClass.class, id));
    }

    public AcademyClass createAcademyClass(CreateAcademyClassRequestDto createAcademyClassRequestDto) {
        Employee instructor = this.employeeRepository.findById(createAcademyClassRequestDto.getInstructorId())
                .orElseThrow(() -> new NotFoundException(AcademyClass.class, createAcademyClassRequestDto.getInstructorId()));

        AcademyClass academyClass = AcademyClass.builder()
                .classType(createAcademyClassRequestDto.getClassType())
                .startDate(createAcademyClassRequestDto.getStartDate())
                .endDate(createAcademyClassRequestDto.getEndDate())
                .instructor(instructor)
                .build();

        return this.academyClassRepository.save(academyClass);
    }

    public AcademyClass updateAcademyClass(Long academyClassId, UpdateAcademyClassRequestDto updateAcademyClasRequestDto) {

        AcademyClass academyClass = this.academyClassRepository.findById(academyClassId)
                .orElseThrow(() -> new NotFoundException(AcademyClass.class, updateAcademyClasRequestDto.getInstructorId()));

        Employee instructor = this.employeeRepository.findById(updateAcademyClasRequestDto.getInstructorId())
                .orElseThrow(() -> new NotFoundException(AcademyClass.class, updateAcademyClasRequestDto.getInstructorId()));

        academyClass.setClassType(updateAcademyClasRequestDto.getClassType());
        academyClass.setStartDate(updateAcademyClasRequestDto.getStartDate());
        academyClass.setEndDate(updateAcademyClasRequestDto.getEndDate());
        academyClass.setInstructor(instructor);

        return this.academyClassRepository.save(academyClass);
    }
}
