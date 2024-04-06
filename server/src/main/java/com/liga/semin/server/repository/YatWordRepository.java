package com.liga.semin.server.repository;

import com.liga.semin.server.model.YatWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YatWordRepository extends JpaRepository<YatWord, String> {
}
