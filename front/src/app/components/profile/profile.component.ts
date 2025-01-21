import { Component, OnInit } from '@angular/core';
import { User } from '../../shared/models/user';
import { UserService } from '../../shared/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit() {
    const userId = this.getLoggedInUserId();
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

  private getLoggedInUserId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
  }
}
