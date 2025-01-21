import { Component, OnInit } from '@angular/core';
import { Subject } from '../../shared/models/subject';
import { Subscription } from '../../shared/models/subscription';
import { SubjectService } from '../../shared/services/subject.service';
import { SubscriptionService } from '../../shared/services/subscription.service';

@Component({
  selector: 'app-subject-list',
  templateUrl: './subject-list.component.html',
  styleUrls: [],
})
export class SubjectListComponent implements OnInit {
  subjects: Subject[] = [];
  subscriptions: Subscription[] = [];
  isLoading: boolean = true;

  constructor(
    private subjectService: SubjectService,
    private subscriptionService: SubscriptionService
  ) {}

  ngOnInit(): void {
    this.loadSubjects();
    this.loadSubscriptions();
  }

  private loadSubjects(): void {
    this.subjectService.getAllSubjects().subscribe(
      (data: Subject[]) => {
        this.subjects = data;
        this.isLoading = false;
      },
      (error: any) => {
        console.error('Erreur lors de la récupération des sujets', error);
        this.isLoading = false;
      }
    );
  }

  private loadSubscriptions(): void {
    const userId = localStorage.getItem('userId'); // Récupération de l'utilisateur connecté
    if (userId) {
      this.subscriptionService.getSubscriptionsByUserId(+userId).subscribe(
        (data: Subscription[]) => {
          this.subscriptions = data;
        },
        (error: any) => {
          console.error('Erreur lors de la récupération des souscriptions', error);
        }
      );
    }
  }

  isSubscribed(subjectId: number): boolean {
    return this.subscriptions.some((sub) => sub.subjectId === subjectId);
  }

  toggleSubscription(subjectId: number): void {
    const userId = localStorage.getItem('userId');
    if (!userId) return;

    if (this.isSubscribed(subjectId)) {
      const subscription = this.subscriptions.find((sub) => sub.subjectId === subjectId);
      if (subscription) {
        this.subscriptionService.unsubscribe(subscription.id).subscribe(() => {
          this.subscriptions = this.subscriptions.filter((sub) => sub.id !== subscription.id);
        });
      }
    } else {
      this.subscriptionService
        .subscribe({ userId: +userId, subjectId })
        .subscribe((newSubscription: Subscription) => {
          this.subscriptions.push(newSubscription);
        });
    }
  }
}
