import {
  Post,
  PostSummary,
  PostRequest,
  PostListResponse,
} from "../types/Post";

const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

export class PostService {
  // 게시글 목록 조회
  static async getPosts(
    page: number = 0,
    size: number = 10
  ): Promise<PostListResponse> {
    const response = await fetch(
      `${API_BASE_URL}/api/posts?page=${page}&size=${size}`
    );
    if (!response.ok) {
      throw new Error("게시글 목록 조회에 실패했습니다.");
    }
    return response.json();
  }

  // 카테고리별 게시글 조회
  static async getPostsByCategory(
    category: string,
    page: number = 0,
    size: number = 10
  ): Promise<PostListResponse> {
    const response = await fetch(
      `${API_BASE_URL}/api/posts/category/${encodeURIComponent(category)}?page=${page}&size=${size}`
    );
    if (!response.ok) {
      throw new Error("카테고리별 게시글 조회에 실패했습니다.");
    }
    return response.json();
  }

  // 게시글 검색
  static async searchPosts(
    keyword: string,
    page: number = 0,
    size: number = 10
  ): Promise<PostListResponse> {
    const response = await fetch(
      `${API_BASE_URL}/api/posts/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`
    );
    if (!response.ok) {
      throw new Error("게시글 검색에 실패했습니다.");
    }
    return response.json();
  }

  // 게시글 상세 조회
  static async getPostById(id: number): Promise<Post> {
    const response = await fetch(`${API_BASE_URL}/api/posts/${id}`);
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("게시글을 찾을 수 없습니다.");
      }
      throw new Error("게시글 조회에 실패했습니다.");
    }
    return response.json();
  }

  // 게시글 생성
  static async createPost(postData: PostRequest): Promise<Post> {
    const response = await fetch(`${API_BASE_URL}/api/posts`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(postData),
    });

    if (!response.ok) {
      throw new Error("게시글 생성에 실패했습니다.");
    }
    return response.json();
  }

  // 게시글 수정
  static async updatePost(id: number, postData: PostRequest): Promise<Post> {
    const response = await fetch(`${API_BASE_URL}/api/posts/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(postData),
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("게시글을 찾을 수 없습니다.");
      }
      throw new Error("게시글 수정에 실패했습니다.");
    }
    return response.json();
  }

  // 게시글 삭제
  static async deletePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/posts/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("게시글을 찾을 수 없습니다.");
      }
      throw new Error("게시글 삭제에 실패했습니다.");
    }
  }

  // 게시글 좋아요
  static async likePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/posts/${id}/like`, {
      method: "POST",
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("게시글을 찾을 수 없습니다.");
      }
      throw new Error("좋아요 처리에 실패했습니다.");
    }
  }

  // 인기 게시글 조회
  static async getPopularPosts(limit: number = 5): Promise<PostSummary[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/posts/popular?limit=${limit}`
    );
    if (!response.ok) {
      throw new Error("인기 게시글 조회에 실패했습니다.");
    }
    return response.json();
  }

  // 카테고리 목록 조회
  static async getCategories(): Promise<string[]> {
    const response = await fetch(`${API_BASE_URL}/api/posts/categories`);
    if (!response.ok) {
      throw new Error("카테고리 목록 조회에 실패했습니다.");
    }
    return response.json();
  }

  // 작성자별 게시글 조회
  static async getPostsByAuthor(
    author: string,
    page: number = 0,
    size: number = 10
  ): Promise<PostListResponse> {
    const response = await fetch(
      `${API_BASE_URL}/api/posts/author/${encodeURIComponent(author)}?page=${page}&size=${size}`
    );
    if (!response.ok) {
      throw new Error("작성자별 게시글 조회에 실패했습니다.");
    }
    return response.json();
  }
}
