import { Product } from "./product";

export interface CartItem {
    id?: number;
    product: Product;
    quantity: number;
    cost: number;
}

export interface Cart{
    map(arg0: (item: any) => any): Product[];
    items: CartItem[];
}