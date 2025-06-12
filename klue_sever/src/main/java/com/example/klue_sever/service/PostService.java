package com.example.klue_sever.service;

import com.example.klue_sever.dto.PostRequestDto;
import com.example.klue_sever.dto.PostResponseDto;
import com.example.klue_sever.entity.Post;
import com.example.klue_sever.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {
    
    private final PostRepository postRepository;
    
    // 게시글 목록 조회 (요약 형태)
    public Page<PostResponseDto.Summary> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc(pageable);
        
        return posts.map(this::convertToSummary);
    }
    
    // 카테고리별 게시글 조회
    public Page<PostResponseDto.Summary> getPostsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByCategoryAndIsDeletedFalseOrderByIsPinnedDescCreatedAtDesc(category, pageable);
        
        return posts.map(this::convertToSummary);
    }
    
    // 검색 (제목 또는 내용)
    public Page<PostResponseDto.Summary> searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByTitleOrContentContaining(keyword, pageable);
        
        return posts.map(this::convertToSummary);
    }
    
    // 작성자별 게시글 조회
    public Page<PostResponseDto.Summary> getPostsByAuthor(String author, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByAuthorAndIsDeletedFalseOrderByCreatedAtDesc(author, pageable);
        
        return posts.map(this::convertToSummary);
    }
    
    // 게시글 상세 조회
    @Transactional
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        // 조회수 증가
        postRepository.incrementViewCount(id);
        
        return convertToResponseDto(post);
    }
    
    // 게시글 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(requestDto.getAuthor())
                .category(requestDto.getCategory())
                .isPinned(requestDto.getIsPinned() != null ? requestDto.getIsPinned() : false)
                .build();
        
        Post savedPost = postRepository.save(post);
        log.info("새 게시글 생성: ID={}, 제목={}, 작성자={}", savedPost.getId(), savedPost.getTitle(), savedPost.getAuthor());
        
        return convertToResponseDto(savedPost);
    }
    
    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setCategory(requestDto.getCategory());
        if (requestDto.getIsPinned() != null) {
            post.setIsPinned(requestDto.getIsPinned());
        }
        
        Post updatedPost = postRepository.save(post);
        log.info("게시글 수정: ID={}, 제목={}", updatedPost.getId(), updatedPost.getTitle());
        
        return convertToResponseDto(updatedPost);
    }
    
    // 게시글 삭제 (소프트 삭제)
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        post.setIsDeleted(true);
        postRepository.save(post);
        log.info("게시글 삭제: ID={}, 제목={}", post.getId(), post.getTitle());
    }
    
    // 좋아요 증가
    @Transactional
    public void likePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id);
        }
        
        postRepository.incrementLikeCount(id);
        log.info("게시글 좋아요: ID={}", id);
    }
    
    // 인기 게시글 조회
    public List<PostResponseDto.Summary> getPopularPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Post> posts = postRepository.findPopularPosts(pageable);
        
        return posts.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
    
    // 카테고리 목록 조회
    public List<String> getCategories() {
        return postRepository.findDistinctCategories();
    }
    
    // Entity -> ResponseDto 변환
    private PostResponseDto convertToResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .category(post.getCategory())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .isPinned(post.getIsPinned())
                .build();
    }
    
    // Entity -> Summary 변환
    private PostResponseDto.Summary convertToSummary(Post post) {
        String preview = post.getContent().length() > 100 ? 
                post.getContent().substring(0, 100) + "..." : 
                post.getContent();
        
        return PostResponseDto.Summary.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .category(post.getCategory())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .isPinned(post.getIsPinned())
                .preview(preview)
                .build();
    }
} 