package br.com.dea.management.academyclass.dto;

import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.employee.dto.EmployeeDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AcademyClassDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private EmployeeDto instructor;

    public static List<AcademyClassDto> fromAcademyClass(List<AcademyClass> academyClasses) {
        return academyClasses.stream().map(student -> {
            AcademyClassDto classDto = AcademyClassDto.fromAcademyClass(student);
            return classDto;
        }).collect(Collectors.toList());
    }

    public static AcademyClassDto fromAcademyClass(AcademyClass academyClass) {
        AcademyClassDto academyClassDto = new AcademyClassDto();
        academyClassDto.setId(academyClass.getId());
        academyClassDto.setStartDate(academyClass.getStartDate());
        academyClassDto.setEndDate(academyClass.getEndDate());

        academyClassDto.setInstructor(EmployeeDto.fromEmployee(academyClass.getInstructor()));

        return academyClassDto;
    }

}
