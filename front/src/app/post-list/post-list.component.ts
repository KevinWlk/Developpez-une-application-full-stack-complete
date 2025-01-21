import { Component, OnInit } from '@angular/core';
import { Post } from '../shared/models/post';
import { Subject } from '../shared/models/subject';
import { PostService } from '../shared/services/post.service';
import { SubscriptionService } from '../shared/services/subscription.service';
import { MatDialog } from '@angular/material/dialog';
import { PostDialogComponent } from '../components/post-dialog/post-dialog.component';


@Component({
  selector: 'app-article-list',
  templateUrl: './post-list.component.html',
  styleUrls: [],
})
export class PostListComponent implements OnInit {
  articles: Post[] = [];
  isLoading: boolean = true;
  subscribedSubjects: Subject[] = [];

  constructor(
    private postService: PostService,
    private subscriptionService: SubscriptionService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadSubscribedSubjects();
  }
  loadSubscribedSubjects(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.subscriptionService.getSubscriptionsByUserId(+userId).subscribe((subs) => {
        this.subscribedSubjects = subs
          .map((sub) => sub.subject)
          .filter((subject): subject is Subject => !!subject); // Filtre les undefined
        this.loadArticles();
      });
    }
  }

  loadArticles(): void {
    if (this.subscribedSubjects.length > 0) {
      this.subscribedSubjects.forEach((subject) => {
        this.postService.getPostsBySubjectId(subject.id).subscribe((posts) => {
          this.articles.push(...posts);
          this.isLoading = false;
        });
      });
    } else {
      this.isLoading = false;
    }
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(PostDialogComponent, {
      width: '500px',
      data: { subjects: this.subscribedSubjects }, // Passer les sujets souscrits
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.postService.createPost(result).subscribe((newPost) => {
          this.articles.push(newPost); // Ajouter l'article à la liste
        });
      }
    });
  }


  openEditDialog(article: Post): void {
    // Implémentez l'ouverture d'un dialogue pour modifier un article
  }

  deleteArticle(articleId: number): void {
    this.postService.deletePost(articleId).subscribe(() => {
      this.articles = this.articles.filter((article) => article.id !== articleId);
    });
  }
}
