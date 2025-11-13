package dev.kangmin.pawpal.domain.dogbreed.service;

import dev.kangmin.pawpal.domain.dogbreed.DogBreed;
import dev.kangmin.pawpal.domain.dogbreed.dto.DogBreedInfoDto;
import dev.kangmin.pawpal.domain.dogbreed.repository.DogBreedRepository;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogBreedService {

    private DogBreedRepository dogBreedRepository;

    //모든 견종 id, 이름 주기

    /**
     * 견종의 정보가 필요한 페이지 접속 시 반환
     * @return
     */
    public List<DogBreedInfoDto> getDogBreedInfoList() {
        return dogBreedRepository.findDogBreedInfo();
    }

    public DogBreed findByDogBreedId(Long dogBreedId) {
        return dogBreedRepository.findByDogBreedId(dogBreedId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, DOG_BREED_IS_NOT_EXISTS));
    }

}
