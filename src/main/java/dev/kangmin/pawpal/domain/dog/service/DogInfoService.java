package dev.kangmin.pawpal.domain.dog.service;

import dev.kangmin.pawpal.domain.healthrecord.repository.HealthRecordRepository;
import dev.kangmin.pawpal.domain.healthrecord.service.HealthRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DogInfoService {

    private HealthRecordService healthRecordService;

}
