-- 키캡 데이터 입력 템플릿
-- 아래 형식을 복사하여 사용하세요.
-- 주의: 모든 (?) 부분을 적절한 값으로 변경해야 합니다.

INSERT INTO Keycap 
(startdate, enddate, name, material,
profile_type, thickness, sound_profile,
price_tier, build_quality) 
VALUES 
('2024-03-20', '2025-12-31',
'(?)',     -- name: 키캡 이름
'(?)',     -- material: 재질 (PBT 또는 ABS)
'(?)',     -- profile_type: 프로파일 타입 (Cherry, OEM, SA 등)
(?),       -- thickness: 두께 (mm)
(?),       -- sound_profile: 소리 특성 (1-10)
(?),       -- price_tier: 가격 티어 (1-4)
(?)        -- build_quality: 빌드 품질 (1-10)
); 