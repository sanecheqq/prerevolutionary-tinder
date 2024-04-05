package com.liga.semin.server.repository;

import com.liga.semin.server.model.GenderType;
import com.liga.semin.server.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        SELECT u FROM User u
            WHERE u.id != :id
                AND (u.mateGender = :allGender OR u.mateGender = :uGender)
                AND (:uMateGender = :allGender OR :uMateGender = u.gender)
    """)
    List<User> findNextWithOffset(
            @Param("id") Long userId,
            @Param("uGender") GenderType userGender,
            @Param("uMateGender") GenderType userMateGender,
            @Param("allGender") GenderType allGender,
            Pageable pageable
    );
}
