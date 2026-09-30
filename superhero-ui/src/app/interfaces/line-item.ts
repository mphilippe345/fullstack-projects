import { Product } from './product';

export interface LineItem {
  id?: number;
  product: {
    id: number;
  };
  quantity: number;
}
