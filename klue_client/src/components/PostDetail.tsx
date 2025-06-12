import React, { useState, useEffect } from "react";
import { Post } from "../types/Post";
import { PostService } from "../services/postService";
import "./PostDetail.css";

interface PostDetailProps {
  postId: number;
  onBack: () => void;
  onEdit: (post: Post) => void;
  onDelete: (postId: number) => void;
}

const PostDetail: React.FC<PostDetailProps> = ({
  postId,
  onBack,
  onEdit,
  onDelete,
}) => {
  const [post, setPost] = useState<Post | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [liking, setLiking] = useState(false);

  // 게시글 로드
  const loadPost = async () => {
    setLoading(true);
    setError(null);

    try {
      const postData = await PostService.getPostById(postId);
      setPost(postData);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "게시글을 불러오는데 실패했습니다."
      );
    } finally {
      setLoading(false);
    }
  };

  // 좋아요 처리
  const handleLike = async () => {
    if (!post || liking) return;

    setLiking(true);
    try {
      await PostService.likePost(post.id);
      // 좋아요 수 증가 (로컬 업데이트)
      setPost((prev) =>
        prev ? { ...prev, likeCount: prev.likeCount + 1 } : null
      );
    } catch (err) {
      alert(err instanceof Error ? err.message : "좋아요 처리에 실패했습니다.");
    } finally {
      setLiking(false);
    }
  };

  // 삭제 확인
  const handleDelete = () => {
    if (!post) return;

    if (window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
      onDelete(post.id);
    }
  };

  // 컴포넌트 마운트 시 게시글 로드
  useEffect(() => {
    loadPost();
  }, [postId]);

  // 날짜 포맷팅
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleString("ko-KR", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  // 내용을 HTML로 렌더링 (줄바꿈 처리)
  const formatContent = (content: string) => {
    return content.split("\n").map((line, index) => (
      <React.Fragment key={index}>
        {line}
        {index < content.split("\n").length - 1 && <br />}
      </React.Fragment>
    ));
  };

  if (loading) {
    return (
      <div className="post-detail-loading">
        <div className="loading-spinner">📰</div>
        <div>게시글을 불러오는 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="post-detail-error">
        <div className="error-icon">❌</div>
        <div className="error-text">{error}</div>
        <button onClick={onBack} className="back-btn">
          돌아가기
        </button>
      </div>
    );
  }

  if (!post) {
    return (
      <div className="post-detail-error">
        <div className="error-icon">📭</div>
        <div className="error-text">게시글을 찾을 수 없습니다.</div>
        <button onClick={onBack} className="back-btn">
          돌아가기
        </button>
      </div>
    );
  }

  return (
    <div className="post-detail">
      {/* 헤더 */}
      <div className="post-detail-header">
        <button onClick={onBack} className="back-btn">
          ◀️ 목록으로
        </button>
        <div className="post-actions">
          <button onClick={() => onEdit(post)} className="edit-btn">
            ✏️ 수정
          </button>
          <button onClick={handleDelete} className="delete-btn">
            🗑️ 삭제
          </button>
        </div>
      </div>

      {/* 게시글 내용 */}
      <article className="post-content">
        {/* 제목 및 메타 정보 */}
        <header className="post-header">
          <h1 className="post-title">
            {post.isPinned && <span className="pin-icon">📌</span>}
            {post.title}
          </h1>

          <div className="post-meta">
            <div className="post-info">
              <span className="author">✍️ {post.author}</span>
              {post.category && (
                <span className="category">🏷️ {post.category}</span>
              )}
              <span className="created-date">
                🕒 {formatDate(post.createdAt)}
              </span>
              {post.updatedAt !== post.createdAt && (
                <span className="updated-date">
                  (수정: {formatDate(post.updatedAt)})
                </span>
              )}
            </div>

            <div className="post-stats">
              <span className="view-count">👀 {post.viewCount}</span>
              <span className="like-count">❤️ {post.likeCount}</span>
            </div>
          </div>
        </header>

        {/* 본문 */}
        <div className="post-body">
          <div className="content">{formatContent(post.content)}</div>
        </div>

        {/* 좋아요 버튼 */}
        <div className="post-actions-bottom">
          <button
            onClick={handleLike}
            disabled={liking}
            className={`like-btn ${liking ? "liking" : ""}`}
          >
            {liking ? "💫" : "❤️"} 좋아요 {post.likeCount}
          </button>
        </div>
      </article>

      {/* 관련 게시글 또는 댓글 섹션을 여기에 추가할 수 있습니다 */}
      <footer className="post-footer">
        <div className="post-footer-actions">
          <button onClick={onBack} className="back-to-list-btn">
            📋 목록으로 돌아가기
          </button>
        </div>
      </footer>
    </div>
  );
};

export default PostDetail;
