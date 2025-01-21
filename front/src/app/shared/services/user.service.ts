import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:3001/api';

  constructor(private http: HttpClient) {}

  getUser(id: number): Observable<User> {
    const token = localStorage.getItem('token'); // Récupérez le jeton stocké
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<User>(`${this.apiUrl}/user/${id}`, { headers });
  }
  updateUser(id: number, user: Partial<User>): Observable<User> {
    const token = localStorage.getItem('token'); // Si l'authentification est nécessaire
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.put<User>(`${this.apiUrl}/user/${id}`, user, { headers });
  }

}
