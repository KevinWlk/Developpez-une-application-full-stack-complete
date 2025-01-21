export interface Subscription {
  id: number;
  subjectId: number;
  subject?: {
    id: number;
    name: string;
  };
  userId: number;
}
