package com.swissroute.repository;

import com.swissroute.model.RutaFavorita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaFavoritaRepository extends JpaRepository<RutaFavorita, Long> {

    boolean existsByUser_IdAndNombreIgnoreCaseAndFlgState(
            Long userId,
            String nombre,
            String flgState
    );
}