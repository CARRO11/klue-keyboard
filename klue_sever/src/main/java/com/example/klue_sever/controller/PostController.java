package com.example.klue_sever.controller;

import com.example.klue_sever.dto.PostRequestDto;
import com.example.klue_sever.dto.PostResponseDto;
import com.example.klue_sever.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Validated
public class PostController {
    
    private final PostService postService;
    
    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Page<PostResponseDto.Summary> posts = postService.getAllPosts(page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts.getContent());
            response.put("currentPage", posts.getNumber());
            response.put("totalPages", posts.getTotalPages());
            response.put("totalItems", posts.getTotalElements());
            response.put("pageSize", posts.getSize());
            response.put("isFirst", posts.isFirst());
            response.put("isLast", posts.isLast());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("게시글 목록 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "게시글 목록 조회에 실패했습니다."));
        }
    }
    
    // 카테고리별 게시글 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getPostsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Page<PostResponseDto.Summary> posts = postService.getPostsByCategory(category, page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts.getContent());
            response.put("currentPage", posts.getNumber());
            response.put("totalPages", posts.getTotalPages());
            response.put("totalItems", posts.getTotalElements());
            response.put("pageSize", posts.getSize());
            response.put("category", category);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("카테고리별 게시글 조회 실패: {}", category, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카테고리별 게시글 조회에 실패했습니다."));
        }
    }
    
    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "검색 키워드를 입력해주세요."));
            }
            
            Page<PostResponseDto.Summary> posts = postService.searchPosts(keyword.trim(), page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts.getContent());
            response.put("currentPage", posts.getNumber());
            response.put("totalPages", posts.getTotalPages());
            response.put("totalItems", posts.getTotalElements());
            response.put("pageSize", posts.getSize());
            response.put("keyword", keyword);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("게시글 검색 실패: {}", keyword, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "게시글 검색에 실패했습니다."));
        }
    }
    
    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        try {
            PostResponseDto post = postService.getPostById(id);
            return ResponseEntity.ok(post);
            
        } catch (RuntimeException e) {
            log.error("게시글 상세 조회 실패: ID={}", id, e);
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error("게시글 상세 조회 오류: ID={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 게시글 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto requestDto) {
        try {
            PostResponseDto createdPost = postService.createPost(requestDto);
            log.info("새 게시글 생성됨: ID={}, 제목={}", createdPost.getId(), createdPost.getTitle());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
            
        } catch (Exception e) {
            log.error("게시글 생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDto requestDto) {
        
        try {
            PostResponseDto updatedPost = postService.updatePost(id, requestDto);
            log.info("게시글 수정됨: ID={}, 제목={}", updatedPost.getId(), updatedPost.getTitle());
            
            return ResponseEntity.ok(updatedPost);
            
        } catch (RuntimeException e) {
            log.error("게시글 수정 실패: ID={}", id, e);
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error("게시글 수정 오류: ID={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            log.info("게시글 삭제됨: ID={}", id);
            
            return ResponseEntity.ok(Map.of("message", "게시글이 삭제되었습니다."));
            
        } catch (RuntimeException e) {
            log.error("게시글 삭제 실패: ID={}", id, e);
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error("게시글 삭제 오류: ID={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "게시글 삭제에 실패했습니다."));
        }
    }
    
    // 게시글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, String>> likePost(@PathVariable Long id) {
        try {
            postService.likePost(id);
            log.info("게시글 좋아요: ID={}", id);
            
            return ResponseEntity.ok(Map.of("message", "좋아요가 추가되었습니다."));
            
        } catch (RuntimeException e) {
            log.error("게시글 좋아요 실패: ID={}", id, e);
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error("게시글 좋아요 오류: ID={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "좋아요 처리에 실패했습니다."));
        }
    }
    
    // 인기 게시글 조회
    @GetMapping("/popular")
    public ResponseEntity<List<PostResponseDto.Summary>> getPopularPosts(
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<PostResponseDto.Summary> popularPosts = postService.getPopularPosts(limit);
            return ResponseEntity.ok(popularPosts);
            
        } catch (Exception e) {
            log.error("인기 게시글 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        try {
            List<String> categories = postService.getCategories();
            return ResponseEntity.ok(categories);
            
        } catch (Exception e) {
            log.error("카테고리 목록 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 작성자별 게시글 조회
    @GetMapping("/author/{author}")
    public ResponseEntity<Map<String, Object>> getPostsByAuthor(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Page<PostResponseDto.Summary> posts = postService.getPostsByAuthor(author, page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts.getContent());
            response.put("currentPage", posts.getNumber());
            response.put("totalPages", posts.getTotalPages());
            response.put("totalItems", posts.getTotalElements());
            response.put("pageSize", posts.getSize());
            response.put("author", author);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("작성자별 게시글 조회 실패: {}", author, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "작성자별 게시글 조회에 실패했습니다."));
        }
    }
} 