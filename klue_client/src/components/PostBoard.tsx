import React, { useState, useEffect } from "react";
import { PostSummary, PostListResponse } from "../types/Post";
import { PostService } from "../services/postService";
import "./PostBoard.css";

interface PostBoardProps {
  onPostClick: (postId: number) => void;
  onCreatePost: () => void;
}

const PostBoard: React.FC<PostBoardProps> = ({ onPostClick, onCreatePost }) => {
  const [posts, setPosts] = useState<PostSummary[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [categories, setCategories] = useState<string[]>([]);

  const pageSize = 10;

  // 게시글 목록 로드
  const loadPosts = async (
    page: number = 0,
    keyword?: string,
    category?: string
  ) => {
    setLoading(true);
    setError(null);

    try {
      let response: PostListResponse;

      if (keyword && keyword.trim()) {
        response = await PostService.searchPosts(
          keyword.trim(),
          page,
          pageSize
        );
      } else if (category) {
        response = await PostService.getPostsByCategory(
          category,
          page,
          pageSize
        );
      } else {
        response = await PostService.getPosts(page, pageSize);
      }

      setPosts(response.posts);
      setCurrentPage(response.currentPage);
      setTotalPages(response.totalPages);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "게시글을 불러오는데 실패했습니다."
      );
    } finally {
      setLoading(false);
    }
  };

  // 카테고리 목록 로드
  const loadCategories = async () => {
    try {
      const categoryList = await PostService.getCategories();
      setCategories(categoryList);
    } catch (err) {
      console.error("카테고리 로드 실패:", err);
    }
  };

  // 컴포넌트 마운트 시 초기 로드
  useEffect(() => {
    loadPosts();
    loadCategories();
  }, []);

  // 검색 실행
  const handleSearch = () => {
    setCurrentPage(0);
    setSelectedCategory(null);
    loadPosts(0, searchKeyword);
  };

  // 카테고리 선택
  const handleCategorySelect = (category: string | null) => {
    setSelectedCategory(category);
    setSearchKeyword("");
    setCurrentPage(0);
    loadPosts(0, undefined, category || undefined);
  };

  // 페이지 변경
  const handlePageChange = (newPage: number) => {
    loadPosts(
      newPage,
      searchKeyword || undefined,
      selectedCategory || undefined
    );
  };

  // 날짜 포맷팅
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = now.getTime() - date.getTime();
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
      return date.toLocaleTimeString("ko-KR", {
        hour: "2-digit",
        minute: "2-digit",
      });
    } else if (diffDays < 7) {
      return `${diffDays}일 전`;
    } else {
      return date.toLocaleDateString("ko-KR");
    }
  };

  if (loading && posts.length === 0) {
    return <div className="post-board-loading">게시글을 불러오는 중...</div>;
  }

  return (
    <div className="post-board">
      <div className="post-board-header">
        <h2>🔥 New Products 게시판</h2>
        <button className="create-post-btn" onClick={onCreatePost}>
          ✏️ 새 글 작성
        </button>
      </div>

      {/* 검색 및 필터 */}
      <div className="post-board-controls">
        <div className="search-section">
          <input
            type="text"
            placeholder="제목 또는 내용으로 검색..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            onKeyPress={(e) => e.key === "Enter" && handleSearch()}
            className="search-input"
          />
          <button onClick={handleSearch} className="search-btn">
            🔍
          </button>
        </div>

        <div className="category-section">
          <button
            className={`category-btn ${selectedCategory === null ? "active" : ""}`}
            onClick={() => handleCategorySelect(null)}
          >
            전체
          </button>
          {categories.map((category) => (
            <button
              key={category}
              className={`category-btn ${selectedCategory === category ? "active" : ""}`}
              onClick={() => handleCategorySelect(category)}
            >
              {category}
            </button>
          ))}
        </div>
      </div>

      {/* 에러 메시지 */}
      {error && <div className="error-message">❌ {error}</div>}

      {/* 게시글 목록 */}
      <div className="post-list">
        {posts.length === 0 ? (
          <div className="no-posts">
            📝 게시글이 없습니다. 첫 번째 글을 작성해보세요!
          </div>
        ) : (
          posts.map((post) => (
            <div
              key={post.id}
              className={`post-item ${post.isPinned ? "pinned" : ""}`}
              onClick={() => onPostClick(post.id)}
            >
              <div className="post-item-header">
                <h3 className="post-title">
                  {post.isPinned && <span className="pin-icon">📌</span>}
                  {post.title}
                </h3>
                <div className="post-stats">
                  <span className="view-count">👀 {post.viewCount}</span>
                  <span className="like-count">❤️ {post.likeCount}</span>
                </div>
              </div>

              <div className="post-preview">{post.preview}</div>

              <div className="post-item-footer">
                <div className="post-meta">
                  <span className="author">✍️ {post.author}</span>
                  {post.category && (
                    <span className="category">🏷️ {post.category}</span>
                  )}
                </div>
                <span className="created-date">
                  🕒 {formatDate(post.createdAt)}
                </span>
              </div>
            </div>
          ))
        )}
      </div>

      {/* 페이지네이션 */}
      {totalPages > 1 && (
        <div className="pagination">
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 0}
            className="page-btn"
          >
            ◀️ 이전
          </button>

          <div className="page-numbers">
            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
              const pageNum =
                Math.max(0, Math.min(totalPages - 5, currentPage - 2)) + i;
              return (
                <button
                  key={pageNum}
                  onClick={() => handlePageChange(pageNum)}
                  className={`page-number ${currentPage === pageNum ? "active" : ""}`}
                >
                  {pageNum + 1}
                </button>
              );
            })}
          </div>

          <button
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages - 1}
            className="page-btn"
          >
            다음 ▶️
          </button>
        </div>
      )}

      {loading && <div className="loading-overlay">로딩 중...</div>}
    </div>
  );
};

export default PostBoard;
