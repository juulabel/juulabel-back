-- 전통주 종류 INSERT
INSERT INTO alcohol_type (name, created_at, updated_at)
VALUES ('탁주', now(), now()),
       ('소주/증류주', now(), now()),
       ('약청주', now(), now()),
       ('과실주', now(), now()),
       ('기타 주류', now(), now());

-- 양조장 INSERT
INSERT INTO brewery (name, region, created_at, updated_at)
VALUES ('청주 양조장', '충청북도 청주시', now(), now()),
       ('서울 양조장', '서울특별시', now(), now()),
       ('부산 양조장', '부산광역시', now(), now()),
       ('전주 양조장', '전라북도 전주시', now(), now()),
       ('대구 양조장', '대구광역시', now(), now());

-- 전통주 INSERT
INSERT INTO alcoholic_drinks (alcohol_type_id, brewery_id, name, description, alcohol_content, created_at, updated_at)
VALUES (1, 1, '청주탁주', '신선하고 깔끔한 맛의 청주 탁주입니다.', 6.5, now(), now()),
       (2, 2, '서울소주', '전통 방식으로 만들어진 서울 소주입니다.', 19.5, now(), now()),
       (3, 3, '부산약주', '깊은 맛과 향을 자랑하는 부산 약주입니다.', 13.0, now(), now()),
       (4, 4, '전주과실주', '전주의 신선한 과일로 만들어진 과실주입니다.', 12.0, now(), now()),
       (5, 5, '대구기타주류', '독특한 향과 맛을 가진 대구 기타 주류입니다.', 8.0, now(), now()),
       (1, 2, '서울탁주', '부드럽고 깊은 맛을 자랑하는 안동 탁주입니다.', 7.0, now(), now()),
       (1, 3, '부산탁주', '신선한 맛과 향이 일품인 전주 탁주입니다.', 6.8, now(), now());

-- 탁주
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_type, is_used, created_at, updated_at)
VALUES (1, 'TURBIDITY', TRUE, NOW(), NOW()),
       (1, 'CARBONATION', TRUE, NOW(), NOW()),
       (1, 'VISCOSITY', TRUE, NOW(), NOW());

-- 소주/증류주
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_type, is_used, created_at, updated_at)
VALUES (2, 'VISCOSITY', TRUE, NOW(), NOW()),
       (2, 'CLARITY', TRUE, NOW(), NOW());

-- 약청주
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_type, is_used, created_at, updated_at)
VALUES (3, 'TURBIDITY', TRUE, NOW(), NOW()),
       (3, 'VISCOSITY', TRUE, NOW(), NOW()),
       (3, 'SEDIMENT', TRUE, NOW(), NOW());

-- 과실주
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_type, is_used, created_at, updated_at)
VALUES (4, 'CARBONATION', TRUE, NOW(), NOW()),
       (4, 'DENSITY', TRUE, NOW(), NOW());

-- 기타 주류
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_type, is_used, created_at, updated_at)
VALUES (5, 'CARBONATION', TRUE, NOW(), NOW()),
       (5, 'DENSITY', TRUE, NOW(), NOW()),
       (5, 'SEDIMENT', TRUE, NOW(), NOW());

-- 색상 데이터 삽입
INSERT INTO color (name, rgb)
VALUES ('빨간색', '#FF0000'),
       ('파란색', '#0000FF'),
       ('초록색', '#00FF00'),
       ('노란색', '#FFFF00'),
       ('검정색', '#000000');

-- 탁주와 색상 매핑 데이터 삽입
INSERT INTO alcohol_type_color (alcohol_type_id, color_id, is_used, created_at, updated_at)
VALUES (1, 1, TRUE, now(), now()), -- 탁주 - 빨간색
       (1, 2, TRUE, now(), now()), -- 탁주 - 파란색
       (1, 3, TRUE, now(), now());  -- 탁주 - 초록색

-- 소주/증류주와 색상 매핑 데이터 삽입
INSERT INTO alcohol_type_color (alcohol_type_id, color_id, is_used, created_at, updated_at)
VALUES (2, 2, TRUE, now(), now()), -- 소주/증류주 - 파란색
       (2, 3, TRUE, now(), now()), -- 소주/증류주 - 초록색
       (2, 4, TRUE, now(), now());  -- 소주/증류주 - 노란색

-- 약청주와 색상 매핑 데이터 삽입
INSERT INTO alcohol_type_color (alcohol_type_id, color_id, is_used, created_at, updated_at)
VALUES (3, 1, TRUE, now(), now()), -- 약청주 - 빨간색
       (3, 4, TRUE, now(), now()), -- 약청주 - 노란색
       (3, 5, TRUE, now(), now());  -- 약청주 - 검정색

-- 과실주와 색상 매핑 데이터 삽입
INSERT INTO alcohol_type_color (alcohol_type_id, color_id, is_used, created_at, updated_at)
VALUES (4, 1, TRUE, now(), now()), -- 과실주 - 빨간색
       (4, 3, TRUE, now(), now()), -- 과실주 - 초록색
       (4, 4, TRUE, now(), now());  -- 과실주 - 노란색

-- 기타 주류와 색상 매핑 데이터 삽입
INSERT INTO alcohol_type_color (alcohol_type_id, color_id, is_used, created_at, updated_at)
VALUES (5, 2, TRUE, now(), now()), -- 기타 주류 - 파란색
       (5, 4, TRUE, now(), now()), -- 기타 주류 - 노란색
       (5, 5, TRUE, now(), now());  -- 기타 주류 - 검정색

-- 카테고리 데이터 삽입
INSERT INTO category (type, code, name, is_used, created_at, updated_at)
VALUES ('SCENT', 'SC000', '자연', TRUE, NOW(), NOW()),
       ('SCENT', 'SC001', '과일/채소', TRUE, NOW(), NOW()),
       ('SCENT', 'SC002', '곡류/밥', TRUE, NOW(), NOW()),
       ('SCENT', 'SC003', '향신료/기타', TRUE, NOW(), NOW());

-- 향 데이터 입력
INSERT INTO scent (name)
VALUES
    ('꽃'),
    ('허브'),
    ('꿀/나무'),
    ('흙'),
    ('귤/오렌지'),
    ('레몬/라임'),
    ('버섯'),
    ('보리'),
    ('옥수수'),
    ('향신료'),
    ('계피'),
    ('꿀'),
    ('팔각');

-- AlcoholTypeScent 데이터 삽입
INSERT INTO alcohol_type_scent (category_id, alcohol_type_id, scent_id, is_used, created_at, updated_at)
VALUES
    -- 자연 카테고리 (SC000)
    (1, 1, 1, TRUE, NOW(), NOW()),  -- 꽃
    (1, 1, 2, TRUE, NOW(), NOW()),  -- 허브
    (1, 1, 3, TRUE, NOW(), NOW()),  -- 꿀/나무
    (1, 1, 4, TRUE, NOW(), NOW()),  -- 흙

    (1, 2, 1, TRUE, NOW(), NOW()),  -- 꽃
    (1, 2, 2, TRUE, NOW(), NOW()),  -- 허브
    (1, 2, 3, TRUE, NOW(), NOW()),  -- 꿀/나무
    (1, 2, 4, TRUE, NOW(), NOW()),  -- 흙

    (1, 3, 1, TRUE, NOW(), NOW()),  -- 꽃
    (1, 3, 2, TRUE, NOW(), NOW()),  -- 허브
    (1, 3, 3, TRUE, NOW(), NOW()),  -- 꿀/나무
    (1, 3, 4, FALSE, NOW(), NOW()),  -- 흙

    (1, 4, 1, FALSE, NOW(), NOW()),  -- 꽃
    (1, 4, 2, FALSE, NOW(), NOW()),  -- 허브
    (1, 4, 3, TRUE, NOW(), NOW()),  -- 꿀/나무
    (1, 4, 4, FALSE, NOW(), NOW()),  -- 흙

    -- 과일/채소 카테고리 (SC001)
    (2, 1, 5, TRUE, NOW(), NOW()),  -- 귤/오렌지
    (2, 1, 6, TRUE, NOW(), NOW()),  -- 레몬/라임

    (2, 2, 5, TRUE, NOW(), NOW()),  -- 귤/오렌지
    (2, 2, 6, TRUE, NOW(), NOW()),  -- 레몬/라임

    (2, 3, 5, FALSE, NOW(), NOW()),  -- 귤/오렌지
    (2, 3, 6, TRUE, NOW(), NOW()),  -- 레몬/라임

    (2, 4, 5, TRUE, NOW(), NOW()),  -- 귤/오렌지
    (2, 4, 6, FALSE, NOW(), NOW()),  -- 레몬/라임

    -- 곡류/밥 카테고리 (SC002)
    (3, 1, 7, TRUE, NOW(), NOW()),  -- 버섯
    (3, 1, 8, TRUE, NOW(), NOW()),  -- 보리
    (3, 1, 9, TRUE, NOW(), NOW()),  -- 옥수수

    (3, 2, 7, TRUE, NOW(), NOW()),  -- 버섯
    (3, 2, 8, TRUE, NOW(), NOW()),  -- 보리
    (3, 2, 9, FALSE, NOW(), NOW()),  -- 옥수수

    (3, 3, 7, TRUE, NOW(), NOW()),  -- 버섯
    (3, 3, 8, TRUE, NOW(), NOW()),  -- 보리
    (3, 3, 9, TRUE, NOW(), NOW()),  -- 옥수수

    (3, 4, 7, FALSE, NOW(), NOW()),  -- 버섯
    (3, 4, 8, TRUE, NOW(), NOW()),  -- 보리
    (3, 4, 9, FALSE, NOW(), NOW()),  -- 옥수수

    -- 향신료/기타 카테고리 (SC003)
    (4, 1, 10, TRUE, NOW(), NOW()), -- 향신료

    (4, 2, 11, TRUE, NOW(), NOW()), -- 계피
    (4, 2, 12, TRUE, NOW(), NOW()), -- 꿀

    (4, 3, 13, TRUE, NOW(), NOW()), -- 팔각

    (4, 4, 10, TRUE, NOW(), NOW()), -- 향신료
    (4, 4, 11, TRUE, NOW(), NOW()), -- 계피
    (4, 4, 12, TRUE, NOW(), NOW()), -- 꿀
    (4, 4, 13, TRUE, NOW(), NOW()); -- 팔각
