package ureca.muneobe.common.combined;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CombinedType {
    TOGETHER("투게터"),
    FAMILY_EASY("참 쉬운 가족 결합"),
    FAMILY_INFINITY("가족 무한 사랑");

    private final String description;
}
