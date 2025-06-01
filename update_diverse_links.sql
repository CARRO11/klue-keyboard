-- 스위치 링크 업데이트
UPDATE Switches SET Link = 'https://swagkeys.com/products/gateron-ink-black-v2-switches' WHERE name LIKE '%Gateron%Ink%Black%';
UPDATE Switches SET Link = 'https://swagkeys.com/products/gateron-ink-red-v2-switches' WHERE name LIKE '%Gateron%Ink%Red%';
UPDATE Switches SET Link = 'https://swagkeys.com/products/gateron-cap-v2-yellow-switches' WHERE name LIKE '%Gateron%Cap%Yellow%';
UPDATE Switches SET Link = 'https://swagkeys.com/products/gateron-oil-king-switches' WHERE name LIKE '%Gateron%Oil%King%';
UPDATE Switches SET Link = 'https://dailyclack.com/products/cherry-mx-switches' WHERE name LIKE '%Cherry%MX%Red%';
UPDATE Switches SET Link = 'https://dailyclack.com/products/cherry-mx-black-switches' WHERE name LIKE '%Cherry%MX%Black%';
UPDATE Switches SET Link = 'https://keebsforall.com/products/kailh-box-black-switches' WHERE name LIKE '%Kailh%Box%Black%';
UPDATE Switches SET Link = 'https://keebsforall.com/products/kailh-box-white-switches' WHERE name LIKE '%Kailh%Box%White%';
UPDATE Switches SET Link = 'https://monstargears.com/products/tecsee-ice-candy-switches' WHERE name LIKE '%Tecsee%Ice%';
UPDATE Switches SET Link = 'https://monstargears.com/products/tecsee-diamond-switches' WHERE name LIKE '%Tecsee%Diamond%';

-- 플레이트 링크 업데이트
UPDATE Plate SET Link = 'https://kbdfans.com/products/65-polycarbonate-plate' WHERE material LIKE '%Polycarbonate%' AND name LIKE '%65%';
UPDATE Plate SET Link = 'https://novelkeys.com/products/aluminum-plate' WHERE material LIKE '%Aluminum%' AND name LIKE '%65%';
UPDATE Plate SET Link = 'https://cannonkeys.com/products/brass-plate' WHERE material LIKE '%Brass%' AND name LIKE '%65%';
UPDATE Plate SET Link = 'https://keebsforall.com/products/fr4-plate' WHERE material LIKE '%FR4%';
UPDATE Plate SET Link = 'https://divinikey.com/products/carbon-fiber-plate' WHERE material LIKE '%Carbon%Fiber%';
UPDATE Plate SET Link = 'https://monstargears.com/products/pom-plate' WHERE material LIKE '%POM%';

-- 스태빌라이저 링크 업데이트
UPDATE Stabilizer SET Link = 'https://swagkeys.com/products/durock-v2-stabilizers' WHERE name LIKE '%Durock%V2%';
UPDATE Stabilizer SET Link = 'https://divinikey.com/products/original-cherry-stabilizers' WHERE name LIKE '%Cherry%Original%';
UPDATE Stabilizer SET Link = 'https://zealpc.net/products/zealstabilizers' WHERE name LIKE '%Zeal%';
UPDATE Stabilizer SET Link = 'https://novelkeys.com/products/nk-stabilizers' WHERE name LIKE '%NK%';
UPDATE Stabilizer SET Link = 'https://cannonkeys.com/products/staebies-stabilizers' WHERE name LIKE '%Staebies%';
UPDATE Stabilizer SET Link = 'https://keebsforall.com/products/c3-equalz-stabilizers' WHERE name LIKE '%C3%';

-- 키캡 링크 업데이트
UPDATE Keycap SET Link = 'https://novelkeys.com/products/cherry-pbt-keycaps' WHERE profile = 'Cherry' AND material LIKE '%PBT%';
UPDATE Keycap SET Link = 'https://drop.com/buy/drop-mt3-black-on-white-keycap-set' WHERE profile = 'MT3';
UPDATE Keycap SET Link = 'https://pimpmykeyboard.com/sa-ice-cap-keyset/' WHERE profile = 'SA' AND material LIKE '%PBT%';
UPDATE Keycap SET Link = 'https://kbdfans.com/products/xda-v2-keycaps' WHERE profile = 'XDA';
UPDATE Keycap SET Link = 'https://cannonkeys.com/products/nicepbt-keycaps' WHERE profile = 'Cherry' AND name LIKE '%NicePBT%';
UPDATE Keycap SET Link = 'https://divinikey.com/products/gmk-keycaps' WHERE profile = 'Cherry' AND material LIKE '%ABS%';
UPDATE Keycap SET Link = 'https://keebsforall.com/products/akko-keycaps' WHERE name LIKE '%Akko%';
UPDATE Keycap SET Link = 'https://swagkeys.com/products/epbt-keycaps' WHERE name LIKE '%ePBT%';

-- PCB 링크 업데이트
UPDATE PCB SET Link = 'https://kbdfans.com/products/dz65-rgb-hot-swap-pcb' WHERE name LIKE '%DZ65%';
UPDATE PCB SET Link = 'https://keebsforall.com/products/kfa-65-pcb' WHERE name LIKE '%KFA%65%';
UPDATE PCB SET Link = 'https://cannonkeys.com/products/an-c-60-pcb' WHERE name LIKE '%AN-C%';
UPDATE PCB SET Link = 'https://novelkeys.com/products/nk65-pcb' WHERE name LIKE '%NK65%';
UPDATE PCB SET Link = 'https://monstargears.com/products/monster-65-pcb' WHERE name LIKE '%Monster%65%';
UPDATE PCB SET Link = 'https://keebsforall.com/products/blade65-pcb' WHERE name LIKE '%Blade65%';

-- 기본 링크 업데이트 (위의 조건에 해당하지 않는 경우)
UPDATE Switches SET Link = 'https://swagkeys.com/collections/switches' WHERE Link IS NULL;
UPDATE Plate SET Link = 'https://novelkeys.com/collections/plates' WHERE Link IS NULL;
UPDATE Stabilizer SET Link = 'https://divinikey.com/collections/keyboard-stabilizers' WHERE Link IS NULL;
UPDATE Keycap SET Link = 'https://kbdfans.com/collections/keycaps' WHERE Link IS NULL;
UPDATE PCB SET Link = 'https://keebsforall.com/collections/pcbs' WHERE Link IS NULL; 