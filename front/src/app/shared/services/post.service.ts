import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Post } from '../models/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = 'http://localhost:3001/api/posts';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => new Error('Une erreur est survenue, veuillez réessayer plus tard.'));
  }

  /** Récupère tous les posts */
  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiUrl, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Récupère un post par son ID */
  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Récupère les posts par ID de sujet */
  getPostsBySubjectId(subjectId: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/subject/${subjectId}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Crée un nouveau post */
  createPost(post: Partial<Post>): Observable<Post> {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      return throwError(() => new Error('Utilisateur non connecté.'));
    }

    const postData = {
      ...post,
      userId: +userId,
    };

    return this.http.post<Post>(this.apiUrl, postData, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Met à jour un post */
  updatePost(id: number, post: Post): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${id}`, post, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Supprime un post */
  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }
}
