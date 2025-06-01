-- 스위치 링크 업데이트
UPDATE Switches SET Link = 'https://kbdfans.com/collections/gateron-switch/products/gateron-ink-v2-switches' WHERE name LIKE '%Gateron%';
UPDATE Switches SET Link = 'https://kbdfans.com/collections/cherry-switches/products/cherry-rgb-switches' WHERE name LIKE '%Cherry%';
UPDATE Switches SET Link = 'https://kbdfans.com/collections/kailh-switches/products/kailh-box-v2-switches' WHERE name LIKE '%Kailh%';

-- 플레이트 링크 업데이트
UPDATE Plate SET Link = 'https://kbdfans.com/collections/65-layout-plate/products/65-polycarbonate-plate' WHERE material LIKE '%Polycarbonate%';
UPDATE Plate SET Link = 'https://kbdfans.com/collections/65-layout-plate/products/65-cnc-aluminum-plate' WHERE material LIKE '%Aluminum%';
UPDATE Plate SET Link = 'https://kbdfans.com/collections/65-layout-plate/products/65-brass-plate' WHERE material LIKE '%Brass%';

-- 스태빌라이저 링크 업데이트
UPDATE Stabilizer SET Link = 'https://kbdfans.com/collections/keyboard-stabilizer/products/durock-v2-stabilizer' WHERE name LIKE '%Durock%';
UPDATE Stabilizer SET Link = 'https://kbdfans.com/collections/keyboard-stabilizer/products/cherry-original-pcb-stabilizers' WHERE name LIKE '%Cherry%';
UPDATE Stabilizer SET Link = 'https://kbdfans.com/collections/keyboard-stabilizer/products/zeal-pc-stabilizers' WHERE name LIKE '%Zeal%';

-- 키캡 링크 업데이트
UPDATE Keycap SET Link = 'https://kbdfans.com/collections/cherry-profile/products/pbt-dye-sub-keycaps' WHERE profile = 'Cherry';
UPDATE Keycap SET Link = 'https://kbdfans.com/collections/sa-profile/products/sa-profile-dye-sub-keycaps' WHERE profile = 'SA';
UPDATE Keycap SET Link = 'https://kbdfans.com/collections/xda-profile/products/xda-profile-pbt-keycaps' WHERE profile = 'XDA';

-- PCB 링크 업데이트
UPDATE PCB SET Link = 'https://kbdfans.com/collections/65-pcb/products/dz65-rgb-hot-swap-pcb' WHERE name LIKE '%DZ65%';
UPDATE PCB SET Link = 'https://kbdfans.com/collections/65-pcb/products/kbd67-mkii-pcb' WHERE name LIKE '%KBD67%';

-- 기본 링크 업데이트 (위의 조건에 해당하지 않는 경우)
UPDATE Switches SET Link = 'https://kbdfans.com/collections/switches' WHERE Link IS NULL;
UPDATE Plate SET Link = 'https://kbdfans.com/collections/plate' WHERE Link IS NULL;
UPDATE Stabilizer SET Link = 'https://kbdfans.com/collections/keyboard-stabilizer' WHERE Link IS NULL;
UPDATE Keycap SET Link = 'https://kbdfans.com/collections/keycaps' WHERE Link IS NULL;
UPDATE PCB SET Link = 'https://kbdfans.com/collections/pcb' WHERE Link IS NULL; 