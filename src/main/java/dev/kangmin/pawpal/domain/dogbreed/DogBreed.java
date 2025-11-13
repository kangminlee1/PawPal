package dev.kangmin.pawpal.domain.dogbreed;

import dev.kangmin.pawpal.domain.dog.Dog;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DogBreed {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long dogBreedId;
    private String breed;

    private double minHeight;
    private double maxHeight;

    private double minWeight;
    private double maxWeight;

    private String dogSize;
    private String personality;//성격

    @OneToMany(mappedBy = "dogBreed")
    private List<Dog> dogList;
}
