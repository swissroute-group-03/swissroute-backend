package com.swissroute.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.swissroute.dto.request.RoleRequestDTO;
import com.swissroute.dto.response.RoleResponseDTO;
import com.swissroute.model.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleResponseDTO toDto(Role role);

    Role toEntity(RoleRequestDTO roleRequestDTO);
}
