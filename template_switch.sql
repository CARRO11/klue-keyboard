-- 스위치 데이터 입력 템플릿
-- 아래 형식을 복사하여 사용하세요.
-- 주의: 모든 (?) 부분을 적절한 값으로 변경해야 합니다.

INSERT INTO Switches 
(startdate, enddate, name, manufacturer, type, 
actuation_force, bottom_force, actuation_point, total_travel,
spring, stem_material, top_housing, bottom_housing,
tactile_bump_position, sound_level, sound_signature,
price_tier, build_quality, smoothness, wobble) 
VALUES 
('2024-03-20', '2025-12-31',
'(?)',     -- name: 스위치 이름
'(?)',     -- manufacturer: 제조사
'(?)',     -- type: linear/tactile/clicky
(?),       -- actuation_force: 작동력 (45-70)
(?),       -- bottom_force: 바닥력 (50-80)
(?),       -- actuation_point: 작동점 (1.0-2.5)
(?),       -- total_travel: 전체 이동거리 (3.0-4.0)
'(?)',     -- spring: 스프링 종류
'(?)',     -- stem_material: 스템 재질
'(?)',     -- top_housing: 상부 하우징 재질
'(?)',     -- bottom_housing: 하부 하우징 재질
(?),       -- tactile_bump_position: 돌기 위치 (0-1)
(?),       -- sound_level: 소리 크기 (1-10)
'(?)',     -- sound_signature: 소리 특성
(?),       -- price_tier: 가격 티어 (1-4)
(?),       -- build_quality: 빌드 품질 (1-10)
(?),       -- smoothness: 부드러움 (1-10)
(?)        -- wobble: 흔들림 (1-10)
); 