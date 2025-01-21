import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subscription } from '../models/subscription';

@Injectable({
  providedIn: 'root',
})
export class SubscriptionService {
  private apiUrl = 'http://localhost:3001/api/subscriptions';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getSubscriptionsByUserId(userId: number): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(`${this.apiUrl}/user/${userId}`, {
      headers: this.getAuthHeaders(),
    });
  }

  subscribe(subscription: { userId: number; subjectId: number }): Observable<Subscription> {
    return this.http.post<Subscription>(this.apiUrl, subscription, {
      headers: this.getAuthHeaders(),
    });
  }

  unsubscribe(subscriptionId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${subscriptionId}`, {
      headers: this.getAuthHeaders(),
    });
  }
}
