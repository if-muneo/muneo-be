package ureca.muneobe.common.mplan.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.mplan.entity.*;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.common.mplan.repository.UnapplyMplanRepository;
import ureca.muneobe.common.vector.entity.Fat;
import ureca.muneobe.common.vector.repository.FatRepository;
import ureca.muneobe.common.vector.service.FatService;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

import static org.mockito.Mockito.*;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.batch.job.enabled=false",  // 자동 실행 방지
        "logging.level.org.springframework.retry=DEBUG"
})
@Transactional
@Rollback
@ActiveProfiles("test")
class MplanApplySchedulerTest {

    @Autowired
    private MplanApplyScheduler scheduler;

    @Autowired
    private MplanRepository mplanRepository;

    @Autowired
    private UnapplyMplanRepository unapplyMplanRepository;

    @Autowired
    private FatRepository fatRepository;

    @Autowired
    private AddonGroupRepository addonGroupRepository;

    @Autowired
    private MplanDetailRepository mplanDetailRepository;

    @MockitoBean  // 외부 의존성 모킹
    private FatService fatService;

    @MockitoBean
    private DescriptionGenerator descriptionGenerator;

    @BeforeEach
    void setUp() {
        // FK 관계 고려한 삭제 순서
        fatRepository.deleteAll();
        mplanRepository.deleteAll();
        unapplyMplanRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // @Rollback이 있으므로 자동으로 롤백됨
        // 수동으로 정리할 것이 있다면 여기에 추가
        // 예: 외부 파일 시스템, 캐시 등
    }

    @Test
    @DisplayName("스케줄러 전체 플로우 테스트")
    void applyMplans_success() {
        // given
        AddonGroup savedAddonGroup = addonGroupRepository.save(createTestAddonGroup());
        MplanDetail savedMplanDetail = mplanDetailRepository.save(createTestMplanDetail());

        UnapplyMplan unapplyMplan = createTestUnapplyMplan(savedAddonGroup, savedMplanDetail);
        unapplyMplanRepository.save(unapplyMplan);

        when(descriptionGenerator.generate(any(Mplan.class)))
                .thenReturn("테스트 설명입니다$추가 설명 문장입니다");

        // when
        scheduler.applyMplans();

        // then
        List<Mplan> savedMplans = mplanRepository.findAll();
        List<Fat> savedFats = fatRepository.findAll();

        assertThat(savedMplans).hasSize(1);
        assertThat(savedFats).hasSize(1);

        // 저장된 Mplan 검증
        Mplan savedMplan = savedMplans.get(0);
        assertThat(savedMplan.getName()).isEqualTo("테스트 플랜");
        assertThat(savedMplan.getAddonGroup().getAddonGroupName()).isEqualTo("테스트 애드온 그룹");
        assertThat(savedMplan.getMplanDetail().getMonthlyPrice()).isEqualTo(30000);

        // 저장된 Fat 검증
        Fat savedFat = savedFats.get(0);
        assertThat(savedFat.getName()).isEqualTo("테스트 플랜");
        assertThat(savedFat.getDescription()).isEqualTo("테스트 설명입니다$추가 설명 문장입니다");
        assertThat(savedFat.getMonthlyPrice()).isEqualTo(30000L);
        assertThat(savedFat.getAddon()).isEqualTo("테스트 애드온 그룹");
        assertThat(savedFat.getEmbedding()).isFalse(); // Fat.of()에서 false로 설정

        verify(fatService, times(1)).generateAndSaveAllNullEmbeddings();
        verify(descriptionGenerator, times(1)).generate(any(Mplan.class));
    }

    @Test
    @DisplayName("빈 데이터로 스케줄러 실행 테스트")
    void applyMplans_withEmptyData() {
        // given - 데이터 없음

        // when
        scheduler.applyMplans();

        // then
        assertThat(mplanRepository.findAll()).isEmpty();
        assertThat(fatRepository.findAll()).isEmpty();

        verify(fatService, times(1)).generateAndSaveAllNullEmbeddings();
        verify(descriptionGenerator, never()).generate(any());
    }

    @Test
    @DisplayName("여러 UnapplyMplan 처리 테스트")
    void applyMplans_withMultipleData() {
        // given
        AddonGroup savedAddonGroup = addonGroupRepository.save(createTestAddonGroup());
        MplanDetail savedMplanDetail = mplanDetailRepository.save(createTestMplanDetail());

        List<UnapplyMplan> unapplyMplans = createMultipleTestUnapplyMplans(3, savedAddonGroup, savedMplanDetail);
        unapplyMplanRepository.saveAll(unapplyMplans);

        when(descriptionGenerator.generate(any(Mplan.class)))
                .thenReturn("테스트 설명");

        // when
        scheduler.applyMplans();

        // then
        assertThat(mplanRepository.findAll()).hasSize(3);
        assertThat(fatRepository.findAll()).hasSize(3);

        verify(fatService, times(1)).generateAndSaveAllNullEmbeddings();
        verify(descriptionGenerator, times(3)).generate(any(Mplan.class));
    }

    @Test
    @DisplayName("DescriptionGenerator 예외 발생 시 재시도 테스트")
    void applyMplans_retryOnException() {
        // given
        AddonGroup savedAddonGroup = addonGroupRepository.save(createTestAddonGroup());
        MplanDetail savedMplanDetail = mplanDetailRepository.save(createTestMplanDetail());

        UnapplyMplan unapplyMplan = createTestUnapplyMplan(savedAddonGroup, savedMplanDetail);
        unapplyMplanRepository.save(unapplyMplan);

        // 첫 2번은 실패, 3번째는 성공
        when(descriptionGenerator.generate(any(Mplan.class)))
                .thenThrow(new GlobalException(ErrorCode.DEMO_ERROR))
                .thenThrow(new GlobalException(ErrorCode.DEMO_ERROR))
                .thenReturn("성공한 설명");

        // when & then
        assertDoesNotThrow(() -> scheduler.applyMplans());

        // 3번 호출되었는지 확인 (첫 실행 + 2번 재시도)
        verify(descriptionGenerator, times(3)).generate(any(Mplan.class));
    }

    // === 테스트 헬퍼 메서드들 ===

    private UnapplyMplan createTestUnapplyMplan(AddonGroup addonGroup, MplanDetail mplanDetail) {
        return UnapplyMplan.builder()
                .name("테스트 플랜")
                .addonGroup(addonGroup)
                .mplanDetail(mplanDetail)
                .build();
    }

    private List<UnapplyMplan> createMultipleTestUnapplyMplans(int count, AddonGroup addonGroup, MplanDetail mplanDetail) {
        return IntStream.range(0, count)
                .mapToObj(i -> UnapplyMplan.builder()
                        .name("테스트 플랜 " + i)
                        .addonGroup(addonGroup)
                        .mplanDetail(mplanDetail)
                        .build())
                .collect(Collectors.toList());
    }

    private MplanDetail createTestMplanDetail() {
        return MplanDetail.builder()
                .basicDataAmount(5000)      // 5GB
                .dailyData(200)             // 200MB
                .sharingData(1000)          // 1GB
                .monthlyPrice(30000)        // 30,000원
                .voiceCallVolume(300)       // 300분
                .textMessage(true)          // 문자 무제한
                .subDataSpeed(1)            // 1Mbps
                .qualification(Qualification.ALL)    // 모든 사람
                .mplanType(MplanType.LTE_5G)          // 기본 요금제
                .dataType(DataType.LTE)              // LTE
                .build();
    }

    private AddonGroup createTestAddonGroup() {
        return AddonGroup.builder()
                .addonGroupName("테스트 애드온 그룹")
                .build();
    }
}