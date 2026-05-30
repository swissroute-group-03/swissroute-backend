package com.swissroute.mapper;

import com.swissroute.dto.request.RutaFavoritaRequestDTO;
import com.swissroute.dto.response.RutaFavoritaResponseDTO;
import com.swissroute.model.RutaFavorita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RutaFavoritaMapper {

    @Mapping(target = "userId", source = "user.id")
    RutaFavoritaResponseDTO toDto(RutaFavorita rutaFavorita);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    RutaFavorita toEntity(RutaFavoritaRequestDTO rutaFavoritaRequestDTO);

}