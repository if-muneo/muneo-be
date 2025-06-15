package ureca.muneobe.common.chat.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MplanType {

    LTE_5G("5G_LTE 모바일 요금제"),
    NUGGET("USIM/ESIM 전용 모바일 요금제"),
    SMART_DEVICE("스마트기기/테블릿 전용 모바일 요금제");

    private final String description;
}
