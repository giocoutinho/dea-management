package br.com.dea.management.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateProjectRequestDto {
    @NotNull(message = "Name could not be null")
    private String name;
    @NotNull(message = "Start date could not be null")
    private LocalDate startDate;
    @NotNull(message = "End date could not be null")
    private LocalDate endDate;
    @NotNull(message = "Client could not be null")
    private String client;
    @NotNull(message = "External product manager could not be null")
    private String externalProductManager;
    @NotNull(message = "Product owner could not be null")
    private Long productOwnerId;
    @NotNull(message = "Scrum master could not be null")
    private Long scrumMasterId;
}