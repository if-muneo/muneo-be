package ureca.muneobe.entity.user;

public enum Job {
    YOUTH("청년 : 19~34"),
    OLD("노년 : 65~"),
    WELFARE("복지 : 복지카드 있는 사람들"),
    BOY("힘을 내라 소년 : 4~18"),
    SOLDIER("군인 : 호국장병"),
    KID("애기들 : ~4"),
    OFFICE_WORKER("직장인"),
    UNIVERSITY_STUDENT("대학생"),
    JOBLESS("무직");

    private final String description;

    Job(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
