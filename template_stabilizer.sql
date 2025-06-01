-- 스태빌라이저 데이터 입력 템플릿
-- 아래 형식을 복사하여 사용하세요.
-- 주의: 모든 (?) 부분을 적절한 값으로 변경해야 합니다.

INSERT INTO Stabilizer 
(startdate, enddate, name, material,
size, type, rattle, smoothness,
sound_profile, price_tier, build_quality) 
VALUES 
('2024-03-20', '2025-12-31',
'(?)',     -- name: 스태빌라이저 이름
'(?)',     -- material: 재질 (POM, ABS 등)
'(?)',     -- size: 사이즈 (Standard 등)
'(?)',     -- type: 타입 (Screw-in, Clip-in, Plate-mount)
(?),       -- rattle: 흔들림 (1-10)
(?),       -- smoothness: 부드러움 (1-10)
(?),       -- sound_profile: 소리 특성 (1-10)
(?),       -- price_tier: 가격 티어 (1-4)
(?)        -- build_quality: 빌드 품질 (1-10)
); 