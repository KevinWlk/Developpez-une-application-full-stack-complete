import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject } from '../../shared/models/subject';
import { SubscriptionService } from '../../shared/services/subscription.service';
import { PostService } from '../../shared/services/post.service';

@Component({
  selector: 'app-post-create-page',
  templateUrl: './post-create-page.component.html',
  styleUrls: [],
})
export class PostCreatePageComponent implements OnInit {
  postForm!: FormGroup;
  subscribedSubjects: Subject[] = [];

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private subscriptionService: SubscriptionService,
    private postService: PostService
  ) {
  }

  ngOnInit(): void {
    this.postForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      subjectId: [null, Validators.required],
    });

    this.loadSubscribedSubjects();
  }

  selectedSubject: Subject | null = null;

  selectSubject(subject: Subject): void {
    this.selectedSubject = subject;
    this.postForm.patchValue({subjectId: subject.id});
  }

  private loadSubscribedSubjects(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.subscriptionService.getSubscriptionsByUserId(+userId).subscribe((subscriptions) => {
        this.subscribedSubjects = subscriptions.map(sub => ({
          id: sub.subjectId, // Utilisation de subjectId
          name: (sub as any).subjectName || 'Inconnu', // Contournement de TypeScript
          description: '', // Ajout d'une valeur vide pour respecter l'interface
          createdAt: new Date() // Valeur par défaut pour respecter l'interface
        }));
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/posts']); // Redirige vers la liste des articles
  }

  onInput(event: any): void {
    if (event.target.value.length > 100) {
      event.target.value = event.target.value.substring(0, 100);
    }
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      this.postService.createPost(this.postForm.value).subscribe(() => {
        this.router.navigate(['/posts']); // 🔥 Redirection après création
      });
    }
  }
}
