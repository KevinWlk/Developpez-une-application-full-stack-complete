import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subscription } from '../models/Subscription';

@Injectable({
  providedIn: 'root',
})
export class SubscriptionService {
  private apiUrl = 'http://localhost:3001/api/subscriptions';

  constructor(private http: HttpClient) {}

  getSubscriptionsByUserId(userId: number): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(`${this.apiUrl}/user/${userId}`);
  }

  subscribe(subscription: { userId: number; subjectId: number }): Observable<Subscription> {
    return this.http.post<Subscription>(this.apiUrl, subscription);
  }

  unsubscribe(subscriptionId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${subscriptionId}`);
  }
}
