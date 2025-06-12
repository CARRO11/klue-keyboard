import React, { useState } from "react";
import styled from "@emotion/styled";

const PageContainer = styled.div`
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 2rem;
`;

const Container = styled.div`
  max-width: 800px;
  margin: 0 auto;
`;

const Header = styled.div`
  text-align: center;
  margin-bottom: 3rem;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 1rem;
  font-weight: 700;
`;

const Subtitle = styled.p`
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
`;

const FormContainer = styled.div`
  background: white;
  border-radius: 12px;
  padding: 2.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
`;

const FormGroup = styled.div`
  margin-bottom: 2rem;
`;

const Label = styled.label`
  display: block;
  font-size: 1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
`;

const Input = styled.input`
  width: 100%;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #ffd700;
  }
`;

const TextArea = styled.textarea`
  width: 100%;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  min-height: 120px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #ffd700;
  }
`;

const FileUploadContainer = styled.div`
  position: relative;
`;

const FileInput = styled.input`
  display: none;
`;

const FileUploadButton = styled.label`
  display: inline-block;
  width: 100%;
  padding: 1rem;
  border: 2px dashed #e0e0e0;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s ease;
  background-color: #f8f9fa;

  &:hover {
    border-color: #ffd700;
    background-color: #fff;
  }
`;

const FileUploadText = styled.div`
  color: #666;
  font-size: 1rem;
`;

const FilePreview = styled.div`
  margin-top: 1rem;
  padding: 0.5rem;
  background-color: #f0f0f0;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #333;
`;

const CategoryContainer = styled.div`
  position: relative;
`;

const CategoryButton = styled.button<{ $isOpen: boolean }>`
  width: 100%;
  padding: 1rem;
  background: white;
  border: 2px solid ${(props) => (props.$isOpen ? "#ffd700" : "#e0e0e0")};
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  font-size: 1rem;
  color: #333;
  transition: all 0.2s ease;

  &:hover {
    border-color: #ffd700;
  }
`;

const CategoryArrow = styled.span<{ $isOpen: boolean }>`
  transform: ${(props) => (props.$isOpen ? "rotate(180deg)" : "rotate(0deg)")};
  transition: transform 0.2s ease;
  color: #666;
`;

const CategoryDropdown = styled.div<{ $isOpen: boolean }>`
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 2px solid #ffd700;
  border-top: none;
  border-radius: 0 0 8px 8px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 10;
  display: ${(props) => (props.$isOpen ? "block" : "none")};
`;

const CategoryItem = styled.div`
  padding: 0.75rem 1rem;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: #f8f9fa;
  }

  &:last-child {
    border-bottom: none;
  }
`;

const SubmitButton = styled.button`
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  color: #333;
  border: none;
  padding: 1rem 2rem;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  width: 100%;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 215, 0, 0.3);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(255, 215, 0, 0.4);
  }

  &:active {
    transform: translateY(0);
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
  }
`;

const SuccessMessage = styled.div`
  background: #d4edda;
  color: #155724;
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid #c3e6cb;
  margin-bottom: 2rem;
  text-align: center;
`;

const InfoBox = styled.div`
  background: #e7f3ff;
  border: 1px solid #b3d7ff;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
`;

const InfoTitle = styled.h3`
  color: #0066cc;
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
`;

const InfoText = styled.p`
  color: #004499;
  margin: 0;
  line-height: 1.5;
`;

const RequestPage = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    screenshot: null as File | null,
    requirements: "",
    workshop: "",
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isWorkshopOpen, setIsWorkshopOpen] = useState(false);

  const workshops = [
    {
      id: "external",
      name: "외부공방",
      description: "고객님이 직접 선택한 외부 공방",
    },
    {
      id: "partner",
      name: "제휴공방",
      description: "KLUE와 제휴된 신뢰할 수 있는 공방",
    },
    {
      id: "klue",
      name: "KLUE",
      description: "KLUE 전문 조립 서비스",
    },
  ];

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    setFormData((prev) => ({
      ...prev,
      screenshot: file,
    }));
  };

  const handleWorkshopSelect = (workshop: string) => {
    setFormData((prev) => ({
      ...prev,
      workshop,
    }));
    setIsWorkshopOpen(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    // 실제 API 호출 시뮬레이션 (2초 후 성공)
    setTimeout(() => {
      setIsSubmitting(false);
      setIsSubmitted(true);
      // 3초 후 폼 리셋
      setTimeout(() => {
        setIsSubmitted(false);
        setFormData({
          name: "",
          email: "",
          screenshot: null,
          requirements: "",
          workshop: "",
        });
      }, 3000);
    }, 2000);
  };

  if (isSubmitted) {
    return (
      <PageContainer>
        <Container>
          <SuccessMessage>
            🎉 조립 의뢰가 성공적으로 접수되었습니다! <br />
            선택하신 공방에서 검토 후 이메일로 연락드리겠습니다.
          </SuccessMessage>
        </Container>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <Container>
        <Header>
          <Title>🔧 키보드 조립 대행 서비스</Title>
          <Subtitle>
            부품을 가져오시면 전문 공방에서 <br />
            완벽한 키보드로 조립해 드립니다!
          </Subtitle>
        </Header>

        <InfoBox>
          <InfoTitle>📋 서비스 안내</InfoTitle>
          <InfoText>
            • 고객님이 구매하신 키보드 부품들을 전문가가 조립해 드립니다
            <br />
            • 부품 상태와 요청사항을 스크린샷으로 첨부해 주세요
            <br />
            • 조립 방식과 특별 요청사항을 상세히 적어주시면 더 좋은 결과를 얻을
            수 있습니다
            <br />• 공방별로 전문분야와 소요시간이 다르니 신중히 선택해 주세요
          </InfoText>
        </InfoBox>

        <FormContainer>
          <form onSubmit={handleSubmit}>
            <FormGroup>
              <Label htmlFor="name">이름 *</Label>
              <Input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
                placeholder="성함을 입력해주세요"
              />
            </FormGroup>

            <FormGroup>
              <Label htmlFor="email">이메일 *</Label>
              <Input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
                placeholder="연락받을 이메일을 입력해주세요"
              />
            </FormGroup>

            <FormGroup>
              <Label htmlFor="screenshot">부품 사진 *</Label>
              <FileUploadContainer>
                <FileInput
                  type="file"
                  id="screenshot"
                  accept="image/*"
                  onChange={handleFileChange}
                />
                <FileUploadButton htmlFor="screenshot">
                  <FileUploadText>
                    📷 부품 사진을 첨부해주세요 (클릭하여 선택)
                  </FileUploadText>
                </FileUploadButton>
                {formData.screenshot && (
                  <FilePreview>
                    ✅ 선택된 파일: {formData.screenshot.name}
                  </FilePreview>
                )}
              </FileUploadContainer>
            </FormGroup>

            <FormGroup>
              <Label htmlFor="workshop">공방 선택 *</Label>
              <CategoryContainer>
                <CategoryButton
                  type="button"
                  $isOpen={isWorkshopOpen}
                  onClick={() => setIsWorkshopOpen(!isWorkshopOpen)}
                >
                  <span>
                    {formData.workshop
                      ? workshops.find((w) => w.id === formData.workshop)?.name
                      : "공방을 선택해주세요"}
                  </span>
                  <CategoryArrow $isOpen={isWorkshopOpen}>▼</CategoryArrow>
                </CategoryButton>
                <CategoryDropdown $isOpen={isWorkshopOpen}>
                  {workshops.map((workshop) => (
                    <CategoryItem
                      key={workshop.id}
                      onClick={() => handleWorkshopSelect(workshop.id)}
                    >
                      <div
                        style={{ fontWeight: "600", marginBottom: "0.25rem" }}
                      >
                        {workshop.name}
                      </div>
                      <div style={{ fontSize: "0.85rem", color: "#666" }}>
                        {workshop.description}
                      </div>
                    </CategoryItem>
                  ))}
                </CategoryDropdown>
              </CategoryContainer>
            </FormGroup>

            <FormGroup>
              <Label htmlFor="requirements">조립 요청사항 *</Label>
              <TextArea
                id="requirements"
                name="requirements"
                value={formData.requirements}
                onChange={handleInputChange}
                required
                placeholder="조립 방식, 특별 요청사항, 주의사항 등을 자세히 적어주세요&#10;예: 스위치 윤활 필요, 스테빌라이저 밴드에이드 모드, 폼 타입 선호 등"
              />
            </FormGroup>

            <SubmitButton
              type="submit"
              disabled={
                isSubmitting ||
                !formData.name ||
                !formData.email ||
                !formData.screenshot ||
                !formData.workshop ||
                !formData.requirements
              }
            >
              {isSubmitting ? "제출 중..." : "조립 의뢰하기"}
            </SubmitButton>
          </form>
        </FormContainer>
      </Container>
    </PageContainer>
  );
};

export default RequestPage;
