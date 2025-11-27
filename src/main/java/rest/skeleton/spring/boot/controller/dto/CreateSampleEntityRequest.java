package rest.skeleton.spring.boot.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateSampleEntityRequest", description = "Payload to create a new SampleEntity")
public class CreateSampleEntityRequest {
    @Schema(description = "Name of the entity", example = "Alpha", maxLength = 255)
    @NotBlank
    @Size(max = 255)
    private String name;

    @Schema(description = "Optional description", example = "First sample entity", maxLength = 1000)
    @Size(max = 1000)
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
