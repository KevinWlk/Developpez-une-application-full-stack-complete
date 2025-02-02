import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuard } from './shared/guards/auth.guard';
import { SubjectListComponent } from './components/subject-list/subject-list.component';
import { PostListComponent } from './components/post-list/post-list.component';
import { PostDetailComponent } from './components/post-detail/post-detail.component';
import { PostCreatePageComponent } from './components/post-create-page/post-create-page.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'subjects', component: SubjectListComponent, canActivate: [AuthGuard] },
  { path: 'posts', component: PostListComponent, canActivate: [AuthGuard] },
  { path: 'post/create', component: PostCreatePageComponent, canActivate: [AuthGuard] },
  { path: 'post/:id', component: PostDetailComponent, canActivate: [AuthGuard] },


// 🔒 Ajout de l'AuthGuard pour sécuriser

  // Redirection vers la home en cas de route inconnue
  { path: '**', redirectTo: '', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
