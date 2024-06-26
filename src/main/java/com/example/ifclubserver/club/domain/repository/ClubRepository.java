package com.example.ifclubserver.club.domain.repository;

import com.example.ifclubserver.club.domain.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findById(Long id);
}
