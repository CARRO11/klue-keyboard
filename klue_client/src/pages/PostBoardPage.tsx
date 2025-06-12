import React, { useState } from "react";
import { Post } from "../types/Post";
import { PostService } from "../services/postService";
import PostBoard from "../components/PostBoard";
import PostDetail from "../components/PostDetail";
import PostForm from "../components/PostForm";
import "./PostBoardPage.css";

type PageMode = "list" | "detail" | "create" | "edit";

const PostBoardPage: React.FC = () => {
  const [currentMode, setCurrentMode] = useState<PageMode>("list");
  const [selectedPostId, setSelectedPostId] = useState<number | null>(null);
  const [editingPost, setEditingPost] = useState<Post | null>(null);

  // 게시글 목록으로 이동
  const goToList = () => {
    setCurrentMode("list");
    setSelectedPostId(null);
    setEditingPost(null);
  };

  // 게시글 상세 보기
  const goToDetail = (postId: number) => {
    setSelectedPostId(postId);
    setCurrentMode("detail");
  };

  // 게시글 작성 페이지로 이동
  const goToCreate = () => {
    setEditingPost(null);
    setCurrentMode("create");
  };

  // 게시글 수정 페이지로 이동
  const goToEdit = (post: Post) => {
    setEditingPost(post);
    setCurrentMode("edit");
  };

  // 게시글 생성/수정 완료 후 처리
  const handlePostSave = (post: Post) => {
    // 성공 메시지 표시
    alert(
      editingPost ? "게시글이 수정되었습니다!" : "게시글이 작성되었습니다!"
    );

    // 상세 페이지로 이동
    setSelectedPostId(post.id);
    setCurrentMode("detail");
    setEditingPost(null);
  };

  // 게시글 삭제 처리
  const handlePostDelete = async (postId: number) => {
    try {
      await PostService.deletePost(postId);
      alert("게시글이 삭제되었습니다.");
      goToList();
    } catch (err) {
      alert(err instanceof Error ? err.message : "게시글 삭제에 실패했습니다.");
    }
  };

  // 현재 모드에 따라 렌더링할 컴포넌트 결정
  const renderCurrentPage = () => {
    switch (currentMode) {
      case "list":
        return <PostBoard onPostClick={goToDetail} onCreatePost={goToCreate} />;

      case "detail":
        if (!selectedPostId) {
          goToList();
          return null;
        }
        return (
          <PostDetail
            postId={selectedPostId}
            onBack={goToList}
            onEdit={goToEdit}
            onDelete={handlePostDelete}
          />
        );

      case "create":
        return <PostForm onSave={handlePostSave} onCancel={goToList} />;

      case "edit":
        if (!editingPost) {
          goToList();
          return null;
        }
        return (
          <PostForm
            post={editingPost}
            onSave={handlePostSave}
            onCancel={goToList}
          />
        );

      default:
        return <PostBoard onPostClick={goToDetail} onCreatePost={goToCreate} />;
    }
  };

  return (
    <div className="post-board-page">
      <div className="page-container">{renderCurrentPage()}</div>
    </div>
  );
};

export default PostBoardPage;
