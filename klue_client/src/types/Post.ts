export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  category?: string;
  viewCount: number;
  likeCount: number;
  createdAt: string;
  updatedAt: string;
  isPinned: boolean;
}

export interface PostSummary {
  id: number;
  title: string;
  author: string;
  category?: string;
  viewCount: number;
  likeCount: number;
  createdAt: string;
  isPinned: boolean;
  preview: string;
}

export interface PostRequest {
  title: string;
  content: string;
  author: string;
  category?: string;
  isPinned?: boolean;
}

export interface PostListResponse {
  posts: PostSummary[];
  currentPage: number;
  totalPages: number;
  totalItems: number;
  pageSize: number;
  isFirst: boolean;
  isLast: boolean;
}
