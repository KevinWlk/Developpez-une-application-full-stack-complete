import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginResponse } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [],
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe(
        (response: LoginResponse) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('userId', response.userId.toString());
          this.router.navigate(['/subjects']);
        },
        (error) => {
          console.error('Erreur de connexion', error);
        }
      );
    }
  }

  goBack() {
    this.router.navigate(['/']);
  }
}
