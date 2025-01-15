import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  template: `
    <div class="bg-gray-50 min-h-screen flex flex-col items-center justify-center">
      <header class="w-full bg-blue-600 py-4 text-white text-center">
        <h1 class="text-2xl font-bold">Monde de Dév</h1>
      </header>
      <main class="flex flex-col items-center p-6">
        <h2 class="text-4xl font-bold text-gray-800">Bienvenue</h2>
        <p class="mt-4 text-lg text-gray-600">Explorez, apprenez et connectez-vous avec d'autres développeurs.</p>
        <div class="mt-8 flex gap-4">
          <a routerLink="/login" class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-700">Connexion</a>
          <a routerLink="/register" class="px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-700">Inscription</a>
        </div>
      </main>
    </div>
  `,
  styles: []
})
export class HomeComponent { }
