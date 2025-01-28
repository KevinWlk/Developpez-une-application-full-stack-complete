import { Component, OnInit } from '@angular/core';
import { User } from '../../shared/models/user';
import { UserService } from '../../shared/services/user.service';
import { AuthService } from '../../shared/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: [],
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit() {
    const userId = this.authService.getUserId();
    if (userId) {
      this.userService.getUser(userId).subscribe({
        next: (data) => {
          this.user = data;
        },
        error: (err) => {
          this.error = "Impossible de charger les informations de l'utilisateur.";
          console.error(err);
        },
      });
    } else {
      this.error = "Utilisateur non trouvé.";
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
}
