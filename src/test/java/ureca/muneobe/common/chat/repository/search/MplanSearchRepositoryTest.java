//package ureca.muneobe.common.chat.repository.search;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ureca.muneobe.common.chat.entity.DataType;
//import ureca.muneobe.common.chat.entity.MplanType;
//import ureca.muneobe.common.chat.entity.Qualification;
//import ureca.muneobe.common.chat.service.rdb.input.Condition;
//import ureca.muneobe.common.chat.service.rdb.input.MplanCondition;
//import ureca.muneobe.common.chat.service.rdb.input.Range;
//import ureca.muneobe.common.chat.service.rdb.output.FindingMplan;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Testcontainers
//class MplanSearchRepositoryTest {
//
//    @Container
//    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:15")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass")
//            .withInitScript("init.sql");
//
//    @DynamicPropertySource
//    static void overrideProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url",    postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
//    }
//
//    @Autowired
//    private MplanSearchRepository mplanSearchRepository;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Test
//    void checkInit() {
//        System.out.println("mplan_detail: " + jdbcTemplate.queryForList("SELECT * FROM mplan_detail"));
//        System.out.println("mplan: " + jdbcTemplate.queryForList("SELECT * FROM mplan"));
//        System.out.println("addon_group: " + jdbcTemplate.queryForList("SELECT * FROM addon_group"));
//        System.out.println("addon: " + jdbcTemplate.queryForList("SELECT * FROM addon"));
//    }
//
//    @Test
//    void 모바일_요금제_이름_검색() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .name("5G 프리미어 에센셜")
//                .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        results.forEach(System.out::println);
//    }
//
//    @Test
//    void 모바일_요금제_월가격_검색_5만원_이상() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .monthlyPrice(Range.builder().baseNumber(50000).operator("이상").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getMonthlyPrice() >= 50000);
//    }
//
//    @Test
//    void 모바일_요금제_월가격_검색_4만원_이하() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .monthlyPrice(Range.builder().baseNumber(40000).operator("이하").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//
//        assertThat(results)
//                .allMatch(r -> r.getMonthlyPrice() <= 40000);
//    }
//
//    @Test
//    void 모바일_요금제_월가격_검색_3만원_이상_5만원_이하() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .monthlyPrice(Range.builder().baseNumber(30000).subNumber(50000).operator("사이").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//
//        assertThat(results)
//                .allMatch(r -> r.getMonthlyPrice() <= 40000);
//    }
//
//    @Test
//    void 모바일_요금제_월가격_검색_5만원_초과() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .monthlyPrice(Range.builder().baseNumber(50000).operator("초과").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getMonthlyPrice() > 50000);
//    }
//
//    @Test
//    void 모바일_요금제_monthly_price_검색_5만원_미만() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .monthlyPrice(Range.builder().baseNumber(50000).operator("미만").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getMonthlyPrice() < 50000);
//    }
//
//    @Test
//    void 모바일_요금제_basic_data_amount_검색_5만원_이상() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .monthlyPrice(Range.builder().baseNumber(50000).operator("이상").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getBasicDataAmount() >= 50000);
//    }
//
//    @Test
//    void 모바일_요금제_daily_date_검색_5천원_이상() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .dailyData(Range.builder().baseNumber(5000).operator("이상").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getDailyData() >= 5000);
//    }
//
//    @Test
//    void 모바일_요금제_sharing_data_검색_5만원_이상() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .sharingData(Range.builder().baseNumber(50000).operator("이상").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getSharingData() >= 5000);
//    }
//
//    @Test
//    void 모바일_요금제_voice_call_volume_검색_0_이상() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .dailyData(Range.builder().baseNumber(0).operator("이상").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getVoiceCallVolume() >= 0);
//    }
//
//    @Test
//    void 모바일_요금제_text_message_검색_true() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .textMessage(true)
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getTextMessage().equals(true));
//    }
//
//    @Test
//    void 모바일_요금제_sub_data_speed_5000이상() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .subDataSpeed(Range.builder().baseNumber(5000).operator("이상").build())
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getSubDataSpeed() >= 5000);
//    }
//
//    @Test
//    void 모바일_요금제_qualification_ALL() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .qualification(Qualification.ALL)
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getQualification().equals(Qualification.ALL));
//    }
//
//    @Test
//    void 모바일_요금제_dataType_5G() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .dataType(DataType._5G)
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getDataType().equals(DataType._5G));
//    }
//
//    @Test
//    void 모바일_요금제_mplanType_LTE_5G() {
//        Condition condition = Condition.builder().mplanCondition(
//                MplanCondition.builder()
//                        .mplanType(MplanType.LTE_5G)
//                        .build()).build();
//
//        List<FindingMplan> results = mplanSearchRepository.search(condition);
//        assertThat(results)
//                .allMatch(r -> r.getMplanType().equals(MplanType.LTE_5G));
//    }
//}