import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject } from '../../shared/models/subject';

@Component({
  selector: 'app-post-dialog',
  templateUrl: './post-dialog.component.html',
  styleUrls: [],
})
export class PostDialogComponent implements OnInit {
  postForm!: FormGroup;
  subscribedSubjects: Subject[] = [];

  constructor(
    public dialogRef: MatDialogRef<PostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { subjects: Subject[] },
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.subscribedSubjects = this.data.subjects || [];

    this.postForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      subjectId: [null, Validators.required],
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      this.dialogRef.close(this.postForm.value);
    }
  }
}
