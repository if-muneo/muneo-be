-- 1) 일반 사용자(USER) 계정들: job Enum별로 한 명씩, created_at을 직접 지정
INSERT INTO "user"
(email, password, phoneNumber, old, gender, name, category, created_at, deleted_at, active_yn, role)
VALUES
    -- 1) 청년 (YOUTH: 19~34)
    ('김민수@naver.com', abc123, '01088888888', 25, 'M', '김민수', 'YOUTH', '2025-06-04 09:00:00'::TIMESTAMP, NULL, TRUE,
     'USER'),
    -- 2) 노년 (OLD: 65~)
    ('홍정기@naver.com', abc123, '01077777777', 70, 'M', '홍정기', 'OLD', '2025-06-04 09:30:00'::TIMESTAMP, NULL, TRUE,
     'USER'),
    -- 3) 복지 (WELFARE: 복지카드 있는 사람)
    ('송민규@naver.com', abc123, '01066666666', 50, 'F', '송민규', 'WELFARE', '2025-06-04 10:00:00'::TIMESTAMP, NULL,
     TRUE, 'USER'),
    -- 4) 소년 (BOY: 4~18)
    ('김도연@naver.com', abc123, '01055555555', 10, 'F', '김도연', 'BOY', '2025-06-04 10:30:00'::TIMESTAMP, NULL, TRUE,
     'USER'),
    -- 5) 군인 (SOLDIER)
    ('박상윤@naver.com', abc123, '01044444444', 22, 'M', '박상윤', 'SOLDIER', '2025-06-04 11:00:00'::TIMESTAMP, NULL,
     TRUE, 'USER'),
    -- 6) 애기 (KID: ~4)
    ('정지호@naver.com', abc123, '01033333333', 3, 'M', '정지호', 'KID', '2025-06-04 11:30:00'::TIMESTAMP, NULL, TRUE,
     'USER'),
    -- 7) 직장인 (OFFICE_WORKER)
    ('정민경@naver.com', abc123, '01022222222', 30, 'F', '정민경', 'OFFICE_WORKER', '2025-06-04 12:00:00'::TIMESTAMP,
     NULL, TRUE, 'USER'),
    -- 8) 대학생 (UNIVERSITY_STUDENT)
    ('노재호@naver.com', abc123, '01011111111', 21, 'M', '노재호', 'UNIVERSITY_STUDENT', '2025-06-04 13:00:00'::TIMESTAMP,
     NULL, TRUE, 'USER'),
    -- 9) 무직 (JOBLESS)
    ('김무직@naver.com', abc123, '01000000000', 40, 'F', '김무직', 'JOBLESS', '2025-06-04 14:00:00'::TIMESTAMP, NULL,
     TRUE, 'USER');

-- 2) 관리자(ADMIN) 계정 1명, created_at을 별도 지정
INSERT INTO "user"
(email, password, phoneNumber, old, gender, name, category, created_at, deleted_at, active_yn, role)
VALUES ('최정민@naver.com', abc123, '01099999999', 35, 'M', '최정민', 'OFFICE_WORKER', '2025-06-04 15:00:00'::TIMESTAMP,
        NULL, TRUE, 'ADMIN');