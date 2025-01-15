import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subject } from '../models/Subject';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private apiUrl = 'http://localhost:3001/api/subjects';

  constructor(private http: HttpClient) {}

  getAllThemes(): Observable<Subject[]> {
    return this.http.get<Subject[]>(this.apiUrl);
  }
}
