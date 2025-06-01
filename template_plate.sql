-- 플레이트 데이터 입력 템플릿
-- 아래 형식을 복사하여 사용하세요.
-- 주의: 모든 (?) 부분을 적절한 값으로 변경해야 합니다.

INSERT INTO Plate 
(startdate, enddate, name, material, 
flex_level, thickness, stiffness, sound_profile,
price_tier, weight, flex) 
VALUES 
('2024-03-20', '2025-12-31',
'(?)',     -- name: 플레이트 이름 (예: 'Aluminum 60%', 'Brass Universal')
'(?)',     -- material: 재질 (첫 글자만 대문자)
(?),       -- flex_level: 플렉스 레벨 (1-10)
(?),       -- thickness: 두께 (mm)
(?),       -- stiffness: 강성 (1-10)
(?),       -- sound_profile: 소리 특성 (1-10)
(?),       -- price_tier: 가격 티어 (1-4)
(?),       -- weight: 무게 (g)
(?)        -- flex: 플렉스 점수 (1-10)
); 