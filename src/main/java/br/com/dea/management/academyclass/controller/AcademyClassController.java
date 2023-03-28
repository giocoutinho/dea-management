package br.com.dea.management.academyclass.controller;

import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.dto.AcademyClassDto;
import br.com.dea.management.academyclass.service.AcademyClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "AcademyClass", description = "The AcademyClass API")
public class AcademyClassController {

    @Autowired
    AcademyClassService academyClassService;

    @Operation(summary = "Load the list of academyClass paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Page or Page Size params not valid"),
            @ApiResponse(responseCode = "500", description = "Error fetching academyClass list"),
    })
    @GetMapping("/academy-class")
    public Page<AcademyClassDto> getAcademyClass(@RequestParam(required = true) Integer page,
                                                 @RequestParam(required = true) Integer pageSize) {

        log.info(String.format("Fetching academyClass : page : %s : pageSize", page, pageSize));

        Page<AcademyClass> academyClassPaged = this.academyClassService.findAllAcademyClassPaginated(page, pageSize);
        Page<AcademyClassDto> academyClasses = academyClassPaged.map(academyClass -> AcademyClassDto.fromAcademyClass(academyClass));

        log.info(String.format("AcademyClass loaded successfully : AcademyClass : %s : pageSize", academyClasses.getContent().size()));

        return academyClasses;

    }

    @Operation(summary = "Load the academyClass by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "AcademyClass Id invalid"),
            @ApiResponse(responseCode = "404", description = "AcademyClass Not found"),
            @ApiResponse(responseCode = "500", description = "Error fetching academyClass list"),
    })
    @GetMapping("/academy-class/{id}")
    public AcademyClassDto getAcademyClass(@PathVariable Long id) {

        log.info(String.format("Fetching academyClass by id : Id : %s", id));

        AcademyClassDto academyClass = AcademyClassDto.fromAcademyClass(this.academyClassService.findAcademyClassById(id));

        log.info(String.format("AcademyClass loaded successfully : AcademyClass : %s", academyClass));

        return academyClass;

    }

}
