-- mplan_detail 테이블
INSERT INTO mplan_detail
(basic_data_amount, daily_data, sharing_data, monthly_price, voice_call_volume, text_message, sub_data_speed, qualification, data_type, mplan_type)
VALUES
    (10000000, NULL, 70000, 85000, 0, TRUE, NULL, 'ALL',   '_5G', 'LTE_5G'),
    (150000,   NULL, 60000, 75000, 0, TRUE, 5000, 'ALL',   '_5G', 'LTE_5G'),
    (31000,    NULL, NULL,  61000, 0, TRUE, 1000, 'ALL',   '_5G', 'LTE_5G'),
    (10000000, NULL, 80000, 95000, 0, TRUE, NULL, 'ALL',   '_5G', 'LTE_5G'),
    (210000,   NULL, 65000, 75000, 0, TRUE, 5000, 'YOUTH', '_5G', 'LTE_5G'),
    (50000,    NULL, 40000, 63000, 0, TRUE, 1000, 'ALL',   '_5G', 'LTE_5G'),
    (80000,    NULL, 45000, 66000, 0, TRUE, 1000, 'ALL',   '_5G', 'LTE_5G'),
    (14000,    NULL, NULL,  55000, 0, TRUE, 1000, 'ALL',   '_5G', 'LTE_5G'),
    (110000,   NULL, 50000, 66000, 0, TRUE, 1000, 'YOUTH', '_5G', 'LTE_5G'),
    (5000,     NULL, NULL,  37000, 0, TRUE, 400,  'ALL',   '_5G', 'LTE_5G'),
    (26000,    NULL, NULL,  55000, 0, TRUE, 1000, 'YOUTH', '_5G', 'LTE_5G'),
    (9000,     NULL, NULL,  47000, 0, TRUE, 400,  'ALL',   '_5G', 'LTE_5G'),
    (10000000, NULL, 100000,105000,0, TRUE, NULL, 'ALL',   '_5G', 'LTE_5G'),
    (10000,    NULL, NULL,  43000, 0, TRUE, 1000, 'OLD',   '_5G', 'LTE_5G'),
    (1700,     NULL, NULL,  33000, 0, TRUE, NULL, 'OLD',   '_5G', 'LTE_5G'),
    (15000,    NULL, NULL,  47000, 0, TRUE, 400,  'YOUTH', '_5G', 'LTE_5G'),
    (10000,    NULL, NULL,  43000, 0, TRUE, 1000, 'OLD',   '_5G', 'LTE_5G'),
    (70000,    NULL, 45000, 63000, 0, TRUE, 1000, 'YOUTH', '_5G', 'LTE_5G'),
    (1500,     NULL, NULL,  33000, 0, TRUE, NULL, 'ALL',   '_5G', 'LTE_5G'),
    (9000,     NULL, NULL,  37000, 0, TRUE, 400,  'YOUTH', '_5G', 'LTE_5G'),
    (41000,    NULL, NULL,  61000, 0, TRUE, 1000, 'YOUTH', '_5G', 'LTE_5G'),
    (24000,    NULL, NULL,  59000, 0, TRUE, 1000, 'ALL',   '_5G', 'LTE_5G'),
    (10000,    NULL, NULL,  39000, 0, TRUE, 1000, 'OLD',   '_5G', 'LTE_5G'),
    (125000,   NULL, 55000, 70000, 0, TRUE, 5000, 'ALL',   '_5G', 'LTE_5G'),
    (300,      NULL, NULL,  16500, 0, TRUE, NULL, 'OLD',   '_5G', 'LTE_5G'),
    (NULL,     5000, 11000, 69000, 0, TRUE, 5000, 'ALL',   '_5G', 'LTE_5G'),
    (95000,    NULL, 50000, 68000, 0, TRUE, 3000, 'ALL',   '_5G', 'LTE_5G'),
    (185000,   NULL, 60000, 70000, 0, TRUE, 5000, 'YOUTH', '_5G', 'LTE_5G'),
    (10000000, NULL, 120000,130000,0, TRUE, NULL, 'ALL',   '_5G', 'LTE_5G'),
    (80000,    NULL, 40000, 72000, 0, TRUE, 1000, 'YOUTH', '_5G', 'LTE_5G'),
    (24000,    NULL, NULL,  57000, 0, TRUE, 1000, 'YOUTH', '_5G', 'LTE_5G'),
    (150000,   5000, NULL,  55000, 0, TRUE, 5000, 'SOLDIER','_5G', 'LTE_5G'),
    (2000,     2000, NULL,  33000, 0, TRUE, 3000, 'SOLDIER','_5G', 'LTE_5G'),
    (8000,     NULL, NULL,  45000, 0, TRUE, 1000, 'BOY',   '_5G', 'LTE_5G'),
    (NULL,     5000, 15000, 69000, 0, TRUE, 5000, 'OLD',   '_5G', 'LTE_5G'),
    (10000000, NULL, 100000,115000,0, TRUE, NULL, 'ALL',   '_5G', 'LTE_5G'),
    (2000,     NULL, NULL,  33000, 0, TRUE, 400,  'BOY',   '_5G', 'LTE_5G'),
    (2000,     NULL, NULL,  33000, 0, TRUE, NULL, 'WELFARE','_5G','LTE_5G'),
    (6000,     NULL, NULL,  49000, 0, TRUE, 1000, 'WELFARE','_5G','LTE_5G'),
    (NULL,     5000, 15000, 69000, 0, TRUE, 5000, 'BOY',   '_5G', 'LTE_5G'),
    (14000,    NULL, NULL,  55000, 0, TRUE, 1000, 'WELFARE','_5G','LTE_5G'),
    (9000,     NULL, NULL,  59000, 0, TRUE, 1000, 'BOY',   '_5G', 'LTE_5G'),
    (150000,   NULL, 60000, 75000, 0, TRUE, 5000, 'WELFARE','_5G','LTE_5G'),
    (3300,     NULL, NULL,  29000, 0, TRUE, 400,  'KID',   '_5G', 'LTE_5G'),
    (5500,     NULL, NULL,  39000, 0, TRUE, 1000, 'KID',   '_5G', 'LTE_5G'),
    (9000,     NULL, NULL,  45000, 0, TRUE, 1000, 'KID',   '_5G', 'LTE_5G'),
    (700,      NULL, NULL,  22000, 0, TRUE, 400,  'KID',   '_5G', 'LTE_5G');

-- NUGGET
INSERT INTO mplan_detail
(basic_data_amount, daily_data, sharing_data, monthly_price, voice_call_volume, text_message, sub_data_speed, qualification, data_type, mplan_type)
VALUES
    -- 너겟 26
    (6000,    NULL,   NULL,   26000, 0, TRUE,  400,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 39
    (27000,   NULL,   NULL,   39000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 38
    (25000,   NULL,   NULL,   39000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 37
    (22000,   NULL,   NULL,   37000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 36
    (20000,   NULL,   NULL,   36000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 35
    (17000,   NULL,   NULL,   35000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 34
    (15000,   NULL,   NULL,   34000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 33
    (12000,   NULL,   NULL,   33000, 0, TRUE,  400,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 32
    (10000,   NULL,   NULL,   32000, 0, TRUE,  400,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 31
    (8000,    NULL,   NULL,   31000, 0, TRUE,  400,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 30
    (7000,    NULL,   NULL,   30000, 0, TRUE,  400,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 47
    (100000,  NULL,   60000,  47000, 0, TRUE, 3000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 46
    (80000,   NULL,   55000,  46000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 44
    (41000,   NULL,   NULL,   44000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 45
    (50000,   NULL,   NULL,   45000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 43
    (38000,   NULL,   NULL,   43000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 42
    (36000,   NULL,   NULL,   42000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 41
    (33000,   NULL,   NULL,   41000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 40
    (31000,   NULL,   NULL,   40000, 0, TRUE, 1000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 51
    (150000,  NULL,   65000,  51000, 0, TRUE, 5000,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 59
    (10000000,NULL,   70000,  59000, 0, TRUE, NULL,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 69
    (10000000,NULL,   100000, 69000, 0, TRUE, NULL,  'ALL', '_5G', 'NUGGET'),
    -- 너겟 65
    (10000000,NULL,   80000,  65000, 0, TRUE, NULL,  'ALL', '_5G', 'NUGGET'),
    -- LTE 다이렉트 22
    (1800,    NULL,   NULL,   22000, 0, TRUE, NULL,  'ALL', 'LTE','NUGGET'),
    -- LTE 다이렉트 45
    (NULL,    5000,   10000,  45000, 0, TRUE, 5000,  'ALL', 'LTE','NUGGET');

-- SMART_DEVICE
INSERT INTO mplan_detail
(basic_data_amount, daily_data, sharing_data, monthly_price, voice_call_volume, text_message, sub_data_speed, qualification, data_type, mplan_type)
VALUES
    -- 태블릿/스마트기기 500MB + 데이터 나눠쓰기
    (500,   NULL,   NULL,   11000, 2, TRUE,  NULL, 'ALL', 'LTE', 'SMART_DEVICE'),
    -- 5G 태블릿 4GB + 데이터 나눠쓰기
    (4000,  NULL,   NULL,   22000, 2, TRUE,  NULL, 'ALL', '_5G',  'SMART_DEVICE'),
    -- 태블릿/스마트기기 10GB
    (10000, NULL,   NULL,   16500, 2, TRUE,  NULL, 'ALL', 'LTE', 'SMART_DEVICE'),
    -- 태블릿/스마트기기 데이터 20GB
    (20000, NULL,   NULL,   24750, 2, TRUE,  NULL, 'ALL', 'LTE', 'SMART_DEVICE'),
    -- 태블릿/스마트기기 데이터 걱정없는 25GB
    (25000, 2000,   NULL,   65890, 2, TRUE,  3000, 'ALL', 'LTE', 'SMART_DEVICE'),
    -- LTE Wearable
    (250,   NULL,   NULL,   11000, 0, TRUE,  NULL, 'ALL', 'LTE', 'SMART_DEVICE'),
    -- 태블릿/스마트기기 3GB + 데이터 나눠쓰기
    (3000,  NULL,   NULL,   16500, 2, TRUE,  NULL, 'ALL', 'LTE', 'SMART_DEVICE'),
    -- 5G 태블릿 6GB + 데이터 나눠쓰기
    (6000,  NULL,   NULL,   33000, 2, TRUE,  1000, 'ALL', '_5G',  'SMART_DEVICE'),
    -- LTE Wearable KIDS
    (200,   NULL,   NULL,   8800,  0, TRUE,  NULL, 'KID', 'LTE', 'SMART_DEVICE');