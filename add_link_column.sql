-- 스위치 테이블에 Link 칼럼 추가
ALTER TABLE Switches ADD COLUMN Link VARCHAR(255);

-- 플레이트 테이블에 Link 칼럼 추가
ALTER TABLE Plate ADD COLUMN Link VARCHAR(255);

-- 스태빌라이저 테이블에 Link 칼럼 추가
ALTER TABLE Stabilizer ADD COLUMN Link VARCHAR(255);

-- 키캡 테이블에 Link 칼럼 추가
ALTER TABLE Keycap ADD COLUMN Link VARCHAR(255);

-- PCB 테이블에 Link 칼럼 추가
ALTER TABLE PCB ADD COLUMN Link VARCHAR(255);

-- 스위치 링크 업데이트
UPDATE Switches SET Link = 'https://kbdfans.com/collections/switches' WHERE Link IS NULL;

-- 플레이트 링크 업데이트
UPDATE Plate SET Link = 'https://kbdfans.com/collections/plate' WHERE Link IS NULL;

-- 스태빌라이저 링크 업데이트
UPDATE Stabilizer SET Link = 'https://kbdfans.com/collections/keyboard-stabilizer' WHERE Link IS NULL;

-- 키캡 링크 업데이트
UPDATE Keycap SET Link = 'https://kbdfans.com/collections/keycaps' WHERE Link IS NULL;

-- PCB 링크 업데이트
UPDATE PCB SET Link = 'https://kbdfans.com/collections/pcb' WHERE Link IS NULL; 