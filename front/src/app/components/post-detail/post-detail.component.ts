import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from '../../shared/models/post';
import { Comment } from '../../shared/models/comment';
import { PostService } from '../../shared/services/post.service';
import { CommentService } from '../../shared/services/comment.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: [],
})
export class PostDetailComponent implements OnInit {
  post!: Post;
  comments: Comment[] = [];
  newComment: string = '';
  userId: number = Number(localStorage.getItem('userId'));

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPost(postId);
    this.loadComments(postId);
  }

  private loadPost(postId: number): void {
    this.postService.getPostById(postId).subscribe((post) => {
      this.post = post;
    });
  }

  private loadComments(postId: number): void {
    this.commentService.getCommentsByPostId(postId).subscribe((comments) => {
      this.comments = comments;
    });
  }

  // Ajouter un commentaire
  addComment(): void {
    if (!this.newComment.trim()) return;

    const comment: Comment = {
      id: 0, // Géré par le backend
      postId: this.post.id,
      userId: this.userId,
      content: this.newComment,
      createdAt: new Date(),
    };

    this.commentService.createComment(comment).subscribe((newComment) => {
      this.comments.push(newComment);
      this.newComment = '';
    });
  }

  // Modifier un commentaire
  updateComment(comment: Comment): void {
    const updatedContent = prompt('Modifier votre commentaire', comment.content);
    if (updatedContent) {
      comment.content = updatedContent;
      comment.content = updatedContent;
      this.commentService.updateComment(comment).subscribe(() => {
        // Mettre à jour la liste des commentaires
        this.comments = this.comments.map(c => (c.id === comment.id ? { ...c, content: updatedContent } : c));
      });
    }
  }

  // Supprimer un commentaire
  deleteComment(commentId: number): void {
    if (confirm('Voulez-vous vraiment supprimer ce commentaire ?')) {
      this.commentService.deleteComment(commentId).subscribe(() => {
        this.comments = this.comments.filter((c) => c.id !== commentId);
      });
    }
  }
}
