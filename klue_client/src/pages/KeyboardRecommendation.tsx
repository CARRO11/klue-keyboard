import React, { useState } from "react";
import "./KeyboardRecommendation.css";

interface Preferences {
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

const KeyboardRecommendation: React.FC = () => {
  const [preferences, setPreferences] = useState<Preferences>({
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

  // 자연어 입력 상태
  const [naturalLanguageInput, setNaturalLanguageInput] = useState<string>("");
  const [showNaturalInput, setShowNaturalInput] = useState(true); // 기본값을 자연어 입력으로
  const [userRequest, setUserRequest] = useState<string>("");

  // 기본 시스템 프롬프트 (UI 없이 백그라운드에서만 사용)
  const systemPrompt =
    "당신은 키보드 전문가 Tony입니다. 친근하고 전문적인 톤으로 사용자에게 맞는 키보드를 추천해주세요. 각 부품의 특성과 장단점을 자세히 설명하고, 사용자의 용도에 맞는 이유를 명확히 제시해주세요.";

  const handlePreferenceChange = (key: keyof Preferences, value: any) => {
    setPreferences((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const getRecommendations = async () => {
    setLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/recommend", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          ...preferences,
          switch_type: "Linear", // 기본값으로 리니어 설정
        }),
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
        "http://localhost:8080/api/recommend/natural",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            message: naturalLanguageInput,
            systemPrompt: systemPrompt,
          }),
        }
      );

      const data = await response.json();

      if (data.success) {
        setRecommendations(data.recommendations);
        setAiExplanation(data.ai_explanation);
        setUserRequest(data.user_request);
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
              링크
            </a>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="keyboard-recommendation">
      <div className="container">
        <h1>키보드 소믈리에 Tony</h1>

        {/* 자연어 입력 섹션 */}
        <div className="natural-language-section">
          <div className="header-section">
            <h2 className="section-title">어떤 키보드가 필요하신가요?</h2>
            <button
              onClick={() => setShowNaturalInput(false)}
              className={`settings-icon ${!showNaturalInput ? "active" : ""}`}
              title="상세 설정"
            >
              ⚙️
            </button>
          </div>

          {showNaturalInput ? (
            <div className="natural-input-area">
              <div className="input-examples">
                <p>
                  <strong>💡 Tony에게 이렇게 말해보세요:</strong>
                </p>
                <div className="example-tags">
                  <span
                    onClick={() =>
                      setNaturalLanguageInput("조용한 사무용 키보드 추천해줘")
                    }
                  >
                    "조용한 사무용 키보드"
                  </span>
                  <span
                    onClick={() =>
                      setNaturalLanguageInput("게이밍용 RGB 키보드 만들고 싶어")
                    }
                  >
                    "게이밍용 RGB 키보드"
                  </span>
                  <span
                    onClick={() =>
                      setNaturalLanguageInput("프리미엄 타이핑용 키보드")
                    }
                  >
                    "프리미엄 타이핑용"
                  </span>
                  <span
                    onClick={() =>
                      setNaturalLanguageInput("집에서 쓸 편안한 키보드")
                    }
                  >
                    "집에서 쓸 편안한"
                  </span>
                </div>
              </div>

              <textarea
                value={naturalLanguageInput}
                onChange={(e) => setNaturalLanguageInput(e.target.value)}
                placeholder="Tony에게 편하게 말씀해주세요! 
예: '회사에서 쓸 조용한 키보드', '게임할 때 쓸 반응 빠른 키보드', '예쁜 키보드 만들고 싶어'"
                className="natural-textarea"
                rows={4}
              />
              <button
                onClick={getNaturalLanguageRecommendations}
                disabled={loading || !naturalLanguageInput.trim()}
                className="natural-recommend-button"
              >
                {loading ? "🤖 Tony가 분석 중..." : "🎯 Tony에게 추천 받기"}
              </button>
            </div>
          ) : (
            <div className="preferences-section">
              <div className="back-button-container">
                <button
                  onClick={() => setShowNaturalInput(true)}
                  className="back-button"
                >
                  ← 돌아가기
                </button>
              </div>

              <p className="section-intro">
                Tony와 함께 각 항목을 조절해서 완벽한 키보드를 만들어보세요
              </p>

              <div className="preference-group">
                <label>🔊 소리 선호도: {preferences.sound_profile}/10</label>
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
                  <span>🤫 조용한</span>
                  <span>🎵 경쾌한</span>
                </div>
              </div>

              <div className="preference-group">
                <label>✋ 타건감: {preferences.tactile_score}/10</label>
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
                  <span>🥺 부드러운</span>
                  <span>💪 강한 촉감</span>
                </div>
              </div>

              <div className="preference-group">
                <label>⚡ 반응속도: {preferences.speed_score}/10</label>
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
                  <span>🐌 여유로운</span>
                  <span>🚀 빠른</span>
                </div>
              </div>

              <div className="preference-group">
                <label>💰 예산</label>
                <select
                  value={preferences.price_tier}
                  onChange={(e) =>
                    handlePreferenceChange(
                      "price_tier",
                      parseInt(e.target.value)
                    )
                  }
                >
                  <option value={1}>💸 저렴하게 (10만원대)</option>
                  <option value={2}>💳 적당하게 (20-30만원대)</option>
                  <option value={3}>💎 좋은 걸로 (40-50만원대)</option>
                  <option value={4}>👑 최고급으로 (60만원 이상)</option>
                </select>
              </div>

              <div className="preference-group checkbox-group">
                <label>
                  <input
                    type="checkbox"
                    checked={preferences.rgb_compatible}
                    onChange={(e) =>
                      handlePreferenceChange("rgb_compatible", e.target.checked)
                    }
                  />
                  <span>🌈 예쁜 RGB 조명 원해요!</span>
                </label>
              </div>

              <button
                onClick={getRecommendations}
                disabled={loading}
                className="recommend-button"
              >
                {loading ? "🤖 추천 생성 중..." : "🎯 내 키보드 추천받기"}
              </button>
            </div>
          )}

          {userRequest && (
            <div className="user-request-display">
              <h3>📝 요청 내용</h3>
              <p>"{userRequest}"</p>
            </div>
          )}
        </div>

        {/* 추천 결과 */}
        {recommendations && (
          <div className="recommendations-section">
            <h2>🎯 추천 결과</h2>

            {Object.entries(recommendations).map(([category, components]) => (
              <div key={category} className="category-section">
                <h3>
                  {category === "switches" && "🔧 스위치 (키보드의 심장)"}
                  {category === "plate" && "🛡️ 플레이트 (단단함을 책임져요)"}
                  {category === "stabilizers" &&
                    "⚖️ 스태빌라이저 (키가 흔들리지 않게)"}
                  {category === "keycaps" && "🎨 키캡 (예쁜 외관)"}
                  {category === "pcb" && "🖱️ PCB (전자회로 기판)"}
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
            <h2>🎩 Tony의 전문가 추천</h2>
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
