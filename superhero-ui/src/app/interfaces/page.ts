import { Order } from "./order";

export interface Page {
  content: Order[];
  pageable: { [key: string]: any };
  totalElements: number;
}
