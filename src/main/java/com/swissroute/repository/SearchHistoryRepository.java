package com.swissroute.repository;

import com.swissroute.model.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    Page<SearchHistory> findByUser_IdAndFlgState(
            Long userId,
            String flgState,
            Pageable pageable
    );

    Optional<SearchHistory> findByIdAndUser_IdAndFlgState(
            Long id,
            Long userId,
            String flgState
    );

    List<SearchHistory> findAllByUser_IdAndFlgState(
            Long userId,
            String flgState
    );
}