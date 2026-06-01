package com.swissroute.repository;

import com.swissroute.model.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {

    boolean existsByUser_IdAndNombreIgnoreCaseAndFlgState(
            Long userId,
            String nombre,
            String flgState
    );
}