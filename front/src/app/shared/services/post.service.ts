import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = 'http://localhost:3001/api/posts';

  constructor(private http: HttpClient) {
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    console.log("Token envoyé dans la requête :", token); // Ajout du log

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }



  getAllPosts(): Observable<Post[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Post[]>(this.apiUrl, { headers });
  }


  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`, {headers: this.getAuthHeaders()});
  }

  getPostsBySubjectId(subjectId: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/subject/${subjectId}`, {headers: this.getAuthHeaders()});
  }

  createPost(post: Post): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, post, {headers: this.getAuthHeaders()});
  }

  updatePost(id: number, post: Post): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${id}`, post, {headers: this.getAuthHeaders()});
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {headers: this.getAuthHeaders()});
  }
}
