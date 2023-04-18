package br.com.dea.management.academyclass.dto;

import br.com.dea.management.academyclass.ClassType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateAcademyClassRequestDto {
    @NotNull(message = "Start date could not be null")
    private LocalDate startDate;
    @NotNull(message = "End date could not be null")
    private LocalDate endDate;
    @NotNull(message = "Class type id could not be null")
    private ClassType classType;
    @NotNull(message = "Instructor id could not be null")
    private Long instructorId;
}