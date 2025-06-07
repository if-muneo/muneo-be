package ureca.muneobe.entity.addon;

public enum AddonType {
    MEDIA("넷플릭스, 유플레이 등등 같은 미디어"),
    CONVENIENCE("편의 : 데이터, 간편결제, 부가 옵션 약간, 부가서비스 느낌나는 것들"),
    DEVICE("기기 할부 할인"),
    SAFE("안심 서비스들 : 해킹, 휴대폰 분실 등"),
    SALE("모바일 요금제와 결합 시 할인 적용되는 서비스"),
    CALL("통화 관련 부가 서비스");

    private final String description;

    AddonType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
