import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../shared/models/user';
import { UserService } from '../../shared/services/user.service';
import { AuthService } from '../../shared/services/auth.service';
import { Subject } from '../../shared/models/subject';
import { Subscription } from '../../shared/models/subscription';
import { SubscriptionService } from '../../shared/services/subscription.service';
import { SubjectService } from '../../shared/services/subject.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: [],
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  error: string | null = null;
  successMessage: string | null = null;
  subscriptions: Subscription[] = [];
  subjects: Subject[] = [];

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private subscriptionService: SubscriptionService,
    private subjectService: SubjectService,
    private router: Router
  ) {}

  ngOnInit() {
    const userId = this.authService.getUserId();
    if (userId) {
      this.loadUser(userId);
      this.loadSubscriptions(userId);
    } else {
      this.error = "Utilisateur non trouvé.";
    }
  }

  private loadUser(userId: number) {
    this.userService.getUser(userId).subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (err) => {
        this.error = "Impossible de charger les informations de l'utilisateur.";
        console.error(err);
      },
    });
  }

  private loadSubscriptions(userId: number) {
    this.subscriptionService.getSubscriptionsByUserId(userId).subscribe({
      next: (data) => {
        this.subscriptions = data;
        this.loadSubjects();
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des abonnements", err);
      },
    });
  }

  private loadSubjects() {
    this.subjectService.getAllSubjects().subscribe({
      next: (subjects: Subject[]) => {
        this.subjects = subjects.filter((subject) =>
          this.subscriptions.some((sub) => sub.subjectId === subject.id)
        );
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des sujets", err);
      },
    });
  }

  unsubscribe(subjectId: number) {
    const subscription = this.subscriptions.find((sub) => sub.subjectId === subjectId);
    if (subscription) {
      this.subscriptionService.unsubscribe(subscription.id).subscribe(() => {
        this.subscriptions = this.subscriptions.filter((sub) => sub.id !== subscription.id);
        this.subjects = this.subjects.filter((subject) => subject.id !== subjectId);
      });
    }
  }

  saveChanges() {
    if (this.user) {
      this.userService.updateUser(this.user.id, { name: this.user.name, email: this.user.email }).subscribe({
        next: (updatedUser) => {
          this.successMessage = 'Profil mis à jour avec succès.';
          this.user = updatedUser;
        },
        error: (err) => {
          this.error = "Erreur lors de la mise à jour du profil.";
          console.error(err);
        },
      });
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
