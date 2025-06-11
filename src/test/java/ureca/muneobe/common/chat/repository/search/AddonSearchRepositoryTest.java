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
import ureca.muneobe.common.chat.service.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.rdb.input.Condition;
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
class AddonSearchRepositoryTest {

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
    private AddonSearchRepository addonSearchRepository;

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
        List<FindingMplan> results = addonSearchRepository.search(condition);

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
        List<FindingMplan> results = addonSearchRepository.search(condition);
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
        List<FindingMplan> results = addonSearchRepository.search(condition);

        int totalCnt = 0;

        for(FindingMplan result: results) {
            System.out.println(result);
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
        List<FindingMplan> results = addonSearchRepository.search(condition);

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
}