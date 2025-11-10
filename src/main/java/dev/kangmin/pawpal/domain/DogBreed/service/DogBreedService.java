package dev.kangmin.pawpal.domain.DogBreed.service;

import dev.kangmin.pawpal.domain.DogBreed.dto.DogBreedInfoDto;
import dev.kangmin.pawpal.domain.DogBreed.repository.DogBreedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
