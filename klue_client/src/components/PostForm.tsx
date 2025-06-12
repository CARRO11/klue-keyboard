import React, { useState, useEffect } from "react";
import { Post, PostRequest } from "../types/Post";
import { PostService } from "../services/postService";
import "./PostForm.css";

interface PostFormProps {
  post?: Post; // 수정 모드일 때 전달
  onSave: (post: Post) => void;
  onCancel: () => void;
}

const PostForm: React.FC<PostFormProps> = ({ post, onSave, onCancel }) => {
  const [formData, setFormData] = useState<PostRequest>({
    title: "",
    content: "",
    author: "",
    category: "",
    isPinned: false,
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState<string[]>([]);

  const isEditMode = !!post;

  // 폼 초기화
  useEffect(() => {
    if (post) {
      // 수정 모드: 기존 게시글 데이터로 초기화
      setFormData({
        title: post.title,
        content: post.content,
        author: post.author,
        category: post.category || "",
        isPinned: post.isPinned,
      });
    } else {
      // 작성 모드: 빈 폼으로 초기화
      setFormData({
        title: "",
        content: "",
        author: "",
        category: "",
        isPinned: false,
      });
    }
  }, [post]);

  // 카테고리 목록 로드
  useEffect(() => {
    const loadCategories = async () => {
      try {
        const categoryList = await PostService.getCategories();
        setCategories(categoryList);
      } catch (err) {
        console.error("카테고리 로드 실패:", err);
      }
    };
    loadCategories();
  }, []);

  // 입력값 변경 처리
  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value, type } = e.target;

    if (type === "checkbox") {
      const checkbox = e.target as HTMLInputElement;
      setFormData((prev) => ({
        ...prev,
        [name]: checkbox.checked,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        [name]: value,
      }));
    }

    // 에러 메시지 제거
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  // 폼 유효성 검사
  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.title.trim()) {
      newErrors.title = "제목을 입력해주세요.";
    } else if (formData.title.length > 200) {
      newErrors.title = "제목은 200자를 초과할 수 없습니다.";
    }

    if (!formData.content.trim()) {
      newErrors.content = "내용을 입력해주세요.";
    }

    if (!formData.author.trim()) {
      newErrors.author = "작성자를 입력해주세요.";
    } else if (formData.author.length > 100) {
      newErrors.author = "작성자는 100자를 초과할 수 없습니다.";
    }

    if (formData.category && formData.category.length > 100) {
      newErrors.category = "카테고리는 100자를 초과할 수 없습니다.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // 폼 제출
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      let savedPost: Post;

      if (isEditMode && post) {
        // 수정 모드
        savedPost = await PostService.updatePost(post.id, formData);
      } else {
        // 작성 모드
        savedPost = await PostService.createPost(formData);
      }

      onSave(savedPost);
    } catch (err) {
      alert(err instanceof Error ? err.message : "저장에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // 취소 확인
  const handleCancel = () => {
    if (
      window.confirm("작성 중인 내용이 사라집니다. 정말로 취소하시겠습니까?")
    ) {
      onCancel();
    }
  };

  return (
    <div className="post-form">
      <div className="post-form-header">
        <h2>{isEditMode ? "✏️ 게시글 수정" : "📝 새 게시글 작성"}</h2>
      </div>

      <form onSubmit={handleSubmit} className="post-form-content">
        {/* 제목 */}
        <div className="form-group">
          <label htmlFor="title" className="form-label">
            제목 <span className="required">*</span>
          </label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className={`form-input ${errors.title ? "error" : ""}`}
            placeholder="게시글 제목을 입력하세요"
            maxLength={200}
          />
          {errors.title && <div className="error-message">{errors.title}</div>}
          <div className="character-count">{formData.title.length}/200</div>
        </div>

        {/* 작성자 */}
        <div className="form-group">
          <label htmlFor="author" className="form-label">
            작성자 <span className="required">*</span>
          </label>
          <input
            type="text"
            id="author"
            name="author"
            value={formData.author}
            onChange={handleChange}
            className={`form-input ${errors.author ? "error" : ""}`}
            placeholder="작성자 이름을 입력하세요"
            maxLength={100}
            disabled={isEditMode} // 수정 모드에서는 작성자 변경 불가
          />
          {errors.author && (
            <div className="error-message">{errors.author}</div>
          )}
        </div>

        {/* 카테고리 */}
        <div className="form-group">
          <label htmlFor="category" className="form-label">
            카테고리
          </label>
          <div className="category-input-group">
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleChange}
              className="form-select"
            >
              <option value="">카테고리 선택</option>
              {categories.map((cat) => (
                <option key={cat} value={cat}>
                  {cat}
                </option>
              ))}
            </select>
            <input
              type="text"
              name="category"
              value={formData.category}
              onChange={handleChange}
              className={`form-input ${errors.category ? "error" : ""}`}
              placeholder="또는 새 카테고리 입력"
              maxLength={100}
            />
          </div>
          {errors.category && (
            <div className="error-message">{errors.category}</div>
          )}
        </div>

        {/* 내용 */}
        <div className="form-group">
          <label htmlFor="content" className="form-label">
            내용 <span className="required">*</span>
          </label>
          <textarea
            id="content"
            name="content"
            value={formData.content}
            onChange={handleChange}
            className={`form-textarea ${errors.content ? "error" : ""}`}
            placeholder="게시글 내용을 입력하세요"
            rows={15}
          />
          {errors.content && (
            <div className="error-message">{errors.content}</div>
          )}
          <div className="character-count">{formData.content.length}자</div>
        </div>

        {/* 고정 게시글 */}
        <div className="form-group">
          <label className="checkbox-label">
            <input
              type="checkbox"
              name="isPinned"
              checked={formData.isPinned}
              onChange={handleChange}
              className="form-checkbox"
            />
            📌 상단 고정 게시글로 설정
          </label>
        </div>

        {/* 버튼 */}
        <div className="form-actions">
          <button
            type="button"
            onClick={handleCancel}
            className="cancel-btn"
            disabled={loading}
          >
            취소
          </button>
          <button type="submit" className="submit-btn" disabled={loading}>
            {loading ? "저장 중..." : isEditMode ? "수정하기" : "작성하기"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default PostForm;
