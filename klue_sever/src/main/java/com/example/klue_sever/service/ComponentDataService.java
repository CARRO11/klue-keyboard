package com.example.klue_sever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class ComponentDataService {

    private static final Logger logger = LoggerFactory.getLogger(ComponentDataService.class);

    @Autowired
    private SwitchService switchService;
    
    @Autowired
    private KeycapService keycapService;
    
    @Autowired
    private PCBService pcbService;
    
    @Autowired
    private PlateService plateService;
    
    @Autowired
    private StabilizerService stabilizerService;
    
    @Autowired
    private GasketService gasketService;
    
    @Autowired
    private FoamService foamService;
    
    @Autowired
    private CableService cableService;

    /**
     * 모든 부품 데이터를 수집하여 OpenAI에 전달할 형태로 가공
     */
    public Map<String, Object> getAllComponentsForAI() {
        Map<String, Object> components = new HashMap<>();
        
        try {
            // 스위치 데이터
            List<Map<String, Object>> switches = new ArrayList<>();
            switchService.findAll().forEach(s -> {
                Map<String, Object> switchData = new HashMap<>();
                switchData.put("name", s.getName());
                switchData.put("type", s.getType());
                switchData.put("pressure", s.getPressure());
                switchData.put("lubrication", s.getLubrication());
                switchData.put("linear_score", s.getLinearScore());
                switchData.put("tactile_score", s.getTactileScore());
                switchData.put("sound_score", s.getSoundScore());
                switches.add(switchData);
            });
            components.put("switches", switches);
            logger.info("스위치 데이터 수집 완료: {}개", switches.size());

            // 키캡 데이터
            List<Map<String, Object>> keycaps = new ArrayList<>();
            keycapService.findAll().forEach(k -> {
                Map<String, Object> keycapData = new HashMap<>();
                keycapData.put("name", k.getName());
                keycapData.put("material", k.getMaterial());
                keycapData.put("profile", k.getProfile());
                keycapData.put("thickness", k.getThickness());
                keycapData.put("price_tier", k.getPriceTier());
                keycaps.add(keycapData);
            });
            components.put("keycaps", keycaps);
            logger.info("키캡 데이터 수집 완료: {}개", keycaps.size());

            // PCB 데이터
            List<Map<String, Object>> pcbs = new ArrayList<>();
            pcbService.findAll().forEach(p -> {
                Map<String, Object> pcbData = new HashMap<>();
                pcbData.put("name", p.getName());
                pcbData.put("layout", p.getLayout());
                pcbData.put("hotswap", p.getHotswap());
                pcbData.put("wireless", p.getWireless());
                pcbData.put("rgb", p.getRgb());
                pcbData.put("usb_type", p.getUsbType());
                pcbData.put("price_tier", p.getPriceTier());
                pcbs.add(pcbData);
            });
            components.put("pcbs", pcbs);
            logger.info("PCB 데이터 수집 완료: {}개", pcbs.size());

            // 플레이트 데이터
            List<Map<String, Object>> plates = new ArrayList<>();
            plateService.findAll().forEach(p -> {
                Map<String, Object> plateData = new HashMap<>();
                plateData.put("name", p.getName());
                plateData.put("material", p.getMaterial());
                plateData.put("thickness", p.getThickness());
                plateData.put("layout", p.getLayout());
                plateData.put("typing_feel", p.getTypingFeel());
                plates.add(plateData);
            });
            components.put("plates", plates);
            logger.info("플레이트 데이터 수집 완료: {}개", plates.size());

            // 스테빌라이저 데이터
            List<Map<String, Object>> stabilizers = new ArrayList<>();
            stabilizerService.findAll().forEach(s -> {
                Map<String, Object> stabData = new HashMap<>();
                stabData.put("name", s.getName());
                stabData.put("material", s.getMaterial());
                stabData.put("size", s.getSize());
                stabilizers.add(stabData);
            });
            components.put("stabilizers", stabilizers);
            logger.info("스테빌라이저 데이터 수집 완료: {}개", stabilizers.size());

            // 가스켓 데이터
            List<Map<String, Object>> gaskets = new ArrayList<>();
            gasketService.findAll().forEach(g -> {
                Map<String, Object> gasketData = new HashMap<>();
                gasketData.put("name", g.getName());
                gasketData.put("material", g.getMaterial());
                gasketData.put("typing", g.getTyping());
                gaskets.add(gasketData);
            });
            components.put("gaskets", gaskets);
            logger.info("가스켓 데이터 수집 완료: {}개", gaskets.size());

            // 폼 데이터
            List<Map<String, Object>> foams = new ArrayList<>();
            foamService.findAll().forEach(f -> {
                Map<String, Object> foamData = new HashMap<>();
                foamData.put("name", f.getName());
                foamData.put("material", f.getMaterial());
                foams.add(foamData);
            });
            components.put("foams", foams);
            logger.info("폼 데이터 수집 완료: {}개", foams.size());

            // 케이블 데이터
            List<Map<String, Object>> cables = new ArrayList<>();
            cableService.findAll().forEach(c -> {
                Map<String, Object> cableData = new HashMap<>();
                cableData.put("name", c.getName());
                cableData.put("material", c.getMaterial());
                cableData.put("length", c.getLength());
                cables.add(cableData);
            });
            components.put("cables", cables);
            logger.info("케이블 데이터 수집 완료: {}개", cables.size());

            logger.info("전체 부품 데이터 수집 완료 - 총 {}개 카테고리", components.size());
            
        } catch (Exception e) {
            logger.error("부품 데이터 수집 중 오류 발생: ", e);
            return new HashMap<>(); // 빈 맵 반환
        }

        return components;
    }

    /**
     * 특정 카테고리의 부품 수 반환
     */
    public Map<String, Integer> getComponentCounts() {
        Map<String, Integer> counts = new HashMap<>();
        
        try {
            counts.put("switches", (int) switchService.count());
            counts.put("keycaps", (int) keycapService.count());
            counts.put("pcbs", pcbService.findAll().size());
            counts.put("plates", plateService.findAll().size());
            counts.put("stabilizers", (int) stabilizerService.count());
            counts.put("gaskets", (int) gasketService.count());
            counts.put("foams", (int) foamService.count());
            counts.put("cables", (int) cableService.count());
            
            logger.info("부품 수 통계: {}", counts);
        } catch (Exception e) {
            logger.error("부품 수 조회 중 오류: ", e);
        }
        
        return counts;
    }
} 