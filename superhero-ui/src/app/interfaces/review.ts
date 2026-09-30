export interface Review {
  id: number;
  productId: number;
  userEmail: string;
  rating: number;
  title: string;
  comment: string;
  date: Date;
}
