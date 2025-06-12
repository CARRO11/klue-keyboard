package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 삭제되지 않은 게시글 조회 (페이지네이션)
    Page<Post> findByIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc(Pageable pageable);
    
    // 카테고리별 게시글 조회
    Page<Post> findByCategoryAndIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc(String category, Pageable pageable);
    
    // 제목으로 검색
    Page<Post> findByTitleContainingAndIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc(String title, Pageable pageable);
    
    // 내용으로 검색
    Page<Post> findByContentContainingAndIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc(String content, Pageable pageable);
    
    // 제목 또는 내용으로 검색
    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.isDeleted = false ORDER BY p.isPinned DESC, p.createdAt DESC")
    Page<Post> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 작성자별 게시글 조회
    Page<Post> findByAuthorAndIsDeletedFalseOrderByCreatedAtDesc(String author, Pageable pageable);
    
    // 상세 조회 (삭제되지 않은 게시글)
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
    
    // 조회수 증가
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    // 좋아요 수 증가
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    // 인기 게시글 조회 (조회수 또는 좋아요 수 기준)
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false ORDER BY (p.viewCount + p.likeCount) DESC")
    List<Post> findPopularPosts(Pageable pageable);
    
    // 카테고리 목록 조회
    @Query("SELECT DISTINCT p.category FROM Post p WHERE p.isDeleted = false AND p.category IS NOT NULL")
    List<String> findDistinctCategories();
} 