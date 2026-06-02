package com.swissroute.mapper;

import com.swissroute.dto.request.SearchHistoryRequestDTO;
import com.swissroute.dto.response.SearchHistoryResponseDTO;
import com.swissroute.model.SearchHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SearchHistoryMapper {

    @Mapping(target = "origen", source = "from")
    @Mapping(target = "destino", source = "to")
    @Mapping(target = "numResultados", source = "resultCount")
    @Mapping(target = "userId", source = "userId")
    SearchHistoryRequestDTO toRequestDTO(
            String from,
            String to,
            Integer resultCount,
            Long userId
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "fechaConsulta", ignore = true)
    SearchHistory toEntity(SearchHistoryRequestDTO searchHistoryRequestDTO);

    @Mapping(target = "userId", source = "user.id")
    SearchHistoryResponseDTO toDto(SearchHistory searchHistory);
}