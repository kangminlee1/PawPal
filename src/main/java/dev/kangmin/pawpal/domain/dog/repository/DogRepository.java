package dev.kangmin.pawpal.domain.dog.repository;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.repository.querydsl.DogRepositoryCustom;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long>, DogRepositoryCustom {

}
