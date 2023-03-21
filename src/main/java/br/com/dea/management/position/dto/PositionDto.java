package br.com.dea.management.position.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionDto {

    private Long id;
    private String description;
    private String seniority;

}
