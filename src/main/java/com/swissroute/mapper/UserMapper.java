package com.swissroute.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.swissroute.dto.request.UserRequestDTO;
import com.swissroute.dto.response.UserResponseDTO;
import com.swissroute.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = RoleMapper.class)
public interface UserMapper {
    UserResponseDTO toDto(User user);

    User toEntity(UserRequestDTO userRequestDTO);
}
