package dev.kangmin.pawpal.domain.vaccinationrecord.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.GenerateVaccinationDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.ModifyVaccineDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineDetailDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.repository.VaccinationRecordRepository;
import dev.kangmin.pawpal.golbal.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.golbal.error.exception.ErrorCode.VACCINE_IS_NOT_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class VaccinationRecordService {

    private final VaccinationRecordRepository vaccinationRecordRepository;
    private final DogService dogService;

    public VaccinationRecord findByVaccinationRecordByEmailAndVaccineId(String email, Long vaccineId) {
        return vaccinationRecordRepository.findByVaccinationRecordIdAndMemberEmail(vaccineId, email)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, VACCINE_IS_NOT_EXISTS));
    }

    // 등록
    public void generateVaccinationRecord(String email, GenerateVaccinationDto generateVaccinationDto) {
        Dog dog = dogService.findDogByMemberEmailAndDogId(email, generateVaccinationDto.getDogId());

        VaccinationRecord vaccinationRecord = VaccinationRecord.builder()
                .doseNum(generateVaccinationDto.getNum())
                .vaccineType(generateVaccinationDto.getVaccineType())
                .dog(dog)
                .build();
        vaccinationRecordRepository.save(vaccinationRecord);
    }

    //수정
    public void modifyVaccinationRecord(String email, ModifyVaccineDto modifyVaccineDto) {
        Dog dog = dogService.findDogByMemberEmailAndDogId(email, modifyVaccineDto.getDogId());
        VaccinationRecord vaccinationRecord =
                findByVaccinationRecordByEmailAndVaccineId(email, modifyVaccineDto.getVaccinationRecordId());

        vaccinationRecord.modifyVaccine(modifyVaccineDto);
    }


    //삭제

    //전체 조회
    public List<VaccineInfoDto> getVaccinationRecordList(String email, Long dogId) {

        return vaccinationRecordRepository.findVaccinationRecordListByMemberEmailAndDogId(email, dogId);
    }

    //세부정보
    public VaccineDetailDto getMyDogVaccinationRecordDetail(String email, Long dogId, Long vaccineId) {
        return Optional.ofNullable(vaccinationRecordRepository.findByMemberEmailAndDogIdAndVaccinationRecordId(email, dogId, vaccineId) )
                .map(VaccineDetailDto::of)
                .orElseThrow();
    }
}
