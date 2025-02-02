import { Component, ElementRef, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: [],
})
export class HeaderComponent {
  menuOpen = false;
  burgerMenuOpen = false;
  isMobile = false;

  constructor(private authService: AuthService, public router: Router, private eRef: ElementRef) {
    this.checkScreenSize();
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.closeMenu();
        this.burgerMenuOpen = false;
      }
    });
  }

  @HostListener('window:resize', [])
  checkScreenSize() {
    this.isMobile = window.innerWidth <= 768;
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  toggleBurgerMenu() {
    this.burgerMenuOpen = !this.burgerMenuOpen;
  }

  closeMenu() {
    this.menuOpen = false;
    this.burgerMenuOpen = false;
  }

  logout() {
    this.authService.logout();
    this.closeMenu();
    this.router.navigate(['/']);
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    if (this.menuOpen && !this.eRef.nativeElement.contains(event.target)) {
      this.closeMenu();
    }
  }
}
