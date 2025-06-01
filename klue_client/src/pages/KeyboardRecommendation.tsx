import React, { useState, useEffect } from "react";
import "./KeyboardRecommendation.css";

interface Preferences {
  switch_type: string;
  sound_profile: number;
  tactile_score: number;
  speed_score: number;
  price_tier: number;
  build_quality: number;
  rgb_compatible: boolean;
}

interface Component {
  name: string;
  type?: string;
  material?: string;
  profile?: string;
  price_tier: number;
  link: string;
  [key: string]: any;
}

interface Recommendations {
  switches: Component[];
  plate: Component[];
  stabilizers: Component[];
  keycaps: Component[];
  pcb: Component[];
}

interface Template {
  name: string;
  description: string;
  preferences: Preferences;
}

const KeyboardRecommendation: React.FC = () => {
  const [preferences, setPreferences] = useState<Preferences>({
    switch_type: "Linear",
    sound_profile: 5,
    tactile_score: 5,
    speed_score: 5,
    price_tier: 2,
    build_quality: 8,
    rgb_compatible: true,
  });

  const [recommendations, setRecommendations] =
    useState<Recommendations | null>(null);
  const [aiExplanation, setAiExplanation] = useState<string>("");
  const [loading, setLoading] = useState(false);
  const [templates, setTemplates] = useState<{ [key: string]: Template }>({});
  const [selectedTemplate, setSelectedTemplate] = useState<string>("");

  // 자연어 입력 상태 추가
  const [naturalLanguageInput, setNaturalLanguageInput] = useState<string>("");
  const [showNaturalInput, setShowNaturalInput] = useState(false);
  const [userRequest, setUserRequest] = useState<string>("");

  // 템플릿 로드
  useEffect(() => {
    fetchTemplates();
  }, []);

  const fetchTemplates = async () => {
    try {
      const response = await fetch(
        "http://localhost:5002/api/preferences/templates"
      );
      const data = await response.json();
      if (data.success) {
        setTemplates(data.templates);
      }
    } catch (error) {
      console.error("템플릿 로드 실패:", error);
    }
  };

  const handleTemplateChange = (templateKey: string) => {
    if (templateKey && templates[templateKey]) {
      setSelectedTemplate(templateKey);
      setPreferences(templates[templateKey].preferences);
    }
  };

  const handlePreferenceChange = (key: keyof Preferences, value: any) => {
    setPreferences((prev) => ({
      ...prev,
      [key]: value,
    }));
    setSelectedTemplate(""); // 커스텀 설정 시 템플릿 선택 해제
  };

  const getRecommendations = async () => {
    setLoading(true);
    try {
      const response = await fetch("http://localhost:5002/api/recommend", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(preferences),
      });

      const data = await response.json();

      if (data.success) {
        setRecommendations(data.recommendations);
        setAiExplanation(data.ai_explanation);
      } else {
        alert("추천 생성 실패: " + data.message);
      }
    } catch (error) {
      console.error("API 호출 실패:", error);
      alert("서버 연결에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const getNaturalLanguageRecommendations = async () => {
    if (!naturalLanguageInput.trim()) {
      alert("요청 내용을 입력해주세요.");
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(
        "http://localhost:5002/api/recommend/natural",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ message: naturalLanguageInput }),
        }
      );

      const data = await response.json();

      if (data.success) {
        setRecommendations(data.recommendations);
        setAiExplanation(data.ai_explanation);
        setPreferences(data.interpreted_preferences);
        setUserRequest(data.user_request);
        setShowNaturalInput(false); // 성공 후 자연어 입력창 숨기기
      } else {
        alert("추천 생성 실패: " + data.message);
      }
    } catch (error) {
      console.error("자연어 API 호출 실패:", error);
      alert("서버 연결에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const renderComponent = (component: Component, category: string) => {
    const getPriceText = (tier: number) => {
      const prices = { 1: "엔트리급", 2: "중급", 3: "고급", 4: "프리미엄" };
      return prices[tier as keyof typeof prices] || "미정";
    };

    return (
      <div key={component.name} className="component-card">
        <h4>{component.name}</h4>
        <div className="component-details">
          {component.type && (
            <p>
              <strong>타입:</strong> {component.type}
            </p>
          )}
          {component.material && (
            <p>
              <strong>재질:</strong> {component.material}
            </p>
          )}
          {component.profile && (
            <p>
              <strong>프로파일:</strong> {component.profile}
            </p>
          )}
          <p>
            <strong>가격대:</strong> {getPriceText(component.price_tier)}
          </p>

          {/* 카테고리별 특성 표시 */}
          {category === "switches" && (
            <div className="specs">
              <span>소리: {component.sound_score}/10</span>
              <span>부드러움: {component.smoothness_score}/10</span>
              <span>속도: {component.speed_score}/10</span>
            </div>
          )}

          {component.link && (
            <a
              href={component.link}
              target="_blank"
              rel="noopener noreferrer"
              className="buy-link"
            >
              구매하기
            </a>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="keyboard-recommendation">
      <div className="container">
        <h1>🎯 키보드 부품 추천 시스템</h1>

        {/* 자연어 입력 섹션 */}
        <div className="natural-language-section">
          <h2>💬 자연어로 요청하기</h2>
          <div className="natural-input-toggle">
            <button
              onClick={() => setShowNaturalInput(!showNaturalInput)}
              className="toggle-button"
            >
              {showNaturalInput ? "상세 설정으로 전환" : "간단히 말로 요청하기"}
            </button>
          </div>

          {showNaturalInput && (
            <div className="natural-input-area">
              <textarea
                value={naturalLanguageInput}
                onChange={(e) => setNaturalLanguageInput(e.target.value)}
                placeholder="예: '조용한 커스텀키보드 부품 추천해줘', '게이밍용 고급 키보드 만들고 싶어', '사무실에서 쓸 조용한 키보드'"
                className="natural-textarea"
                rows={3}
              />
              <button
                onClick={getNaturalLanguageRecommendations}
                disabled={loading || !naturalLanguageInput.trim()}
                className="natural-recommend-button"
              >
                {loading ? "AI가 분석 중..." : "AI에게 추천 요청하기"}
              </button>
            </div>
          )}

          {userRequest && (
            <div className="user-request-display">
              <p>
                <strong>요청:</strong> "{userRequest}"
              </p>
            </div>
          )}
        </div>

        {/* 템플릿 선택 */}
        {!showNaturalInput && (
          <div className="template-section">
            <h2>빠른 설정</h2>
            <select
              value={selectedTemplate}
              onChange={(e) => handleTemplateChange(e.target.value)}
              className="template-select"
            >
              <option value="">커스텀 설정</option>
              {Object.entries(templates).map(([key, template]) => (
                <option key={key} value={key}>
                  {template.name} - {template.description}
                </option>
              ))}
            </select>
          </div>
        )}

        {/* 선호도 설정 */}
        {!showNaturalInput && (
          <div className="preferences-section">
            <h2>선호도 설정</h2>

            <div className="preference-group">
              <label>스위치 타입</label>
              <select
                value={preferences.switch_type}
                onChange={(e) =>
                  handlePreferenceChange("switch_type", e.target.value)
                }
              >
                <option value="Linear">리니어 (부드러운)</option>
                <option value="Tactile">택타일 (촉감)</option>
                <option value="Clicky">클릭키 (소리)</option>
              </select>
            </div>

            <div className="preference-group">
              <label>소리 선호도: {preferences.sound_profile}/10</label>
              <input
                type="range"
                min="1"
                max="10"
                value={preferences.sound_profile}
                onChange={(e) =>
                  handlePreferenceChange(
                    "sound_profile",
                    parseInt(e.target.value)
                  )
                }
              />
              <div className="range-labels">
                <span>조용한</span>
                <span>경쾌한</span>
              </div>
            </div>

            <div className="preference-group">
              <label>촉감 선호도: {preferences.tactile_score}/10</label>
              <input
                type="range"
                min="1"
                max="10"
                value={preferences.tactile_score}
                onChange={(e) =>
                  handlePreferenceChange(
                    "tactile_score",
                    parseInt(e.target.value)
                  )
                }
              />
              <div className="range-labels">
                <span>부드러운</span>
                <span>강한 촉감</span>
              </div>
            </div>

            <div className="preference-group">
              <label>속도감: {preferences.speed_score}/10</label>
              <input
                type="range"
                min="1"
                max="10"
                value={preferences.speed_score}
                onChange={(e) =>
                  handlePreferenceChange(
                    "speed_score",
                    parseInt(e.target.value)
                  )
                }
              />
              <div className="range-labels">
                <span>여유로운</span>
                <span>빠른</span>
              </div>
            </div>

            <div className="preference-group">
              <label>가격대</label>
              <select
                value={preferences.price_tier}
                onChange={(e) =>
                  handlePreferenceChange("price_tier", parseInt(e.target.value))
                }
              >
                <option value={1}>엔트리급 (저가)</option>
                <option value={2}>중급</option>
                <option value={3}>고급</option>
                <option value={4}>프리미엄</option>
              </select>
            </div>

            <div className="preference-group">
              <label>
                <input
                  type="checkbox"
                  checked={preferences.rgb_compatible}
                  onChange={(e) =>
                    handlePreferenceChange("rgb_compatible", e.target.checked)
                  }
                />
                RGB 조명 지원
              </label>
            </div>

            <button
              onClick={getRecommendations}
              disabled={loading}
              className="recommend-button"
            >
              {loading ? "추천 생성 중..." : "추천 받기"}
            </button>
          </div>
        )}

        {/* 추천 결과 */}
        {recommendations && (
          <div className="recommendations-section">
            <h2>🎯 추천 결과</h2>

            {Object.entries(recommendations).map(([category, components]) => (
              <div key={category} className="category-section">
                <h3>
                  {category === "switches" && "🔧 스위치"}
                  {category === "plate" && "🔧 플레이트"}
                  {category === "stabilizers" && "🔧 스태빌라이저"}
                  {category === "keycaps" && "🔧 키캡"}
                  {category === "pcb" && "🔧 PCB"}
                </h3>
                <div className="components-grid">
                  {components
                    .slice(0, 3)
                    .map((component: Component) =>
                      renderComponent(component, category)
                    )}
                </div>
              </div>
            ))}
          </div>
        )}

        {/* AI 설명 */}
        {aiExplanation && (
          <div className="ai-explanation-section">
            <h2>🤖 AI 추천 설명</h2>
            <div className="ai-explanation">
              {aiExplanation.split("\n").map((line, index) => (
                <p key={index}>{line}</p>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default KeyboardRecommendation;
