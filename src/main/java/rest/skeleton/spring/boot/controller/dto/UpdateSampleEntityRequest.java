package rest.skeleton.spring.boot.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Used for PUT/PATCH updates. For PUT, name is required at controller level; for PATCH it's optional.
 */
@Schema(name = "UpdateSampleEntityRequest", description = "Payload to fully (PUT) or partially (PATCH) update a SampleEntity")
public class UpdateSampleEntityRequest {
    @Schema(description = "New name; required for PUT, optional for PATCH", maxLength = 255, example = "Alpha 2")
    @Size(max = 255)
    private String name; // optional for PATCH

    @Schema(description = "New description; optional", maxLength = 1000, example = "Updated description")
    @Size(max = 1000)
    private String description; // optional

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
