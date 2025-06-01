-- PCB 데이터 입력 템플릿
-- 아래 형식을 복사하여 사용하세요.
-- 주의: 모든 (?) 부분을 적절한 값으로 변경해야 합니다.

INSERT INTO PCB 
(startdate, enddate, name, link, 
rgb_support, qmk_via, flex, price_tier, 
build_quality, features) 
VALUES 
('2024-03-20', '2025-12-31',
'(?)',     -- name: PCB 이름
'(?)',     -- link: 제품 링크 (NULL 가능)
(?),       -- rgb_support: RGB 지원 여부 (true/false)
(?),       -- qmk_via: QMK/VIA 지원 여부 (true/false)
(?),       -- flex: 플렉스 점수 (1-10)
(?),       -- price_tier: 가격 티어 (1-4)
(?),       -- build_quality: 빌드 품질 (1-10)
'(?)'      -- features: 주요 기능 (콤마로 구분)
); 