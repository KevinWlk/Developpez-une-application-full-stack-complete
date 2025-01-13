import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  template: `
    <mat-toolbar color="primary">
      <span>MDD - Accueil</span>
      <span class="spacer"></span>
      <button mat-button routerLink="/login">Connexion</button>
      <button mat-button routerLink="/register">Inscription</button>
    </mat-toolbar>
    <div class="content">
      <h1>Bienvenue sur Monde de Dév</h1>
      <p>Explorez, apprenez et connectez-vous avec d'autres développeurs.</p>
    </div>
  `,
  styles: [
    `.spacer { flex: 1 1 auto; }`,
    `.content { text-align: center; margin-top: 50px; }`
  ]
})
export class HomeComponent { }
