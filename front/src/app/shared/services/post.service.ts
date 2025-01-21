import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = 'http://localhost:3001/api/posts';

  constructor(private http: HttpClient) {}

  getPostsBySubjectId(subjectId: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/subject/${subjectId}`);
  }
}
