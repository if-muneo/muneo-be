package ureca.muneobe.slang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ureca.muneobe.slang.service.ModerationApiSlangFilterService;

@SpringBootTest
public class ModerationApiSlangFilterServiceTest {

    @Autowired
    ModerationApiSlangFilterService filterService;

    @ParameterizedTest
    @CsvSource({
            // 유해한 content
            "'씨발', false",
            "'죽고 싶어', false",
            "'개새끼야', false",
            "'너를 찔러버릴 거야', false",
            "'나는 자살을 생각 중이야', false",
            "'젠장, 이 멍청한 놈아', false",
            "'다 불태워버릴 거야', false",
            "'칼로 손목을 그었어', false",
            "'미친놈들 다 죽어버렸으면', false",
            "'이런 미친년 같으니라고', false",

            // 유해하지 않은 content
            "'오늘 날씨 정말 좋네요.', true",
            "'친구랑 저녁 먹으러 갔어요.', true",
            "'요즘 책 읽는 재미에 빠졌어요.', true",
            "'운동을 시작했더니 기분이 좋아요.', true",
            "'고양이가 너무 귀여워요.', true",
            "'이 프로젝트가 정말 흥미로워요.', true",
            "'조용한 카페에서 공부했어요.', true",
            "'산책하면서 스트레스를 풀었어요.', true",
            "'저는 여행을 정말 좋아해요.', true",
            "'좋은 하루 되세요!', true"
    })
    @DisplayName("문장이 유해한지 여부를 판단합니다")
    void contentModerationTest(String input, boolean expected) {
        boolean isSafe = filterService.isSafeContent(input);
        Assertions.assertEquals(expected, isSafe);
    }
}
