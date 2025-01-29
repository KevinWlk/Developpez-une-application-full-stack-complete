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

    if (!token) {
      console.error("🚨 Aucun token trouvé dans localStorage !");
    } else {
      console.log("✅ Token trouvé :", token);
    }

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }


  private handleError(error: HttpErrorResponse) {
    console.error(`❌ Erreur API :`, error);
    if (error.status === 403) {
      console.warn("🚨 Accès refusé ! Vérifie que le token est bien valide.");
    }
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
    console.log(`🔍 Requête pour récupérer le post ID ${id}`);
    return this.http.get<Post>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Récupère les posts par ID de sujet */
  getPostsBySubjectId(subjectId: number): Observable<Post[]> {
    console.log(`📌 Récupération des posts pour le sujet ID ${subjectId}`);
    return this.http.get<Post[]>(`${this.apiUrl}/subject/${subjectId}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Crée un nouveau post */
  createPost(post: Post): Observable<Post> {
    console.log(`✍ Création d'un post`, post);
    return this.http.post<Post>(this.apiUrl, post, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Met à jour un post */
  updatePost(id: number, post: Post): Observable<Post> {
    console.log(`🛠 Mise à jour du post ID ${id}`);
    return this.http.put<Post>(`${this.apiUrl}/${id}`, post, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  /** Supprime un post */
  deletePost(id: number): Observable<void> {
    console.log(`🗑 Suppression du post ID ${id}`);
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }
}
