import { Component, OnInit } from '@angular/core';
import { User } from '../../shared/models/User';
import { UserService } from '../../shared/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  error: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit() {
    const userId = this.getLoggedInUserId(); // Remplacez cette méthode par une récupération réelle de l'ID utilisateur
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

  private getLoggedInUserId(): number {
    // Exemple : récupérer l'ID utilisateur depuis le localStorage ou un service d'authentification
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0; // Valeur par défaut si l'utilisateur n'est pas connecté
  }
}
