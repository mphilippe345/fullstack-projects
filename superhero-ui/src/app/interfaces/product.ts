import {PromoCode} from "./promoCode";
import {Review} from "./review";

export interface Product {
  id?: number,
  title: string,
  author: string,
  description: string,
  issue: number,
  volume: string,
  publisher: string,
  genre: string,
  releaseDate: Date
  price: number,
  sku: string
  imageUrl: string,
  reviews: Review[],
  active: boolean,
  stockStatus: boolean,
  promos: PromoCode[],
  newrelease: boolean,
  bestSeller: boolean,
  condition?: string,
}
