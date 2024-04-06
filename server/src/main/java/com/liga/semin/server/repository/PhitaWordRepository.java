package com.liga.semin.server.repository;

import com.liga.semin.server.model.PhitaWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhitaWordRepository extends JpaRepository<PhitaWord, String> {
}
