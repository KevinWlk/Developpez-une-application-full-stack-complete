import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    // Ajouter le token si présent
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    // Gérer les erreurs d'authentification
    return next.handle(req).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse) {
          if (error.status === 401 || error.status === 403) {
            console.error("Accès interdit ou non autorisé. Redirection vers la connexion...");
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            this.router.navigate(['/login']); // Redirige vers la page de connexion
          }
        }
        return throwError(() => error);
      })
    );
  }
}
