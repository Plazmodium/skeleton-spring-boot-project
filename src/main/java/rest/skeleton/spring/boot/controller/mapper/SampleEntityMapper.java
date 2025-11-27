package rest.skeleton.spring.boot.controller.mapper;

import rest.skeleton.spring.boot.controller.dto.SampleEntityDto;
import rest.skeleton.spring.boot.domain.SampleEntity;

public final class SampleEntityMapper {
    private SampleEntityMapper() {}

    public static SampleEntityDto toDto(SampleEntity entity) {
        if (entity == null) return null;
        SampleEntityDto dto = new SampleEntityDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static SampleEntity from(String name, String description) {
        SampleEntity entity = new SampleEntity();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }
}
