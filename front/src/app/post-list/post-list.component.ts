import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {PostService} from "../shared/services/post.service";

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss'],
})
export class PostListComponent implements OnInit {
  posts: any[] = [];
  subjectId!: number;

  constructor(private postService: PostService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.subjectId = +this.route.snapshot.paramMap.get('subjectId')!;
    this.postService.getPostsBySubjectId(this.subjectId).subscribe(
      (data) => (this.posts = data),
      (error) => console.error('Error fetching posts:', error)
    );
  }
}
