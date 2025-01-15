import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../shared/services/themes.service';
import { Subject } from '../../shared/models/Subject';
import { SubscriptionService } from '../../shared/services/subscription.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
})
export class ThemesComponent implements OnInit {
  themes: Subject[] = [];
  userId = 1; // Remplacez avec l'ID de l'utilisateur connecté

  constructor(
    private themeService: ThemeService,
    private subscriptionService: SubscriptionService
  ) {}

  ngOnInit(): void {
    this.themeService.getAllThemes().subscribe((data) => {
      this.themes = data;
    });
  }

  subscribe(subjectId: number): void {
    this.subscriptionService.subscribe({ userId: this.userId, subjectId }).subscribe(() => {
      alert('Abonné avec succès !');
    });
  }

  unsubscribe(subscriptionId: number): void {
    this.subscriptionService.unsubscribe(subscriptionId).subscribe(() => {
      alert('Désabonné avec succès !');
    });
  }
}
