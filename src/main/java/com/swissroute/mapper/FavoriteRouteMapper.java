package com.swissroute.mapper;

import com.swissroute.dto.request.FavoriteRouteCreateRequestDTO;
import com.swissroute.dto.response.FavoriteRouteCreateResponseDTO;
import com.swissroute.model.FavoriteRoute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoriteRouteMapper {

    @Mapping(target = "userId", source = "user.id")
    FavoriteRouteCreateResponseDTO toDto(FavoriteRoute favoriteRoute);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    FavoriteRoute toEntity(FavoriteRouteCreateRequestDTO favoriteRouteCreateRequestDTO);

}