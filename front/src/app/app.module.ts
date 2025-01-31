import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Angular Material Modules
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDialogModule } from '@angular/material/dialog';

// Composants principaux
import { HeaderComponent } from './shared/header/header.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SubjectListComponent } from './components/subject-list/subject-list.component';
import { NotFoundComponent } from './shared/not-found/not-found.component';
import { PostListComponent } from './components/post-list/post-list.component';
import {MatMenuModule} from "@angular/material/menu";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { PostCreateComponent } from './components/post-create/post-create.component';
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import { PostDetailComponent } from './components/post-detail/post-detail.component';
import { PostCreatePageComponent } from './components/post-create-page/post-create-page.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    HeaderComponent,
    ProfileComponent,
    SubjectListComponent,
    NotFoundComponent,
    PostListComponent,
    PostCreateComponent,
    PostDetailComponent,
    PostCreatePageComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    AppRoutingModule,
    MatToolbarModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    MatIconModule,
    MatListModule,
    MatDialogModule,
    MatSelectModule,
    MatOptionModule,

    MatMenuModule,
    MatProgressSpinnerModule
  ],
  entryComponents: [PostCreateComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
