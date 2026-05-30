package com.swissroute.service;

import com.swissroute.constant.EstadoConstants;
import com.swissroute.constant.ExceptionMessagesConstants;
import com.swissroute.dto.request.RutaFavoritaRequestDTO;
import com.swissroute.dto.response.RutaFavoritaResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ConflictException;
import com.swissroute.mapper.RutaFavoritaMapper;
import com.swissroute.model.RutaFavorita;
import com.swissroute.model.User;
import com.swissroute.repository.RutaFavoritaRepository;
import com.swissroute.repository.UserRepository;
import com.swissroute.service.use_cases.RutaFavoritaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RutaFavoritaServiceImpl implements RutaFavoritaUseCase {

    private final RutaFavoritaRepository rutaFavoritaRepository;
    private final UserRepository userRepository;
    private final RutaFavoritaMapper rutaFavoritaMapper;

    @Override
    @Transactional
    public RutaFavoritaResponseDTO guardarRutaFavorita(
            RutaFavoritaRequestDTO requestDTO,
            Long userId
    ) {
        String nombreNormalizado = requestDTO.getNombre().trim();

        boolean existeRutaConMismoNombre = rutaFavoritaRepository
                .existsByUser_IdAndNombreIgnoreCaseAndFlgState(
                        userId,
                        nombreNormalizado,
                        EstadoConstants.ACTIVE
                );

        if (existeRutaConMismoNombre) {
            throw new ConflictException(ExceptionMessagesConstants.RUTA_FAVORITA_NOMBRE_DUPLICADO);
        }

        RutaFavorita rutaFavorita = rutaFavoritaMapper.toEntity(requestDTO);

        rutaFavorita.setNombre(nombreNormalizado);
        rutaFavorita.setTipoTransporte(requestDTO.getTipoTransporte().trim().toLowerCase());

        User usuarioAutenticado = userRepository.getReferenceById(userId);
        rutaFavorita.setUser(usuarioAutenticado);

        RutaFavorita rutaGuardada = rutaFavoritaRepository.save(rutaFavorita);

        return rutaFavoritaMapper.toDto(rutaGuardada);
    }
}