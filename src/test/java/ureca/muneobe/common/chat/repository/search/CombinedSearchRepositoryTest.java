package ureca.muneobe.common.chat.repository.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ureca.muneobe.common.chat.entity.AddonType;
import ureca.muneobe.common.chat.entity.DataType;
import ureca.muneobe.common.chat.entity.MplanType;
import ureca.muneobe.common.chat.entity.Qualification;
import ureca.muneobe.common.chat.service.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.rdb.input.Condition;
import ureca.muneobe.common.chat.service.rdb.input.MplanCondition;
import ureca.muneobe.common.chat.service.rdb.input.Range;
import ureca.muneobe.common.chat.service.rdb.output.FindingAddon;
import ureca.muneobe.common.chat.service.rdb.output.FindingMplan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CombinedSearchRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("init.sql");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",    postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    private CombinedSearchRepository combinedSearchRepository;

    @Test
    void 모바일_요금제_부가서비스_이름_검색() {
        // given
        List<String> names = List.of("아이들나라", "발신번호표시제한", "휴대폰결제 (일반/정기결제)");

        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(names)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        int totalCnt = 0;

        for(FindingMplan result: results) {
            List<FindingAddon> addons = result.getFindingAddons();
            Set<String> name = new HashSet<>();
            for(FindingAddon addon: addons) {
                if(names.contains(addon.getName())) {
                    name.add(addon.getName());
                }
            }

            if(name.size() == names.size()) {
                totalCnt++;
            }
        }

        // then
        assertThat(totalCnt).isEqualTo(results.size());
    }


    @Test
    void 모바일_요금제_부가서비스_부가서비스_타입_검색() {
        // given
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CALL, AddonType.CONVENIENCE);

        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .addonTypes(addonTypes)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        Set<AddonType> value = new HashSet<>();

        int totalCnt = 0;

        for(FindingMplan result: results) {
            List<FindingAddon> addons = result.getFindingAddons();
            for(FindingAddon addon: addons) {
                if(addonTypes.contains(addon.getAddonType())) {
                    value.add(addon.getAddonType());
                }
            }

            if(value.size() == addonTypes.size()) {
                totalCnt++;
            }
        }

        // then
        assertThat(totalCnt).isEqualTo(results.size());
    }

    @Test
    void 모바일_요금제_부가서비스_금액_검색() {
        // given
        Range price = Range.builder()
                .baseNumber(0)
                .operator("이상")
                .build();

        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .price(price)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        int totalCnt = 0;

        for(FindingMplan result: results) {
            List<FindingAddon> addons = result.getFindingAddons();
            int cnt = 0;
            for(FindingAddon addon: addons) {
                if(addon.getPrice() >= 0) {
                    cnt++;
                }
            }

            if(cnt == addons.size()) {
                totalCnt++;
            }
        }

        // then
        assertThat(totalCnt).isEqualTo(results.size());
    }


    @Test
    void 모바일_요금제_부가서비스_이름_부가서비스_타입_검색() {

        // given
        List<String> names = List.of("아이들나라", "발신번호표시제한", "휴대폰결제 (일반/정기결제)");
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CALL, AddonType.CONVENIENCE);

        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(names)
                                .addonTypes(addonTypes)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        int totalCnt = 0;

        for(FindingMplan result: results) {
            List<FindingAddon> addons = result.getFindingAddons();
            Set<String> name = new HashSet<>();
            Set<AddonType> addonValue = new HashSet<>();
            for(FindingAddon addon: addons) {
                if(names.contains(addon.getName())) {
                    name.add(addon.getName());
                }

                if(addonTypes.contains(addon.getAddonType())) {
                    addonValue.add(addon.getAddonType());
                }
            }

            if(name.size() == names.size() && addonValue.size() == addonTypes.size()) {
                totalCnt++;
            }
        }

        // then
        assertThat(totalCnt).isEqualTo(results.size());
    }

    @Test
    void 모바일_요금제_이름_검색() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .name("5G 프리미어 에센셜")
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
    }

    @Test
    void 모바일_요금제_월가격_검색_5만원_이상() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .monthlyPrice(Range.builder().baseNumber(50000).operator("이상").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getMonthlyPrice() >= 50000);
    }

    @Test
    void 모바일_요금제_월가격_검색_4만원_이하() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .monthlyPrice(Range.builder().baseNumber(40000).operator("이하").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results)
                .allMatch(r -> r.getMonthlyPrice() <= 40000);
    }

    @Test
    void 모바일_요금제_월가격_검색_3만원_이상_5만원_이하() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .monthlyPrice(Range.builder().baseNumber(30000).subNumber(50000).operator("사이").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results)
                .allMatch(r -> r.getMonthlyPrice() <= 40000);
    }

    @Test
    void 모바일_요금제_월가격_검색_5만원_초과() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .monthlyPrice(Range.builder().baseNumber(50000).operator("초과").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getMonthlyPrice() > 50000);
    }

    @Test
    void 모바일_요금제_monthly_price_검색_5만원_미만() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .monthlyPrice(Range.builder().baseNumber(50000).operator("미만").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getMonthlyPrice() < 50000);
    }

    @Test
    void 모바일_요금제_basic_data_amount_검색_5만원_이상() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .basicDataAmount(Range.builder().baseNumber(50000).operator("이상").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getBasicDataAmount() >= 50000);
    }

    @Test
    void 모바일_요금제_daily_date_검색_5천원_이상() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .dailyData(Range.builder().baseNumber(5000).operator("이상").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getDailyData() >= 5000);
    }

    @Test
    void 모바일_요금제_sharing_data_검색_5만원_이상() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .sharingData(Range.builder().baseNumber(50000).operator("이상").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getSharingData() >= 5000);
    }

    @Test
    void 모바일_요금제_voice_call_volume_검색_0_이상() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .dailyData(Range.builder().baseNumber(0).operator("이상").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getVoiceCallVolume() >= 0);
    }

    @Test
    void 모바일_요금제_text_message_검색_true() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .textMessage(true)
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getTextMessage().equals(true));
    }

    @Test
    void 모바일_요금제_sub_data_speed_5000이상() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .subDataSpeed(Range.builder().baseNumber(5000).operator("이상").build())
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getSubDataSpeed() >= 5000);
    }

    @Test
    void 모바일_요금제_qualification_ALL() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .qualification(Qualification.ALL)
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getQualification().equals(Qualification.ALL));
    }

    @Test
    void 모바일_요금제_dataType_5G() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .dataType(DataType._5G)
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getDataType().equals(DataType._5G));
    }

    @Test
    void 모바일_요금제_mplanType_LTE_5G() {
        Condition condition = Condition.builder().mplanCondition(
                MplanCondition.builder()
                        .mplanType(MplanType.LTE_5G)
                        .build()).build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);
        assertThat(results)
                .allMatch(r -> r.getMplanType().equals(MplanType.LTE_5G));
    }

    @Test
    void 모바일_요금제_월가격_및_부가서비스_이름_검색() {
        // given
        Range monthlyRange = Range.builder()
                .baseNumber(30000)
                .subNumber(50000)
                .operator("사이")
                .build();
        List<String> addonNames = List.of("아이들나라", "발신번호표시제한");

        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .monthlyPrice(monthlyRange)
                                .build()
                )
                .addonCondition(
                        AddonCondition.builder()
                                .names(addonNames)
                                .build()
                )
                .build();

        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        // then
        assertThat(results).allMatch(r -> {

            Integer price = r.getMonthlyPrice();
            boolean priceOk = (price != null && price >= 30000 && price <= 50000);
            if (!priceOk) return false;

            Set<String> foundNames = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {
                if (addonNames.contains(fa.getName())) {
                    foundNames.add(fa.getName());
                }
            }
            return foundNames.size() == addonNames.size();
        });
    }

    @Test
    void 모바일_요금제_데이터타입_및_부가서비스_타입_검색() {
        // given: 5G 요금제, 부가서비스 타입 MEDIA, CALL
        MplanCondition mcond = MplanCondition.builder()
                .dataType(DataType._5G)
                .build();
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CALL);

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(
                        AddonCondition.builder()
                                .addonTypes(addonTypes)
                                .build()
                )
                .build();

        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        // then: dataType 일치 및 부가서비스 타입 포함 여부 확인
        assertThat(results).allMatch(r -> {
            // 1) 요금제 dataType 체크
            if (!DataType._5G.equals(r.getDataType())) {
                return false;
            }
            // 2) 부가서비스 타입 체크: 적어도 지정한 타입들이 모두 존재하는지
            Set<AddonType> foundTypes = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {
                if (addonTypes.contains(fa.getAddonType())) {
                    foundTypes.add(fa.getAddonType());
                }
            }
            return foundTypes.size() == addonTypes.size();
        });
    }

    @Test
    void 모바일_요금제_이름_및_부가서비스_가격범위_검색() {
        // given: 요금제 이름 지정, 부가서비스 가격 >= 0 && <= 10000
        String planName = "5G 프리미어 에센셜";
        Range addonPriceRange = Range.builder()
                .baseNumber(0)
                .subNumber(10000)
                .operator("사이")
                .build();

        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .name(planName)
                                .build()
                )
                .addonCondition(
                        AddonCondition.builder()
                                .price(addonPriceRange)
                                .build()
                )
                .build();

        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        // then
        assertThat(results).allMatch(r -> {
            if (!planName.equals(r.getName())) {
                return false;
            }
            // 부가서비스가 없을 수도 있으므로, null-safe 처리
            for (FindingAddon fa : r.getFindingAddons()) {
                Integer p = fa.getPrice();
                if (p == null || p < 0 || p > 10000) {
                    return false;
                }
            }
            return true;
        });
    }


    @Test
    void 모바일_요금제_월가격_및_부가서비스_이름_타입_검색() {
        // given
        Range monthlyRange = Range.builder()
                .baseNumber(20000)
                .subNumber(60000)
                .operator("사이")
                .build();
        List<String> addonNames = List.of("아이들나라", "휴대폰결제 (일반/정기결제)");
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CONVENIENCE);

        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .monthlyPrice(monthlyRange)
                                .build()
                )
                .addonCondition(
                        AddonCondition.builder()
                                .names(addonNames)
                                .addonTypes(addonTypes)
                                .build()
                )
                .build();

        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);

        // then
        assertThat(results).allMatch(r -> {

            Integer price = r.getMonthlyPrice();
            if (price == null || price < 20000 || price > 60000) {
                return false;
            }

            Set<String> foundNames = new HashSet<>();
            Set<AddonType> foundTypes = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {
                if (addonNames.contains(fa.getName())) {
                    foundNames.add(fa.getName());
                }
                if (addonTypes.contains(fa.getAddonType())) {
                    foundTypes.add(fa.getAddonType());
                }
            }
            return foundNames.size() == addonNames.size() && foundTypes.size() == addonTypes.size();
        });
    }

    @Test
    void 모바일_요금제_단일_부가서비스_이름_검색() {
        // given
        String singleName = "아이들나라";
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(List.of(singleName))
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r ->
                r.getFindingAddons().stream()
                        .anyMatch(fa -> singleName.equals(fa.getName()))
        );
    }

    @Test
    void 모바일_요금제_단일_부가서비스_타입_검색() {
        // given
        AddonType singleType = AddonType.MEDIA;
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .addonTypes(List.of(singleType))
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r ->
                r.getFindingAddons().stream()
                        .anyMatch(fa -> singleType.equals(fa.getAddonType()))
        );
    }

    @Test
    void 모바일_요금제_부가서비스_이름_검색_없는값() {
        // given
        String nonexistent = "존재하지않는부가서비스";
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(List.of(nonexistent))
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_월가격_AND_부가서비스_단일이름_검색() {
        // given
        Range monthlyMin = Range.builder().baseNumber(20000).operator("이상").build();
        String addonName = "아이들나라";
        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder().monthlyPrice(monthlyMin).build()
                )
                .addonCondition(
                        AddonCondition.builder().names(List.of(addonName)).build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> {

            Integer price = r.getMonthlyPrice();
            if (price == null || price < 20000) {
                return false;
            }

            return r.getFindingAddons().stream()
                    .anyMatch(fa -> addonName.equals(fa.getName()));
        });
    }

    @Test
    void 모바일_요금제_여러조건_검색_Mplan과_부가서비스_섞어서() {
        // given
        Range monthlyRange = Range.builder().baseNumber(30000).subNumber(60000).operator("사이").build();
        Range dailyMin = Range.builder().baseNumber(5000).operator("이상").build();
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(monthlyRange)
                .dailyData(dailyMin)
                .dataType(DataType._5G)
                .build();
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CALL);
        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(
                        AddonCondition.builder()
                                .addonTypes(addonTypes)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> {

            Integer price = r.getMonthlyPrice();
            if (price == null || price < 30000 || price > 60000) {
                return false;
            }

            Integer daily = r.getDailyData();
            if (daily == null || daily < 5000) {
                return false;
            }

            if (!DataType._5G.equals(r.getDataType())) {
                return false;
            }

            Set<AddonType> found = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {
                if (addonTypes.contains(fa.getAddonType())) {
                    found.add(fa.getAddonType());
                }
            }
            return found.size() == addonTypes.size();
        });
    }

    @Test
    void 모바일_요금제_부가서비스_가격범위_AND_이름_검색() {
        // given
        Range addonPriceRange = Range.builder().baseNumber(0).subNumber(5000).operator("사이").build();
        List<String> addonNames = List.of("아이들나라", "발신번호표시제한");
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(addonNames)
                                .price(addonPriceRange)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> {
            Set<String> foundNames = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {

                if (addonNames.contains(fa.getName())) {

                    Integer p = fa.getPrice();
                    if (p == null || p < 0 || p > 5000) {
                        return false;
                    }
                    foundNames.add(fa.getName());
                }
            }
            return foundNames.size() == addonNames.size();
        });
    }

    @Test
    void 모바일_요금제_데이터타입_AND_부가서비스_가격범위_검색() {
        // given
        MplanCondition mcond = MplanCondition.builder().dataType(DataType._5G).build();
        Range addonPriceMax = Range.builder().baseNumber(2000).operator("이하").build();
        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(
                        AddonCondition.builder()
                                .price(addonPriceMax)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> {

            if (!DataType._5G.equals(r.getDataType())) {
                return false;
            }

            for (FindingAddon fa : r.getFindingAddons()) {
                Integer p = fa.getPrice();
                if (p == null || p > 2000) {
                    return false;
                }
            }
            return true;
        });
    }

    @Test
    void 모바일_요금제_없는_조합_검색() {
        // given
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(Range.builder().baseNumber(0).operator("미만").build())
                .build();
        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(
                        AddonCondition.builder()
                                .names(List.of("존재하지않음"))
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_둘다_조건없음_검색() {
        // given
        Condition condition = Condition.builder().build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_Mplan이름_없는값_검색() {
        // given
        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .name("존재하지않는요금제")
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_textMessage_false_검색() {
        // given
        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .textMessage(false)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> Boolean.FALSE.equals(r.getTextMessage()));
    }

    @Test
    void 모바일_요금제_여러_Mplan조건_검색() {
        // given
        Range monthlyMax = Range.builder().baseNumber(40000).operator("이하").build();
        Range dailyMin = Range.builder().baseNumber(2000).operator("이상").build();
        Range sharingMin = Range.builder().baseNumber(10000).operator("이상").build();
        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .monthlyPrice(monthlyMax)
                                .dailyData(dailyMin)
                                .sharingData(sharingMin)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> {
            Integer price = r.getMonthlyPrice();
            Integer daily = r.getDailyData();
            Integer sharing = r.getSharingData();
            if (price == null || price > 40000) return false;
            if (daily == null || daily < 2000) return false;
            if (sharing == null || sharing < 10000) return false;
            return true;
        });
    }

    @Test
    void 모바일_요금제_부가서비스_가격범위_상한만_검색() {
        // given
        Range priceMax = Range.builder().baseNumber(3000).operator("미만").build();
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .price(priceMax)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r ->
                r.getFindingAddons().stream()
                        .allMatch(fa -> fa.getPrice() != null && fa.getPrice() < 3000)
        );
    }

    @Test
    void 모바일_요금제_Mplan조건과_부가서비스_없는조합_검색() {
        // given
        Range monthlyMax = Range.builder().baseNumber(10000).operator("미만").build();
        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder()
                                .monthlyPrice(monthlyMax)
                                .build()
                )
                .addonCondition(
                        AddonCondition.builder()
                                .names(List.of("존재하지않음"))
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_qualification_NULL처리_검색() {
        // given
        Condition condition = Condition.builder()
                .mplanCondition(
                        MplanCondition.builder().build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isNotNull();
    }

    @Test
    void 모바일_요금제_부가서비스_빈리스트_이름_검색() {
        // given
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(List.of())  // 빈 리스트
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_부가서비스_빈리스트_타입_검색() {
        // given
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .addonTypes(List.of())  // 빈 리스트
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).isEmpty();
    }

    @Test
    void 모바일_요금제_부가서비스_이름과_price조건_혼합검색() {
        // given
        String addonName = "아이들나라";
        Range priceMin = Range.builder().baseNumber(1000).operator("이상").build();
        Condition condition = Condition.builder()
                .addonCondition(
                        AddonCondition.builder()
                                .names(List.of(addonName))
                                .price(priceMin)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r ->
                r.getFindingAddons().stream()
                        .anyMatch(fa -> addonName.equals(fa.getName()) && fa.getPrice() != null && fa.getPrice() >= 1000)
        );
    }

    @Test
    void 모바일_요금제_Mplan월가격_타입과_부가서비스_조건_혼합검색() {
        // given
        Range monthlyMin = Range.builder().baseNumber(30000).operator("이상").build();
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(monthlyMin)
                .mplanType(MplanType.LTE_5G)
                .build();
        List<AddonType> addonTypes = List.of(AddonType.CONVENIENCE);
        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(
                        AddonCondition.builder()
                                .addonTypes(addonTypes)
                                .build()
                )
                .build();
        // when
        List<FindingMplan> results = combinedSearchRepository.search(condition);
        // then
        assertThat(results).allMatch(r -> {
            Integer price = r.getMonthlyPrice();
            if (price == null || price < 30000) return false;
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            return r.getFindingAddons().stream()
                    .anyMatch(fa -> AddonType.CONVENIENCE.equals(fa.getAddonType()));
        });
    }

    /**
     * 월가격_일일데이터_공유데이터_데이터타입_부가서비스이름혼합검색
     *   - monthlyPrice: 30000 이상 70000 이하
     *   - dailyData: 2000 이상
     *   - sharingData: 5000 이상
     *   - dataType: 5G
     *   - addonNames: ["아이들나라", "발신번호표시제한"]
     */
    @Test
    void 월가격_일일데이터_공유데이터_데이터타입_부가서비스이름혼합검색() {
        Range monthlyRange = Range.builder().baseNumber(30000).subNumber(70000).operator("사이").build();
        Range dailyMin = Range.builder().baseNumber(2000).operator("이상").build();
        Range sharingMin = Range.builder().baseNumber(5000).operator("이상").build();
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(monthlyRange)
                .dailyData(dailyMin)
                .sharingData(sharingMin)
                .dataType(DataType._5G)
                .build();
        List<String> addonNames = List.of("아이들나라", "발신번호표시제한");

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 30000 || mp > 70000) return false;
            Integer dd = r.getDailyData();
            if (dd == null || dd < 2000) return false;
            Integer sd = r.getSharingData();
            if (sd == null || sd < 5000) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;

            Set<String> found = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {
                if (addonNames.contains(fa.getName())) {
                    found.add(fa.getName());
                }
            }
            return found.size() == addonNames.size();
        });
    }

    /**
     * 월가격_기본데이터_텍스트메시지_부가서비스타입가격혼합검색
     *   - monthlyPrice: 25000 이상
     *   - basicDataAmount: 10000 이상
     *   - textMessage: true
     *   - addonTypes: [MEDIA, CONVENIENCE]
     *   - addonPrice: 500 이상 5000 이하
     */
    @Test
    void 월가격_기본데이터_텍스트메시지_부가서비스타입가격혼합검색() {
        Range monthlyMin = Range.builder().baseNumber(25000).operator("이상").build();
        Range basicMin = Range.builder().baseNumber(10000).operator("이상").build();
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(monthlyMin)
                .basicDataAmount(basicMin)
                .textMessage(true)
                .build();
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CONVENIENCE);
        Range addonPriceRange = Range.builder().baseNumber(500).subNumber(5000).operator("사이").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .addonTypes(addonTypes)
                        .price(addonPriceRange)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 25000) return false;
            Integer bd = r.getBasicDataAmount();
            if (bd == null || bd < 10000) return false;
            if (!Boolean.TRUE.equals(r.getTextMessage())) return false;

            return r.getFindingAddons().stream().anyMatch(fa -> {
                boolean typeOk = addonTypes.contains(fa.getAddonType());
                Integer p = fa.getPrice();
                boolean priceOk = (p != null && p >= 500 && p <= 5000);
                return typeOk && priceOk;
            });
        });
    }

    /**
     * MplanType_자격조건_부가서비스이름타입혼합검색
     *   - mplanType: LTE_5G
     *   - qualification: ALL
     *   - addonNames: ["휴대폰결제 (일반/정기결제)"]
     *   - addonTypes: [CALL]
     */
    @Test
    void MplanType_자격조건_부가서비스이름타입혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .mplanType(MplanType.LTE_5G)
                .qualification(Qualification.ALL) // init.sql과 일치하도록
                .build();
        List<String> addonNames = List.of("휴대폰결제 (일반/정기결제)");
        List<AddonType> addonTypes = List.of(AddonType.CALL);

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .addonTypes(addonTypes)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            if (!Qualification.ALL.equals(r.getQualification())) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> {
                boolean nameOk = addonNames.contains(fa.getName());
                boolean typeOk = addonTypes.contains(fa.getAddonType());
                return nameOk && typeOk;
            });
        });
    }

    /**
     * dataType_MplanType_월가격범위_부가서비스이름가격혼합검색
     *   - dataType: 5G
     *   - mplanType: LTE_5G
     *   - monthlyPrice: 20000 이상 60000 이하
     *   - addonNames: ["아이들나라"]
     *   - addonPrice: 1000 이상 3000 이하
     */
    @Test
    void dataType_MplanType_월가격범위_부가서비스이름가격혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .dataType(DataType._5G)
                .mplanType(MplanType.LTE_5G)
                .monthlyPrice(Range.builder().baseNumber(20000).subNumber(60000).operator("사이").build())
                .build();
        List<String> addonNames = List.of("아이들나라");
        Range addonPriceRange = Range.builder().baseNumber(1000).subNumber(3000).operator("사이").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .price(addonPriceRange)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            if (!DataType._5G.equals(r.getDataType())) return false;
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 20000 || mp > 60000) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> {
                boolean nameOk = addonNames.contains(fa.getName());
                Integer p = fa.getPrice();
                boolean priceOk = (p != null && p >= 1000 && p <= 3000);
                return nameOk && priceOk;
            });
        });
    }

    /**
     * 월가격_데이터타입_기본데이터_공유데이터_부가서비스타입혼합검색
     *   - monthlyPrice: 40000 이상
     *   - dataType: 5G
     *   - basicDataAmount: 5000 이상
     *   - sharingData: 2000 이상
     *   - addonTypes: [MEDIA]
     */
    @Test
    void 월가격_데이터타입_기본데이터_공유데이터_부가서비스타입혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(Range.builder().baseNumber(40000).operator("이상").build())
                .dataType(DataType._5G)
                .basicDataAmount(Range.builder().baseNumber(5000).operator("이상").build())
                .sharingData(Range.builder().baseNumber(2000).operator("이상").build())
                .build();
        List<AddonType> addonTypes = List.of(AddonType.MEDIA);

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .addonTypes(addonTypes)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 40000) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;
            Integer bd = r.getBasicDataAmount();
            if (bd == null || bd < 5000) return false;
            Integer sd = r.getSharingData();
            if (sd == null || sd < 2000) return false;

            return r.getFindingAddons().stream().anyMatch(fa -> addonTypes.contains(fa.getAddonType()));
        });
    }

    /**
     * dailyData_qualification_textMessage_부가서비스가격혼합검색
     *   - dailyData: 3000 이상
     *   - qualification: ALL
     *   - textMessage: false
     *   - addonPrice: 0 이상 2000 이하
     */
    @Test
    void dailyData_qualification_textMessage_부가서비스가격혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .dailyData(Range.builder().baseNumber(3000).operator("이상").build())
                .qualification(Qualification.ALL)
                .textMessage(false)
                .build();
        Range addonPriceRange = Range.builder().baseNumber(0).subNumber(2000).operator("사이").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .price(addonPriceRange)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer dd = r.getDailyData();
            if (dd == null || dd < 3000) return false;
            if (!Qualification.ALL.equals(r.getQualification())) return false;
            if (!Boolean.FALSE.equals(r.getTextMessage())) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> {
                Integer p = fa.getPrice();
                return p != null && p >= 0 && p <= 2000;
            });
        });
    }

    /**
     * 여러조건_혼합검색_종합테스트
     *   - monthlyPrice: 20000 이상 50000 이하
     *   - dailyData: 1000 이상 4000 이하
     *   - basicDataAmount: 5000 이상
     *   - dataType: 5G
     *   - mplanType: LTE_5G
     *   - qualification: ALL
     *   - textMessage: true
     *   - addonNames: ["아이들나라", "휴대폰결제 (일반/정기결제)"]
     *   - addonTypes: [MEDIA, CALL]
     *   - addonPrice: 500 이상 5000 이하
     */
    @Test
    void 여러조건_혼합검색_종합테스트() {
        Range monthlyRange = Range.builder().baseNumber(20000).subNumber(50000).operator("사이").build();
        Range dailyRange = Range.builder().baseNumber(1000).subNumber(4000).operator("사이").build();
        Range basicMin = Range.builder().baseNumber(5000).operator("이상").build();
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(monthlyRange)
                .dailyData(dailyRange)
                .basicDataAmount(basicMin)
                .dataType(DataType._5G)
                .mplanType(MplanType.LTE_5G)
                .qualification(Qualification.ALL)
                .textMessage(true)
                .build();
        List<String> addonNames = List.of("아이들나라", "휴대폰결제 (일반/정기결제)");
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CALL);
        Range addonPriceRange = Range.builder().baseNumber(500).subNumber(5000).operator("사이").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .addonTypes(addonTypes)
                        .price(addonPriceRange)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 20000 || mp > 50000) return false;
            Integer dd = r.getDailyData();
            if (dd == null || dd < 1000 || dd > 4000) return false;
            Integer bd = r.getBasicDataAmount();
            if (bd == null || bd < 5000) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            if (!Qualification.ALL.equals(r.getQualification())) return false;
            if (!Boolean.TRUE.equals(r.getTextMessage())) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> {
                boolean nameOk = addonNames.contains(fa.getName());
                boolean typeOk = addonTypes.contains(fa.getAddonType());
                Integer p = fa.getPrice();
                boolean priceOk = (p != null && p >= 500 && p <= 5000);
                return nameOk && typeOk && priceOk;
            });
        });
    }

    /**
     * 월가격_기본데이터_일일데이터_서브데이터속도혼합검색
     *   - monthlyPrice: 15000 이상 45000 이하
     *   - basicDataAmount: 5000 이상 20000 이하
     *   - dailyData: 1000 이상 3000 이하
     *   - subDataSpeed: 2000 이상 8000 이하
     */
    @Test
    void 월가격_기본데이터_일일데이터_서브데이터속도혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(Range.builder().baseNumber(15000).subNumber(45000).operator("사이").build())
                .basicDataAmount(Range.builder().baseNumber(5000).subNumber(20000).operator("사이").build())
                .dailyData(Range.builder().baseNumber(1000).subNumber(3000).operator("사이").build())
                .subDataSpeed(Range.builder().baseNumber(2000).subNumber(8000).operator("사이").build())
                .build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 15000 || mp > 45000) return false;
            Integer bd = r.getBasicDataAmount();
            if (bd == null || bd < 5000 || bd > 20000) return false;
            Integer dd = r.getDailyData();
            if (dd == null || dd < 1000 || dd > 3000) return false;
            Integer sds = r.getSubDataSpeed();
            if (sds == null || sds < 2000 || sds > 8000) return false;
            return true;
        });
    }

    /**
     * 데이터타입_LTE조합_일일데이터상한_부가서비스타입혼합검색
     *   - dataType: _5G (또는 init.sql에 있는 적절한 값)
     *   - mplanType: LTE_5G (init.sql에 해당 값이 있으면)
     *   - dailyData: <= 2000
     *   - addonTypes: [CALL]
     */
    @Test
    void 데이터타입_LTE_일일데이터상한_부가서비스타입혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .dataType(DataType._5G)  // _4G 값이 init 데이터에 있어야 유효
                .mplanType(MplanType.LTE_5G)  // LTE 값이 enum에 있고 데이터에 있어야 함
                .dailyData(Range.builder().baseNumber(2000).operator("이하").build())
                .build();
        List<AddonType> addonTypes = List.of(AddonType.CALL);

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .addonTypes(addonTypes)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            if (!DataType._5G.equals(r.getDataType())) return false;
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            Integer dd = r.getDailyData();
            if (dd == null || dd > 2000) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> AddonType.CALL.equals(fa.getAddonType()));
        });
    }

    /**
     * qualification_비ALL_월가격상한_부가서비스가격하한혼합검색
     *   - qualification: ALL이 아닌 다른 enum 값, 예: Qualification.SOME (init.sql에 맞게 수정)
     *   - monthlyPrice: <= 30000
     *   - addonPrice: >= 1000
     */
    @Test
    void qualification_비ALL_월가격상한_부가서비스가격하한혼합검색() {
        // init.sql에 맞춰 ALL 외 다른 qualification 값이 있어야 의미 있음
        Qualification targetQual = Qualification.BOY; // 실제 enum 값으로 교체
        MplanCondition mcond = MplanCondition.builder()
                .qualification(targetQual)
                .monthlyPrice(Range.builder().baseNumber(30000).operator("이하").build())
                .build();
        Range addonPriceMin = Range.builder().baseNumber(1000).operator("이상").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .price(addonPriceMin)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            if (!targetQual.equals(r.getQualification())) return false;
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp > 30000) return false;
            // addon 중 가격 >=1000인 것이 적어도 하나 있어야 함
            return r.getFindingAddons().stream().anyMatch(fa -> {
                Integer p = fa.getPrice();
                return p != null && p >= 1000;
            });
        });
    }

    /**
     * textMessage_false_데이터타입_부가서비스이름혼합검색
     *   - textMessage: false
     *   - dataType: _5G
     *   - addonNames: ["발신번호표시제한"]
     */
    @Test
    void textMessage_false_데이터타입_부가서비스이름혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .textMessage(false)
                .dataType(DataType._5G)
                .build();
        List<String> addonNames = List.of("발신번호표시제한");

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            if (!Boolean.FALSE.equals(r.getTextMessage())) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> addonNames.contains(fa.getName()));
        });
    }

    /**
     * 여러부가서비스조건_이름타입가격동시검증혼합검색
     *   - addonNames: ["아이들나라", "휴대폰결제 (일반/정기결제)"]
     *   - addonTypes: [MEDIA, CALL]
     *   - addonPrice: 2000 이상 8000 이하
     *   - MplanCondition: monthlyPrice >= 20000, dataType=_5G
     */
    @Test
    void 여러부가서비스조건_이름타입가격동시검증혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(Range.builder().baseNumber(20000).operator("이상").build())
                .dataType(DataType._5G)
                .build();
        List<String> addonNames = List.of("아이들나라", "휴대폰결제 (일반/정기결제)");
        List<AddonType> addonTypes = List.of(AddonType.MEDIA, AddonType.CALL);
        Range addonPriceRange = Range.builder().baseNumber(2000).subNumber(8000).operator("사이").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .addonTypes(addonTypes)
                        .price(addonPriceRange)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 20000) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> {
                boolean nameOk = addonNames.contains(fa.getName());
                boolean typeOk = addonTypes.contains(fa.getAddonType());
                Integer p = fa.getPrice();
                boolean priceOk = (p != null && p >= 2000 && p <= 8000);
                return nameOk && typeOk && priceOk;
            });
        });
    }

    /**
     * sharingData_상한기준_MplanType혼합검색
     *   - sharingData: <= 10000
     *   - mplanType: LTE_5G
     *   - addonTypes: [CONVENIENCE, MEDIA]
     */
    @Test
    void sharingData_상한기준_MplanType혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .sharingData(Range.builder().baseNumber(10000).operator("이하").build())
                .mplanType(MplanType.LTE_5G)
                .build();
        List<AddonType> addonTypes = List.of(AddonType.CONVENIENCE, AddonType.MEDIA);

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .addonTypes(addonTypes)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer sd = r.getSharingData();
            if (sd == null || sd > 10000) return false;
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            // 지정 타입의 addon이 모두 포함되는지: 적어도 각각 하나 존재해야 함
            Set<AddonType> found = new HashSet<>();
            for (FindingAddon fa : r.getFindingAddons()) {
                if (addonTypes.contains(fa.getAddonType())) {
                    found.add(fa.getAddonType());
                }
            }
            return found.containsAll(addonTypes);
        });
    }


    /**
     * 월가격범위_qualification_dataType_부가서비스조건혼합검색
     *   - monthlyPrice: 10000 이상 40000 이하
     *   - qualification: ALL
     *   - dataType: _5G
     *   - addonPrice: <= 5000
     */
    @Test
    void 월가격범위_qualification_dataType_부가서비스가격상한혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .monthlyPrice(Range.builder().baseNumber(10000).subNumber(40000).operator("사이").build())
                .qualification(Qualification.ALL)
                .dataType(DataType._5G)
                .build();
        Range addonPriceMax = Range.builder().baseNumber(5000).operator("이하").build();

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .price(addonPriceMax)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer mp = r.getMonthlyPrice();
            if (mp == null || mp < 10000 || mp > 40000) return false;
            if (!Qualification.ALL.equals(r.getQualification())) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;
            return r.getFindingAddons().stream().allMatch(fa -> {
                Integer p = fa.getPrice();
                return p != null && p <= 5000;
            });
        });
    }

    /**
     * dailyData_range_MplanType_dataType_부가서비스이름혼합검색
     *   - dailyData: 500 이상 2500 이하
     *   - mplanType: LTE_5G
     *   - dataType: _5G
     *   - addonNames: ["아이들나라"]
     */
    @Test
    void dailyData_range_MplanType_dataType_부가서비스이름혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .dailyData(Range.builder().baseNumber(500).subNumber(2500).operator("사이").build())
                .mplanType(MplanType.LTE_5G)
                .dataType(DataType._5G)
                .build();
        List<String> addonNames = List.of("아이들나라");

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .names(addonNames)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer dd = r.getDailyData();
            if (dd == null || dd < 500 || dd > 2500) return false;
            if (!MplanType.LTE_5G.equals(r.getMplanType())) return false;
            if (!DataType._5G.equals(r.getDataType())) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> addonNames.contains(fa.getName()));
        });
    }

    /**
     * basicDataAmount_upperAnd_subDataSpeed_lower혼합검색
     *   - basicDataAmount: <= 15000
     *   - subDataSpeed: >= 3000
     *   - addonTypes: [CONVENIENCE]
     */
    @Test
    void basicDataAmount_upperAnd_subDataSpeed_lower혼합검색() {
        MplanCondition mcond = MplanCondition.builder()
                .basicDataAmount(Range.builder().baseNumber(15000).operator("이하").build())
                .subDataSpeed(Range.builder().baseNumber(3000).operator("이상").build())
                .build();
        List<AddonType> addonTypes = List.of(AddonType.CONVENIENCE);

        Condition condition = Condition.builder()
                .mplanCondition(mcond)
                .addonCondition(AddonCondition.builder()
                        .addonTypes(addonTypes)
                        .build())
                .build();

        List<FindingMplan> results = combinedSearchRepository.search(condition);

        assertThat(results).allMatch(r -> {
            Integer bd = r.getBasicDataAmount();
            if (bd == null || bd > 15000) return false;
            Integer sds = r.getSubDataSpeed();
            if (sds == null || sds < 3000) return false;
            return r.getFindingAddons().stream().anyMatch(fa -> addonTypes.contains(fa.getAddonType()));
        });
    }
}