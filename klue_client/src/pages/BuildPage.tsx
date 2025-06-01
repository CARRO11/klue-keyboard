import React, { useState, useEffect } from "react";
import styled from "@emotion/styled";
import {
  allPartsService,
  PartType,
  PART_RESPONSE_FIELDS,
} from "../services/allPartsService";

const PageContainer = styled.div`
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 0;
`;

const BuildContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
`;

const ComponentSection = styled.div`
  margin-bottom: 1rem;
`;

const ComponentLabel = styled.div`
  font-size: 1rem;
  font-weight: 500;
  color: #333;
  margin-bottom: 0.5rem;
  text-align: right;
  padding-right: 1rem;
  min-width: 200px;
`;

const ComponentRow = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
`;

const DropdownContainer = styled.div`
  position: relative;
  flex: 1;
`;

const DropdownButton = styled.button<{ $isOpen: boolean }>`
  width: 100%;
  padding: 1rem;
  background: white;
  border: 2px solid ${(props) => (props.$isOpen ? "#00bcd4" : "#e0e0e0")};
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  font-size: 1rem;
  color: #333;
  transition: all 0.2s ease;

  &:hover {
    border-color: #00bcd4;
  }
`;

const DropdownArrow = styled.span<{ $isOpen: boolean }>`
  transform: ${(props) => (props.$isOpen ? "rotate(180deg)" : "rotate(0deg)")};
  transition: transform 0.2s ease;
  color: #666;
`;

const DropdownMenu = styled.div<{ $isOpen: boolean }>`
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 2px solid #00bcd4;
  border-top: none;
  border-radius: 0 0 8px 8px;
  max-height: 300px;
  overflow-y: auto;
  z-index: 10;
  display: ${(props) => (props.$isOpen ? "block" : "none")};
`;

const DropdownItem = styled.div`
  padding: 0.75rem 1rem;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;

  &:hover {
    background-color: #f8f9fa;
  }

  &:last-child {
    border-bottom: none;
  }
`;

const ComponentName = styled.div`
  font-weight: 500;
  color: #333;
  margin-bottom: 0.25rem;
`;

const ComponentDetails = styled.div`
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 0.25rem;
`;

const ComponentLink = styled.a`
  font-size: 0.8rem;
  color: #00bcd4;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
`;

const LoadingText = styled.div`
  text-align: center;
  color: #666;
  padding: 2rem;
`;

const ErrorText = styled.div`
  text-align: center;
  color: #e74c3c;
  padding: 2rem;
`;

const MemoContainer = styled.div`
  position: relative;
  flex: 1;
`;

const MemoTextarea = styled.textarea`
  width: 100%;
  padding: 1rem;
  background: white;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  color: #333;
  resize: vertical;
  min-height: 60px;
  max-height: 200px;
  transition: all 0.2s ease;
  font-family: inherit;

  &:focus {
    outline: none;
    border-color: #00bcd4;
  }

  &::placeholder {
    color: #999;
  }

  &:hover {
    border-color: #00bcd4;
  }
`;

interface Component {
  id: number;
  name: string;
  link?: string;
  type?: string;
  material?: string;
  profile?: string;
  price_tier?: number;
  layout?: string;
  [key: string]: any;
}

interface ComponentCategory {
  id: string;
  label: string;
  placeholder: string;
  apiEndpoint: string;
  components: Component[];
}

interface MemoSection {
  id: string;
  label: string;
  placeholder: string;
  value: string;
}

const BuildPage = () => {
  const [openDropdown, setOpenDropdown] = useState<string | null>(null);
  const [selectedComponents, setSelectedComponents] = useState<
    Record<string, Component | null>
  >({});
  const [categories, setCategories] = useState<ComponentCategory[]>([]);
  const [memoSections, setMemoSections] = useState<MemoSection[]>([
    {
      id: "dampening",
      label: "완충재 / Dampening",
      placeholder: "폼, 테이프, 기타 완충재에 대한 메모를 입력하세요...",
      value: "",
    },
    {
      id: "others",
      label: "기타 / Others",
      placeholder: "기타 부품이나 특별한 요구사항을 입력하세요...",
      value: "",
    },
  ]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const initialCategories: Omit<ComponentCategory, "components">[] = [
    {
      id: "switches",
      label: "Switches 스위치",
      placeholder: "-",
      apiEndpoint: "switches",
    },
    {
      id: "keycaps",
      label: "Keycaps 키캡",
      placeholder: "-",
      apiEndpoint: "keycaps",
    },
    {
      id: "pcb",
      label: "PCB 기판",
      placeholder: "-",
      apiEndpoint: "pcbs",
    },
    {
      id: "plate",
      label: "Plate 보강판",
      placeholder: "-",
      apiEndpoint: "plates",
    },
    {
      id: "stabilizers",
      label: "Stabilizers 스테빌라이저",
      placeholder: "-",
      apiEndpoint: "stabilizers",
    },
  ];

  useEffect(() => {
    const fetchAllComponents = async () => {
      try {
        setLoading(true);
        const categoriesWithComponents = await Promise.all(
          initialCategories.map(async (category) => {
            try {
              const response = await allPartsService.getAll(
                category.apiEndpoint as PartType,
                { page: 0, size: 1000 }
              );

              const fieldName =
                PART_RESPONSE_FIELDS[category.apiEndpoint as PartType];
              const componentsData = (response as any)[fieldName] || [];

              return {
                ...category,
                components: componentsData,
              };
            } catch (err) {
              console.error(`Error fetching ${category.apiEndpoint}:`, err);
              return {
                ...category,
                components: [],
              };
            }
          })
        );

        setCategories(categoriesWithComponents);
        setError(null);
      } catch (err) {
        setError("부품 정보를 불러오는데 실패했습니다.");
        console.error("Error fetching components:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAllComponents();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleDropdownToggle = (categoryId: string) => {
    setOpenDropdown(openDropdown === categoryId ? null : categoryId);
  };

  const handleComponentSelect = (categoryId: string, component: Component) => {
    setSelectedComponents((prev) => ({
      ...prev,
      [categoryId]: component,
    }));
    setOpenDropdown(null);
  };

  const handleMemoChange = (sectionId: string, value: string) => {
    setMemoSections((prev) =>
      prev.map((section) =>
        section.id === sectionId ? { ...section, value } : section
      )
    );
  };

  const formatComponentDetails = (component: Component, categoryId: string) => {
    const details = [];

    if (categoryId === "switches" && component.type) {
      details.push(`타입: ${component.type}`);
    }
    if (categoryId === "keycaps" && component.material) {
      details.push(`재질: ${component.material}`);
    }
    if (categoryId === "keycaps" && component.profile) {
      details.push(`프로파일: ${component.profile}`);
    }
    if (
      (categoryId === "plate" || categoryId === "pcb") &&
      component.material
    ) {
      details.push(`재질: ${component.material}`);
    }
    if (categoryId === "pcb" && component.layout) {
      details.push(`레이아웃: ${component.layout}`);
    }
    if (categoryId === "stabilizers" && component.type) {
      details.push(`타입: ${component.type}`);
    }
    if (component.price_tier) {
      const priceTiers: Record<number, string> = {
        1: "엔트리급",
        2: "중급",
        3: "고급",
        4: "프리미엄",
      };
      details.push(`가격대: ${priceTiers[component.price_tier] || "미정"}`);
    }

    return details.join(" • ");
  };

  if (loading) {
    return (
      <PageContainer>
        <BuildContainer>
          <LoadingText>부품 정보를 불러오는 중...</LoadingText>
        </BuildContainer>
      </PageContainer>
    );
  }

  if (error) {
    return (
      <PageContainer>
        <BuildContainer>
          <ErrorText>{error}</ErrorText>
        </BuildContainer>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <BuildContainer>
        {categories.map((category) => (
          <ComponentSection key={category.id}>
            <ComponentRow>
              <ComponentLabel>{category.label}</ComponentLabel>
              <DropdownContainer>
                <DropdownButton
                  $isOpen={openDropdown === category.id}
                  onClick={() => handleDropdownToggle(category.id)}
                >
                  <span>
                    {selectedComponents[category.id]?.name ||
                      category.placeholder}
                  </span>
                  <DropdownArrow $isOpen={openDropdown === category.id}>
                    ▼
                  </DropdownArrow>
                </DropdownButton>
                <DropdownMenu $isOpen={openDropdown === category.id}>
                  {category.components.length > 0 ? (
                    category.components.map((component) => (
                      <DropdownItem
                        key={component.id}
                        onClick={() =>
                          handleComponentSelect(category.id, component)
                        }
                      >
                        <ComponentName>{component.name}</ComponentName>
                        {formatComponentDetails(component, category.id) && (
                          <ComponentDetails>
                            {formatComponentDetails(component, category.id)}
                          </ComponentDetails>
                        )}
                        {component.link && (
                          <ComponentLink
                            href={component.link}
                            target="_blank"
                            rel="noopener noreferrer"
                            onClick={(e) => e.stopPropagation()}
                          >
                            구매 링크 →
                          </ComponentLink>
                        )}
                      </DropdownItem>
                    ))
                  ) : (
                    <DropdownItem>
                      <ComponentDetails>부품 정보가 없습니다.</ComponentDetails>
                    </DropdownItem>
                  )}
                </DropdownMenu>
              </DropdownContainer>
            </ComponentRow>
          </ComponentSection>
        ))}

        {memoSections.map((section) => (
          <ComponentSection key={section.id}>
            <ComponentRow>
              <ComponentLabel>{section.label}</ComponentLabel>
              <MemoContainer>
                <MemoTextarea
                  value={section.value}
                  onChange={(e) => handleMemoChange(section.id, e.target.value)}
                  placeholder={section.placeholder}
                />
              </MemoContainer>
            </ComponentRow>
          </ComponentSection>
        ))}
      </BuildContainer>
    </PageContainer>
  );
};

export default BuildPage;
