from typing import Dict, List
import numpy as np
import pandas as pd
import os

class KeyboardComponentGenerator:
    def __init__(self):
        # 스위치 데이터
        self.switch_data = {
            'Cherry MX Red': {
                'type': 'linear',
                'linearScore': 0.9,
                'tactileScore': 0.1,
                'soundScore': 0.5,
                'weightScore': 0.6,    # 45g
                'smoothnessScore': 0.8,
                'speedScore': 0.8,
                'stabilityScore': 0.7,
                'durabilityScore': 0.8  # 1억회
            },
            'Cherry MX Brown': {
                'type': 'tactile',
                'linearScore': 0.3,
                'tactileScore': 0.8,
                'soundScore': 0.6,
                'weightScore': 0.6,    # 45g
                'smoothnessScore': 0.7,
                'speedScore': 0.7,
                'stabilityScore': 0.7,
                'durabilityScore': 0.8  # 1억회
            },
            'Cherry MX Blue': {
                'type': 'clicky',
                'linearScore': 0.3,
                'tactileScore': 0.8,
                'soundScore': 0.9,
                'weightScore': 0.7,    # 50g
                'smoothnessScore': 0.6,
                'speedScore': 0.6,
                'stabilityScore': 0.7,
                'durabilityScore': 0.8  # 1억회
            },
            'Gateron Yellow': {
                'type': 'linear',
                'linearScore': 0.85,
                'tactileScore': 0.1,
                'soundScore': 0.6,
                'weightScore': 0.55,   # 50g
                'smoothnessScore': 0.9,
                'speedScore': 0.85,
                'stabilityScore': 0.75,
                'durabilityScore': 0.7  # 8천만회
            },
            'Gateron Black Ink V2': {
                'type': 'linear',
                'linearScore': 0.95,
                'tactileScore': 0.1,
                'soundScore': 0.8,
                'weightScore': 0.7,    # 70g
                'smoothnessScore': 0.95,
                'speedScore': 0.8,
                'stabilityScore': 0.85,
                'durabilityScore': 0.75
            },
            'Boba U4T': {
                'type': 'tactile',
                'linearScore': 0.2,
                'tactileScore': 0.95,
                'soundScore': 0.85,
                'weightScore': 0.65,   # 62g
                'smoothnessScore': 0.85,
                'speedScore': 0.7,
                'stabilityScore': 0.9,
                'durabilityScore': 0.85
            },
            'Holy Panda': {
                'type': 'tactile',
                'linearScore': 0.2,
                'tactileScore': 0.9,
                'soundScore': 0.8,
                'weightScore': 0.7,    # 67g
                'smoothnessScore': 0.8,
                'speedScore': 0.65,
                'stabilityScore': 0.85,
                'durabilityScore': 0.8
            },
            'Kailh Box White': {
                'type': 'clicky',
                'linearScore': 0.3,
                'tactileScore': 0.85,
                'soundScore': 0.9,
                'weightScore': 0.6,    # 55g
                'smoothnessScore': 0.7,
                'speedScore': 0.75,
                'stabilityScore': 0.8,
                'durabilityScore': 0.85
            },
            'Alpaca V2': {
                'type': 'linear',
                'linearScore': 0.9,
                'tactileScore': 0.1,
                'soundScore': 0.75,
                'weightScore': 0.6,    # 62g
                'smoothnessScore': 0.9,
                'speedScore': 0.85,
                'stabilityScore': 0.8,
                'durabilityScore': 0.8
            },
            'NK Cream': {
                'type': 'linear',
                'linearScore': 0.85,
                'tactileScore': 0.15,
                'soundScore': 0.85,
                'weightScore': 0.65,   # 65g
                'smoothnessScore': 0.75,
                'speedScore': 0.8,
                'stabilityScore': 0.8,
                'durabilityScore': 0.85
            }
        }

        # 키캡 데이터
        self.keycap_data = {
            'GMK (ABS)': {
                'material': 'abs',
                'profile': 'cherry',
                'thickness': 0.85,     # 두께 (0-1)
                'sound_profile': 0.7,  # 소리 특성 (0-1)
                'build_quality': 0.9,  # 빌드 품질 (0-1)
                'price_tier': 0.9,     # 가격대 (0-1)
                'rgb_compatible': True, # RGB 호환성
                'durability': 0.7      # 내구성 (0-1)
            },
            'ePBT (PBT)': {
                'material': 'pbt',
                'profile': 'cherry',
                'thickness': 0.8,
                'sound_profile': 0.8,
                'build_quality': 0.85,
                'price_tier': 0.7,
                'rgb_compatible': True,
                'durability': 0.9
            },
            'MT3 (PBT)': {
                'material': 'pbt',
                'profile': 'mt3',
                'thickness': 0.9,
                'sound_profile': 0.85,
                'build_quality': 0.9,
                'price_tier': 0.8,
                'rgb_compatible': False,
                'durability': 0.9
            },
            'SA (ABS)': {
                'material': 'abs',
                'profile': 'sa',
                'thickness': 0.8,
                'sound_profile': 0.75,
                'build_quality': 0.85,
                'price_tier': 0.8,
                'rgb_compatible': True,
                'durability': 0.7
            },
            'KAT (PBT)': {
                'material': 'pbt',
                'profile': 'kat',
                'thickness': 0.85,
                'sound_profile': 0.8,
                'build_quality': 0.85,
                'price_tier': 0.75,
                'rgb_compatible': True,
                'durability': 0.85
            }
        }

        # 폼/댐핑 데이터
        self.foam_data = {
            'Poron Foam': {
                'type': 'poron',
                'thickness': 0.8,      # 두께 (0-1)
                'density': 0.7,        # 밀도 (0-1)
                'sound_dampening': 0.8, # 소리 감쇠 (0-1)
                'compression': 0.7,     # 압축성 (0-1)
                'durability': 0.9,     # 내구성 (0-1)
                'price_tier': 0.7      # 가격대 (0-1)
            },
            'PE Foam': {
                'type': 'pe',
                'thickness': 0.6,
                'density': 0.5,
                'sound_dampening': 0.7,
                'compression': 0.8,
                'durability': 0.7,
                'price_tier': 0.3
            },
            'Neoprene Foam': {
                'type': 'neoprene',
                'thickness': 0.7,
                'density': 0.6,
                'sound_dampening': 0.75,
                'compression': 0.75,
                'durability': 0.8,
                'price_tier': 0.5
            },
            'EVA Foam': {
                'type': 'eva',
                'thickness': 0.65,
                'density': 0.55,
                'sound_dampening': 0.65,
                'compression': 0.7,
                'durability': 0.75,
                'price_tier': 0.4
            },
            'Sorbothane': {
                'type': 'sorbothane',
                'thickness': 0.9,
                'density': 0.9,
                'sound_dampening': 0.95,
                'compression': 0.6,
                'durability': 0.85,
                'price_tier': 0.8
            }
        }

        # 가스켓 데이터
        self.gasket_data = {
            'Poron Gasket': {
                'material': 'poron',
                'thickness': 0.7,      # 두께 (0-1)
                'flexibility': 0.8,    # 유연성 (0-1)
                'dampening': 0.75,     # 충격 흡수 (0-1)
                'durability': 0.85,    # 내구성 (0-1)
                'price_tier': 0.6      # 가격대 (0-1)
            },
            'Silicone Gasket': {
                'material': 'silicone',
                'thickness': 0.75,
                'flexibility': 0.9,
                'dampening': 0.8,
                'durability': 0.9,
                'price_tier': 0.7
            },
            'EPDM Gasket': {
                'material': 'epdm',
                'thickness': 0.8,
                'flexibility': 0.85,
                'dampening': 0.85,
                'durability': 0.9,
                'price_tier': 0.75
            }
        }

        # O-링 데이터
        self.oring_data = {
            'Silicone 50A': {
                'material': 'silicone',
                'hardness': 0.5,       # 경도 (0-1)
                'dampening': 0.8,      # 충격 흡수 (0-1)
                'durability': 0.85,    # 내구성 (0-1)
                'price_tier': 0.5      # 가격대 (0-1)
            },
            'Silicone 70A': {
                'material': 'silicone',
                'hardness': 0.7,
                'dampening': 0.7,
                'durability': 0.85,
                'price_tier': 0.5
            },
            'EPDM 60A': {
                'material': 'epdm',
                'hardness': 0.6,
                'dampening': 0.75,
                'durability': 0.9,
                'price_tier': 0.6
            }
        }

        # 케이블 데이터
        self.cable_data = {
            'Custom Coiled (USB-C)': {
                'type': 'coiled',
                'connector': 'usb_c',
                'length': 1.5,         # 길이 (m)
                'quality': 0.9,        # 품질 (0-1)
                'flexibility': 0.8,    # 유연성 (0-1)
                'durability': 0.85,    # 내구성 (0-1)
                'price_tier': 0.8,     # 가격대 (0-1)
                'detachable': True     # 분리 가능 여부
            },
            'Straight Cable (USB-C)': {
                'type': 'straight',
                'connector': 'usb_c',
                'length': 1.8,
                'quality': 0.8,
                'flexibility': 0.85,
                'durability': 0.9,
                'price_tier': 0.5,
                'detachable': True
            },
            'Magnetic Cable (USB-C)': {
                'type': 'magnetic',
                'connector': 'usb_c',
                'length': 1.2,
                'quality': 0.85,
                'flexibility': 0.9,
                'durability': 0.8,
                'price_tier': 0.7,
                'detachable': True
            }
        }

        # 하우징 데이터
        self.housing_data = {
            'Tofu60': {
                'type': 'aluminum',
                'mounting': 'tray_mount',
                'weight': 0.8,        # 무게감 (0-1)
                'acoustics': 0.7,     # 음향 특성 (0-1)
                'build_quality': 0.8, # 빌드 품질 (0-1)
                'price_tier': 0.6,    # 가격대 (0-1)
                'rgb_support': True,  # RGB 지원 여부
                'angle': 6,           # 타이핑 각도 (도)
                'size': '60%'         # 키보드 크기
            },
            'KBD67 Lite': {
                'type': 'plastic',
                'mounting': 'gasket_mount',
                'weight': 0.4,
                'acoustics': 0.8,
                'build_quality': 0.7,
                'price_tier': 0.4,
                'rgb_support': True,
                'angle': 6,
                'size': '65%'
            },
            'Mode80': {
                'type': 'aluminum',
                'mounting': 'top_mount',
                'weight': 0.9,
                'acoustics': 0.9,
                'build_quality': 0.95,
                'price_tier': 0.9,
                'rgb_support': True,
                'angle': 7,
                'size': 'TKL'
            },
            'GMMK Pro': {
                'type': 'aluminum',
                'mounting': 'gasket_mount',
                'weight': 0.75,
                'acoustics': 0.75,
                'build_quality': 0.8,
                'price_tier': 0.7,
                'rgb_support': True,
                'angle': 6,
                'size': '75%'
            },
            'Keychron Q1': {
                'type': 'aluminum',
                'mounting': 'gasket_mount',
                'weight': 0.7,
                'acoustics': 0.8,
                'build_quality': 0.75,
                'price_tier': 0.6,
                'rgb_support': True,
                'angle': 6,
                'size': '75%'
            },
            'Bakeneko60': {
                'type': 'aluminum',
                'mounting': 'o_ring_mount',
                'weight': 0.6,
                'acoustics': 0.85,
                'build_quality': 0.7,
                'price_tier': 0.5,
                'rgb_support': False,
                'angle': 6,
                'size': '60%'
            },
            'NK65': {
                'type': 'aluminum',
                'mounting': 'gasket_mount',
                'weight': 0.65,
                'acoustics': 0.75,
                'build_quality': 0.75,
                'price_tier': 0.6,
                'rgb_support': True,
                'angle': 6,
                'size': '65%'
            },
            'Tofu84': {
                'type': 'aluminum',
                'mounting': 'tray_mount',
                'weight': 0.8,
                'acoustics': 0.7,
                'build_quality': 0.8,
                'price_tier': 0.65,
                'rgb_support': True,
                'angle': 6,
                'size': '75%'
            },
            'Frog TKL': {
                'type': 'aluminum',
                'mounting': 'top_mount',
                'weight': 0.85,
                'acoustics': 0.9,
                'build_quality': 0.9,
                'price_tier': 0.85,
                'rgb_support': False,
                'angle': 7,
                'size': 'TKL'
            },
            'D65': {
                'type': 'aluminum',
                'mounting': 'gasket_mount',
                'weight': 0.75,
                'acoustics': 0.8,
                'build_quality': 0.85,
                'price_tier': 0.7,
                'rgb_support': True,
                'angle': 6,
                'size': '65%'
            }
        }
        
        # 플레이트 데이터
        self.plate_data = {
            'Aluminum Plate': {
                'material': 'aluminum',
                'stiffness': 0.8,     # 강성 (0-1)
                'sound_profile': 0.6,  # 소리 특성 (0-1)
                'price_tier': 0.5,     # 가격대 (0-1)
                'weight': 0.7,         # 무게 (0-1)
                'flex': 0.3           # 유연성 (0-1)
            },
            'FR4 Plate': {
                'material': 'fr4',
                'stiffness': 0.6,
                'sound_profile': 0.7,
                'price_tier': 0.3,
                'weight': 0.4,
                'flex': 0.6
            },
            'POM Plate': {
                'material': 'pom',
                'stiffness': 0.4,
                'sound_profile': 0.8,
                'price_tier': 0.4,
                'weight': 0.3,
                'flex': 0.7
            },
            'Brass Plate': {
                'material': 'brass',
                'stiffness': 0.9,
                'sound_profile': 0.7,
                'price_tier': 0.7,
                'weight': 0.9,
                'flex': 0.2
            },
            'Carbon Fiber Plate': {
                'material': 'carbon_fiber',
                'stiffness': 0.7,
                'sound_profile': 0.6,
                'price_tier': 0.6,
                'weight': 0.4,
                'flex': 0.5
            },
            'Polycarbonate Plate': {
                'material': 'polycarbonate',
                'stiffness': 0.5,
                'sound_profile': 0.8,
                'price_tier': 0.4,
                'weight': 0.3,
                'flex': 0.7
            },
            'Steel Plate': {
                'material': 'steel',
                'stiffness': 0.95,
                'sound_profile': 0.5,
                'price_tier': 0.5,
                'weight': 0.8,
                'flex': 0.1
            },
            'Copper Plate': {
                'material': 'copper',
                'stiffness': 0.85,
                'sound_profile': 0.75,
                'price_tier': 0.8,
                'weight': 0.85,
                'flex': 0.25
            },
            'ABS Plate': {
                'material': 'abs',
                'stiffness': 0.4,
                'sound_profile': 0.6,
                'price_tier': 0.2,
                'weight': 0.2,
                'flex': 0.8
            },
            'PETG Plate': {
                'material': 'petg',
                'stiffness': 0.45,
                'sound_profile': 0.7,
                'price_tier': 0.3,
                'weight': 0.25,
                'flex': 0.75
            }
        }
        
        # PCB 데이터
        self.pcb_data = {
            'DZ60RGB V2': {
                'type': 'hotswap',
                'rgb_support': True,
                'qmk_via': True,      # QMK/VIA 지원
                'flex': 0.4,          # 기판 유연성 (0-1)
                'price_tier': 0.5,    # 가격대 (0-1)
                'build_quality': 0.7,  # 빌드 품질 (0-1)
                'features': ['rgb_per_key', 'usb_c', 'esd_protection']
            },
            'BM60RGB': {
                'type': 'hotswap',
                'rgb_support': True,
                'qmk_via': True,
                'flex': 0.5,
                'price_tier': 0.4,
                'build_quality': 0.6,
                'features': ['rgb_per_key', 'usb_c']
            },
            'KBD67 Rev2': {
                'type': 'solder',
                'rgb_support': False,
                'qmk_via': True,
                'flex': 0.6,
                'price_tier': 0.5,
                'build_quality': 0.8,
                'features': ['usb_c', 'esd_protection']
            },
            'Instant60': {
                'type': 'hotswap',
                'rgb_support': True,
                'qmk_via': True,
                'flex': 0.4,
                'price_tier': 0.6,
                'build_quality': 0.8,
                'features': ['rgb_underglow', 'usb_c', 'esd_protection']
            },
            'WT60-D': {
                'type': 'solder',
                'rgb_support': False,
                'qmk_via': True,
                'flex': 0.7,
                'price_tier': 0.7,
                'build_quality': 0.9,
                'features': ['usb_c', 'esd_protection', 'flex_cuts']
            },
            'H60': {
                'type': 'solder',
                'rgb_support': False,
                'qmk_via': True,
                'flex': 0.8,
                'price_tier': 0.6,
                'build_quality': 0.8,
                'features': ['usb_c', 'flex_cuts']
            },
            'NK65 PCB': {
                'type': 'hotswap',
                'rgb_support': True,
                'qmk_via': True,
                'flex': 0.3,
                'price_tier': 0.5,
                'build_quality': 0.7,
                'features': ['rgb_underglow', 'usb_c']
            },
            'Solder TKL PCB': {
                'type': 'solder',
                'rgb_support': False,
                'qmk_via': True,
                'flex': 0.5,
                'price_tier': 0.4,
                'build_quality': 0.7,
                'features': ['usb_c']
            },
            'HS60': {
                'type': 'hotswap',
                'rgb_support': True,
                'qmk_via': True,
                'flex': 0.4,
                'price_tier': 0.6,
                'build_quality': 0.8,
                'features': ['rgb_per_key', 'usb_c', 'esd_protection']
            },
            'KBD75 Rev2': {
                'type': 'solder',
                'rgb_support': True,
                'qmk_via': True,
                'flex': 0.5,
                'price_tier': 0.5,
                'build_quality': 0.7,
                'features': ['rgb_underglow', 'usb_c']
            }
        }
        
        # 스태빌라이저 데이터
        self.stabilizer_data = {
            'Durock V2': {
                'type': 'screw_in',
                'rattle': 0.2,        # 흔들림 (0-1, 낮을수록 좋음)
                'smoothness': 0.9,    # 부드러움 (0-1)
                'sound_profile': 0.8,  # 소리 특성 (0-1)
                'price_tier': 0.7,    # 가격대 (0-1)
                'build_quality': 0.9   # 빌드 품질 (0-1)
            },
            'Cherry Clip-in': {
                'type': 'clip_in',
                'rattle': 0.6,
                'smoothness': 0.6,
                'sound_profile': 0.5,
                'price_tier': 0.3,
                'build_quality': 0.6
            },
            'C³Equalz Screw-in': {
                'type': 'screw_in',
                'rattle': 0.25,
                'smoothness': 0.85,
                'sound_profile': 0.8,
                'price_tier': 0.6,
                'build_quality': 0.85
            },
            'Zeal Screw-in': {
                'type': 'screw_in',
                'rattle': 0.2,
                'smoothness': 0.9,
                'sound_profile': 0.85,
                'price_tier': 0.9,
                'build_quality': 0.95
            },
            'GMK Screw-in': {
                'type': 'screw_in',
                'rattle': 0.3,
                'smoothness': 0.8,
                'sound_profile': 0.75,
                'price_tier': 0.6,
                'build_quality': 0.8
            },
            'Everglide Panda': {
                'type': 'screw_in',
                'rattle': 0.25,
                'smoothness': 0.85,
                'sound_profile': 0.8,
                'price_tier': 0.65,
                'build_quality': 0.85
            },
            'GOAT Stabilizers': {
                'type': 'clip_in',
                'rattle': 0.4,
                'smoothness': 0.7,
                'sound_profile': 0.7,
                'price_tier': 0.5,
                'build_quality': 0.75
            },
            'NK Stabilizers': {
                'type': 'screw_in',
                'rattle': 0.3,
                'smoothness': 0.8,
                'sound_profile': 0.75,
                'price_tier': 0.5,
                'build_quality': 0.8
            },
            'Gateron Screw-in': {
                'type': 'screw_in',
                'rattle': 0.35,
                'smoothness': 0.75,
                'sound_profile': 0.7,
                'price_tier': 0.4,
                'build_quality': 0.75
            },
            'Durock Piano': {
                'type': 'screw_in',
                'rattle': 0.15,
                'smoothness': 0.95,
                'sound_profile': 0.9,
                'price_tier': 0.8,
                'build_quality': 0.95
            }
        }

    def add_switch(self, name: str, switch_type: str, **scores):
        """새로운 스위치 추가"""
        if switch_type not in ['linear', 'tactile', 'clicky']:
            raise ValueError("switch_type must be one of: 'linear', 'tactile', 'clicky'")
        
        required_scores = [
            'linearScore', 'tactileScore', 'soundScore',
            'weightScore', 'smoothnessScore', 'speedScore',
            'stabilityScore', 'durabilityScore'
        ]
        
        for score_name in required_scores:
            if score_name not in scores or not 0 <= scores[score_name] <= 1:
                raise ValueError(f"{score_name} must be between 0 and 1")
        
        self.switch_data[name] = {'type': switch_type, **scores}
        print(f"스위치 '{name}'가 성공적으로 추가되었습니다.")

    def add_keycap(
        self,
        name: str,
        material: str,
        profile: str,
        thickness: float,
        sound_profile: float,
        build_quality: float,
        price_tier: float,
        rgb_compatible: bool,
        durability: float
    ):
        """새로운 키캡 추가"""
        if material not in ['abs', 'pbt']:
            raise ValueError("material must be one of: 'abs', 'pbt'")
        
        if profile not in ['cherry', 'sa', 'dsa', 'xda', 'mt3', 'kat']:
            raise ValueError("Invalid profile type")
        
        if not all(0 <= x <= 1 for x in [thickness, sound_profile, build_quality, price_tier, durability]):
            raise ValueError("All scores must be between 0 and 1")
        
        self.keycap_data[name] = {
            'material': material,
            'profile': profile,
            'thickness': thickness,
            'sound_profile': sound_profile,
            'build_quality': build_quality,
            'price_tier': price_tier,
            'rgb_compatible': rgb_compatible,
            'durability': durability
        }
        print(f"키캡 '{name}'이 성공적으로 추가되었습니다.")

    def add_foam(
        self,
        name: str,
        foam_type: str,
        thickness: float,
        density: float,
        sound_dampening: float,
        compression: float,
        durability: float,
        price_tier: float
    ):
        """새로운 폼 추가"""
        if not all(0 <= x <= 1 for x in [thickness, density, sound_dampening, compression, durability, price_tier]):
            raise ValueError("All scores must be between 0 and 1")
        
        self.foam_data[name] = {
            'type': foam_type,
            'thickness': thickness,
            'density': density,
            'sound_dampening': sound_dampening,
            'compression': compression,
            'durability': durability,
            'price_tier': price_tier
        }
        print(f"폼 '{name}'이 성공적으로 추가되었습니다.")

    def add_gasket(
        self,
        name: str,
        material: str,
        thickness: float,
        flexibility: float,
        dampening: float,
        durability: float,
        price_tier: float
    ):
        """새로운 가스켓 추가"""
        if not all(0 <= x <= 1 for x in [thickness, flexibility, dampening, durability, price_tier]):
            raise ValueError("All scores must be between 0 and 1")
        
        self.gasket_data[name] = {
            'material': material,
            'thickness': thickness,
            'flexibility': flexibility,
            'dampening': dampening,
            'durability': durability,
            'price_tier': price_tier
        }
        print(f"가스켓 '{name}'이 성공적으로 추가되었습니다.")

    def add_oring(
        self,
        name: str,
        material: str,
        hardness: float,
        dampening: float,
        durability: float,
        price_tier: float
    ):
        """새로운 O-링 추가"""
        if not all(0 <= x <= 1 for x in [hardness, dampening, durability, price_tier]):
            raise ValueError("All scores must be between 0 and 1")
        
        self.oring_data[name] = {
            'material': material,
            'hardness': hardness,
            'dampening': dampening,
            'durability': durability,
            'price_tier': price_tier
        }
        print(f"O-링 '{name}'이 성공적으로 추가되었습니다.")

    def add_cable(
        self,
        name: str,
        cable_type: str,
        connector: str,
        length: float,
        quality: float,
        flexibility: float,
        durability: float,
        price_tier: float,
        detachable: bool
    ):
        """새로운 케이블 추가"""
        if not all(0 <= x <= 1 for x in [quality, flexibility, durability, price_tier]):
            raise ValueError("All scores must be between 0 and 1")
        
        self.cable_data[name] = {
            'type': cable_type,
            'connector': connector,
            'length': length,
            'quality': quality,
            'flexibility': flexibility,
            'durability': durability,
            'price_tier': price_tier,
            'detachable': detachable
        }
        print(f"케이블 '{name}'이 성공적으로 추가되었습니다.")

    def list_components(self, component_type: str = None):
        """부품 목록 출력"""
        components = {
            'switch': ('스위치', self.switch_data),
            'housing': ('하우징', self.housing_data),
            'plate': ('플레이트', self.plate_data),
            'pcb': ('PCB', self.pcb_data),
            'stabilizer': ('스태빌라이저', self.stabilizer_data),
            'keycap': ('키캡', self.keycap_data),
            'foam': ('폼', self.foam_data),
            'gasket': ('가스켓', self.gasket_data),
            'oring': ('O-링', self.oring_data),
            'cable': ('케이블', self.cable_data)
        }
        
        if component_type in components:
            title, data = components[component_type]
            print(f"\n=== {title} 목록 ===")
            for name, specs in data.items():
                print(f"\n{name}:")
                for k, v in specs.items():
                    print(f"  {k}: {v}")
        else:
            print("\n사용 가능한 부품 종류:")
            for i, (key, (title, _)) in enumerate(components.items(), 1):
                print(f"{i}. {title} ({key})")

    def generate_dataset(self, output_dir: str = 'data'):
        """모든 부품 데이터셋 생성"""
        os.makedirs(output_dir, exist_ok=True)
        
        # 각 부품 데이터를 DataFrame으로 변환하고 저장
        components = {
            'switches': self.switch_data,
            'housing': self.housing_data,
            'plate': self.plate_data,
            'pcb': self.pcb_data,
            'stabilizer': self.stabilizer_data,
            'keycap': self.keycap_data,
            'foam': self.foam_data,
            'gasket': self.gasket_data,
            'oring': self.oring_data,
            'cable': self.cable_data
        }
        
        for name, data in components.items():
            # PCB features 리스트를 문자열로 변환
            if name == 'pcb':
                data_copy = {k: v.copy() for k, v in data.items()}
                for pcb in data_copy.values():
                    pcb['features'] = ','.join(pcb['features'])
                data = data_copy
            
            df = pd.DataFrame(data).T.reset_index().rename(columns={'index': 'name'})
            df.to_csv(f'{output_dir}/{name}.csv', index=False)
        
        print(f"모든 부품 데이터가 {output_dir} 디렉토리에 저장되었습니다.")

if __name__ == "__main__":
    # 테스트
    generator = KeyboardComponentGenerator()
    generator.list_components()
    generator.generate_dataset() 