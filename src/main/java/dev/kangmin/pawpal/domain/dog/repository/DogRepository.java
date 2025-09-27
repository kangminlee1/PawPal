package dev.kangmin.pawpal.domain.dog.repository;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    Optional<Dog> findByMember(Member member);

    List<Dog> findByMemberEmailAndBreed(String email, String breed);

    List<Dog> findByMemberEmailAndSortBy(String email, boolean sortBy);
}
