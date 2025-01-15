import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:3001/api';

  constructor(private http: HttpClient) {}

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user/${id}`).pipe(
      catchError((error) => {
        console.error('Erreur lors de la récupération de l’utilisateur:', error);
        return throwError(() => new Error('Erreur de récupération des données utilisateur.'));
      })
    );
  }
}
