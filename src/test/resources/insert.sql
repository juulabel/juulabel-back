-- 전통주 종류 INSERT
INSERT INTO alcohol_type (id, created_at, updated_at, name)
VALUES (1, now(), now()), '탁주'),
       (2, now(), now()), '소주/증류주'),
       (3, now(), now()), '약청주'),
       (4, now(), now()), '과실주'),
       (5, now(), now()), '기타 주류');

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

/* 
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
 */

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

-- Flavor 데이터 삽입
INSERT INTO flavor (name) VALUES ('단맛');
INSERT INTO flavor (name) VALUES ('신맛');
INSERT INTO flavor (name) VALUES ('쓴맛');
INSERT INTO flavor (name) VALUES ('감칠맛');
INSERT INTO flavor (name) VALUES ('여운');
INSERT INTO flavor (name) VALUES ('무게감');

-- FlavorLevel 데이터 삽입
-- FlavorLevel 데이터 삽입
INSERT INTO flavor_level (flavor, score, description)
VALUES (1, 1, '매우 낮음'),
       (1, 2, '낮음'),
       (1, 3, '중간'),
       (1, 4, '높음'),
       (1, 5, '매우 높음'),
       (2, 1, '매우 낮음'),
       (2, 2, '낮음'),
       (2, 3, '중간'),
       (2, 4, '높음'),
       (2, 5, '매우 높음'),
       (3, 1, '매우 낮음'),
       (3, 2, '낮음'),
       (3, 3, '중간'),
       (3, 4, '높음'),
       (3, 5, '매우 높음'),
       (4, 1, '매우 낮음'),
       (4, 2, '낮음'),
       (4, 3, '중간'),
       (4, 4, '높음'),
       (4, 5, '매우 높음'),
       (5, 1, '매우 낮음'),
       (5, 2, '낮음'),
       (5, 3, '중간'),
       (5, 4, '높음'),
       (5, 5, '매우 높음');

-- Alcohol_type_flavor 데이터 삽입
INSERT INTO alcohol_type_flavor (created_at, updated_at, is_used, alcohol_type_id, flavor_id)
values (now(), now(), true, 1 , 1),
       (now(), now(), true, 1 , 2),
       (now(), now(), true, 1 , 3),
       (now(), now(), true, 1 , 4),
       (now(), now(), true, 1 , 5),
       (now(), now(), true, 1 , 6),
       (now(), now(), true, 2 , 1),
       (now(), now(), true, 2 , 2),
       (now(), now(), true, 2 , 3),
       (now(), now(), true, 2 , 4),
       (now(), now(), true, 2 , 5),
       (now(), now(), true, 2 , 6),
       (now(), now(), true, 3 , 1),
       (now(), now(), true, 3 , 2),
       (now(), now(), true, 3 , 3),
       (now(), now(), true, 3 , 4),
       (now(), now(), true, 3 , 5),
       (now(), now(), true, 3 , 6),
       (now(), now(), true, 4 , 1),
       (now(), now(), true, 4 , 2),
       (now(), now(), true, 4 , 3),
       (now(), now(), true, 4 , 4),
       (now(), now(), true, 4 , 5),
       (now(), now(), true, 4 , 6),
       (now(), now(), true, 5 , 1),
       (now(), now(), true, 5 , 2),
       (now(), now(), true, 5 , 3),
       (now(), now(), true, 5 , 4),
       (now(), now(), true, 5 , 5),
       (now(), now(), true, 5 , 6);

-- Sensory 데이터 삽입
INSERT INTO sensory (name, created_at, updated_at)
VALUES ('탁도', NOW(), NOW()),
       ('탄산도', NOW(), NOW()),
       ('점성도', NOW(), NOW()),
       ('투명도', NOW(), NOW()),
       ('침전물', NOW(), NOW()),
       ('진하기', NOW(), NOW());

-- AlcoholTypeSensory 데이터 삽입
-- 탁도 (Sensory ID = 1)
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_id, is_used, created_at, updated_at)
VALUES (1, 1, TRUE, NOW(), NOW()), -- 탁주
       (3, 1, TRUE, NOW(), NOW());  -- 약청주

-- 탄산도 (Sensory ID = 2)
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_id, is_used, created_at, updated_at)
VALUES (1, 2, TRUE, NOW(), NOW()), -- 탁주
       (4, 2, TRUE, NOW(), NOW()), -- 과실주
       (5, 2, TRUE, NOW(), NOW());  -- 기타 주류

-- 점성도 (Sensory ID = 3)
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_id, is_used, created_at, updated_at)
VALUES (1, 3, TRUE, NOW(), NOW()), -- 탁주
       (2, 3, TRUE, NOW(), NOW()), -- 소주/증류주
       (3, 3, TRUE, NOW(), NOW());  -- 약청주

-- 투명도 (Sensory ID = 4)
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_id, is_used, created_at, updated_at)
VALUES (2, 4, TRUE, NOW(), NOW());  -- 소주

-- 침전물 (Sensory ID = 5)
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_id, is_used, created_at, updated_at)
VALUES (3, 5, TRUE, NOW(), NOW()), -- 약청주
       (5, 5, TRUE, NOW(), NOW());  -- 기타 주류

-- 진하기 (Sensory ID = 6)
INSERT INTO alcohol_type_sensory (alcohol_type_id, sensory_id, is_used, created_at, updated_at)
VALUES (4, 6, TRUE, NOW(), NOW()), -- 과실주
       (5, 6, TRUE, NOW(), NOW());  -- 기타 주류

-- TurbidityLevel (탁도)
INSERT INTO sensory_level (sensory, score, description)
VALUES (1, 1, '맑음'),
       (1, 2, '맑은 편'),
       (1, 3, '중간'),
       (1, 4, '탁한 편'),
       (1, 5, '탁함');

-- CarbonationLevel (탄산도)
INSERT INTO sensory_level (sensory, score, description)
VALUES (2, 1, '없음'),
       (2, 2, '적음'),
       (2, 3, '중간'),
       (2, 4, '있는 편'),
       (2, 5, '많음');

-- ViscosityLevel (점성도)
INSERT INTO sensory_level (sensory, score, description)
VALUES (3, 1, '없음'),
       (3, 2, '적음'),
       (3, 3, '중간'),
       (3, 4, '있는 편'),
       (3, 5, '많음');

-- ClarityLevel (투명도)
INSERT INTO sensory_level (sensory, score, description)
VALUES (4, 1, '맑음'),
       (4, 2, '맑은 편'),
       (4, 3, '중간'),
       (4, 4, '흐린 편'),
       (4, 5, '흐림');

-- SedimentLevel (침전물)
INSERT INTO sensory_level (sensory, score, description)
VALUES (5, 1, '없음'),
       (5, 5, '있음');

-- DensityLevel (진하기)
INSERT INTO sensory_level (sensory, score, description)
VALUES (6, 1, '연함'),
       (6, 2, '연한 편'),
       (6, 3, '중간'),
       (6, 4, '진한 편'),
       (6, 5, '진함');

