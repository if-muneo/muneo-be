package ureca.muneobe.entity.mplanDetail;

public enum Qualification {
    ALL("모든 사용자"),
    YOUTH("청년 : 19~34"),
    OLD("노년 : 65~"),
    WELFARE("복지 : 복지카드 있는 사람들"),
    BOY("힘을 내라 소년 : 4~18"),
    SOLDIER("군인 : 호국장병"),
    KID("애기들 : ~4");

    private final String description;

    Qualification(String description) {
        this.description = description;
    }

    public String description() { return description; }
}
