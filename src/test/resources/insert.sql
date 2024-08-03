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
