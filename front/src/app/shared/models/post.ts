export interface Post {
  id: number;
  subjectId: number;
  subjectName: string;
  title: string;
  content: string;
  createdAt: Date;
  updatedAt: Date;
}
