import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Post } from '../../shared/models/post';
import { Subject } from '../../shared/models/subject';
import { PostService } from '../../shared/services/post.service';
import { SubscriptionService } from '../../shared/services/subscription.service';
import { SubjectService } from '../../shared/services/subject.service';
import { PostDialogComponent } from '../post-dialog/post-dialog.component';

@Component({
  selector: 'app-article-list',
  templateUrl: './post-list.component.html',
  styleUrls: [],
})
export class PostListComponent implements OnInit {
  articles: Post[] = [];
  isLoading: boolean = true;
  subscribedSubjects: Subject[] = [];
  sortOrder: 'asc' | 'desc' = 'desc';

  constructor(
    private postService: PostService,
    private subscriptionService: SubscriptionService,
    private subjectService: SubjectService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSubscribedSubjects();
  }

  private loadSubscribedSubjects(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.subscriptionService.getSubscriptionsByUserId(+userId).subscribe((subs) => {
        this.subjectService.getAllSubjects().subscribe((allSubjects) => {
          this.subscribedSubjects = subs
            .map((sub) => allSubjects.find((s) => s.id === sub.subjectId) || null)
            .filter((subject): subject is Subject => !!subject);

          this.loadArticles();
        });
      });
    }
  }

  private loadArticles(): void {
    this.isLoading = true;
    this.articles = [];
    const requests = this.subscribedSubjects.map((subject) => this.postService.getPostsBySubjectId(subject.id));

    Promise.all(requests.map((req) => req.toPromise()))
      .then((results) => {
        results.forEach((posts) => {
          if (posts) {
            this.articles.push(...posts);
          }
        });
        this.sortArticles();
        this.isLoading = false;
      })
      .catch((error) => {
        console.error("Erreur lors du chargement des articles :", error);
        this.isLoading = false;
      });
  }

  viewPostDetail(postId: number): void {
    this.router.navigate(['/post', postId]);
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(PostDialogComponent, {
      width: '500px',
      data: { subjects: this.subscribedSubjects },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.postService.createPost(result).subscribe((newPost) => {
          this.articles.push(newPost);
          this.sortArticles();
        });
      }
    });
  }

  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    this.sortArticles();
  }

  private sortArticles(): void {
    this.articles.sort((a, b) => {
      return this.sortOrder === 'asc'
        ? new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
        : new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
    });
  }
}
