package dev.kangmin.pawpal.domain.vaccinationrecord.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.GenerateVaccinationDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.ModifyVaccineDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineDetailDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.repository.VaccinationRecordRepository;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static dev.kangmin.pawpal.global.error.exception.ErrorCode.VACCINE_IS_NOT_EXISTS;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class VaccinationRecordService {

    private final VaccinationRecordRepository vaccinationRecordRepository;
    private final DogService dogService;

    /**
     * 예방접종 기록 찾기
     * @param memberId
     * @param vaccineId
     * @return
     */
    public VaccinationRecord findByVaccinationRecordByMemberIdAndDogIdAndVaccineId(Long memberId, Long dogId, Long vaccineId) {
        return vaccinationRecordRepository.findVaccinationRecordByMemberIdAndDogIdAndVaccineId(memberId, dogId, vaccineId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, VACCINE_IS_NOT_EXISTS));
    }

    /**
     * 예방 접종 기록 등록
     * @param member
     * @param generateVaccinationDto
     */
    @Transactional
    public void generateVaccinationRecord(Member member, GenerateVaccinationDto generateVaccinationDto) {
        Dog dog = dogService.findDogByMemberIdAndDogId(member.getMemberId(), generateVaccinationDto.getDogId());

        VaccinationRecord vaccinationRecord = VaccinationRecord.builder()
                .doseNum(generateVaccinationDto.getNum())
                .vaccineType(generateVaccinationDto.getVaccineType())
                .dog(dog)
                .build();
        vaccinationRecordRepository.save(vaccinationRecord);
    }


    /**
     * 예방접종 기록 수정
     * @param member
     * @param modifyVaccineDto
     */
    @Transactional
    public void modifyVaccinationRecord(Member member, ModifyVaccineDto modifyVaccineDto) {
        Dog dog = dogService.findDogByMemberIdAndDogId(member.getMemberId(), modifyVaccineDto.getDogId());

        if (!dog.getDogId().equals(modifyVaccineDto.getDogId())) {
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        VaccinationRecord vaccinationRecord =
                findByVaccinationRecordByMemberIdAndDogIdAndVaccineId(member.getMemberId(), modifyVaccineDto.getDogId(), modifyVaccineDto.getVaccinationRecordId());

        vaccinationRecord.modifyVaccine(modifyVaccineDto);
    }


    //삭제 -> 보류
//    public void deleteVaccinationRecord(Member member, ModifyVaccineDto modifyVaccineDto) {

    /**
     * 나의 강아지의 전체 예방 접종 기록
     * @param member
     * @param dogId
     * @return
     */
    public List<VaccineInfoDto> getVaccinationRecordList(Member member, Long dogId) {
        return vaccinationRecordRepository.findVaccinationRecordListByMemberIdAndDogId(member.getMemberId(), dogId);
    }


    /**
     * 내 강아지의 예방 접종 기록 상세 조회
     * @param member
     * @param dogId
     * @param vaccineId
     * @return
     */
    public VaccineDetailDto getMyDogVaccinationRecordDetail(Member member, Long dogId, Long vaccineId) {
//        return Optional.ofNullable(vaccinationRecordRepository.findByMemberEmailAndDogIdAndVaccinationRecordId(email, dogId, vaccineId) )
//                .map(VaccineDetailDto::of)
//                .orElseThrow(); -> N+1
        Dog dog = dogService.findDogByMemberIdAndDogId(member.getMemberId(), dogId);

        if (!dog.getDogId().equals(dogId)) {
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        VaccinationRecord vaccinationRecord =
                findByVaccinationRecordByMemberIdAndDogIdAndVaccineId(member.getMemberId(), dogId, vaccineId);
        if(!vaccinationRecord.getDog().getDogId().equals(dogId)){
            throw new CustomException(FORBIDDEN, VACCINE_OWNER_MISMATCH);
        }

        return VaccineDetailDto.of(vaccinationRecord);
//        return vaccinationRecordRepository.findByMemberIdAndDogIdAndVaccinationRecordId(member.getMemberId(), dogId, vaccineId);
    }
}
