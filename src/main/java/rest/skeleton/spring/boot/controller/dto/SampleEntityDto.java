package rest.skeleton.spring.boot.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "SampleEntity", description = "API representation of SampleEntity")
public class SampleEntityDto {
    @Schema(description = "Unique identifier", example = "1")
    private Long id;
    @Schema(description = "Entity name", example = "Alpha")
    private String name;
    @Schema(description = "Entity description", example = "First sample entity")
    private String description;
    @Schema(description = "Creation timestamp (UTC)", example = "2025-01-01T12:00:00Z")
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
