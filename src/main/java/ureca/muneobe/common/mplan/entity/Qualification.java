package ureca.muneobe.common.mplan.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Qualification {
    ALL("모든 사용자"),
    YOUTH("청년 : 19~34"),
    OLD("노년 : 65~"),
    WELFARE("복지 : 복지카드 있는 사람들"),
    BOY("힘을 내라 소년 : 4~18"),
    SOLDIER("군인 : 호국장병"),
    KID("애기들 : ~4");

    private final String description;
}
