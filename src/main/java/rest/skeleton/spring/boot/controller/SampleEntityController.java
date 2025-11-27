package rest.skeleton.spring.boot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rest.skeleton.spring.boot.controller.dto.CreateSampleEntityRequest;
import rest.skeleton.spring.boot.controller.dto.SampleEntityDto;
import rest.skeleton.spring.boot.controller.dto.UpdateSampleEntityRequest;
import rest.skeleton.spring.boot.controller.error.ApiError;
import rest.skeleton.spring.boot.controller.mapper.SampleEntityMapper;
import rest.skeleton.spring.boot.domain.SampleEntity;
import rest.skeleton.spring.boot.service.SampleEntityService;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sample-entities")
@Validated
@Tag(name = "Sample Entities", description = "CRUD operations for SampleEntity")
public class SampleEntityController {

    private final SampleEntityService service;

    public SampleEntityController(SampleEntityService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new SampleEntity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = SampleEntityDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<SampleEntityDto> create(@Valid @RequestBody CreateSampleEntityRequest request,
                                                  UriComponentsBuilder uriBuilder) {
        SampleEntity toCreate = SampleEntityMapper.from(request.getName(), request.getDescription());
        SampleEntity created = service.create(toCreate);
        SampleEntityDto body = SampleEntityMapper.toDto(created);
        URI location = uriBuilder.path("/api/v1/sample-entities/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a SampleEntity by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = SampleEntityDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public SampleEntityDto getById(@PathVariable Long id) {
        return SampleEntityMapper.toDto(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "List SampleEntities (paginated)")
    @ApiResponse(responseCode = "200", description = "OK")
    public Page<SampleEntityDto> list(@ParameterObject Pageable pageable) {
        return service.list(pageable).map(SampleEntityMapper::toDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a SampleEntity (PUT)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated",
                    content = @Content(schema = @Schema(implementation = SampleEntityDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid argument",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public SampleEntityDto updatePut(@PathVariable Long id,
                                     @Valid @RequestBody UpdateSampleEntityRequest request) {
        // For PUT, name must be provided and not blank
        String name = Objects.requireNonNullElse(request.getName(), "").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name must not be blank for PUT");
        }
        SampleEntity updated = service.updatePut(id, name, request.getDescription());
        return SampleEntityMapper.toDto(updated);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a SampleEntity (PATCH)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated",
                    content = @Content(schema = @Schema(implementation = SampleEntityDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public SampleEntityDto updatePatch(@PathVariable Long id,
                                       @Valid @RequestBody UpdateSampleEntityRequest request) {
        Optional<String> name = Optional.ofNullable(request.getName()).map(String::trim).filter(s -> !s.isEmpty());
        Optional<String> description = Optional.ofNullable(request.getDescription());
        SampleEntity updated = service.updatePatch(id, name, description);
        return SampleEntityMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a SampleEntity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
