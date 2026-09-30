import { HttpClient, HttpParams } from '@angular/common/http';
import {EventEmitter, Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../constants/constants';
import { Product } from '../interfaces/product';
import { Inventory } from "../interfaces/inventory";

@Injectable({
  providedIn: 'root',
})

export class ProductService {
  constructor(private http: HttpClient) {
  }

  products: Product[] = [];
  reinitializeEvent: EventEmitter<[void, number]> = new EventEmitter<[void, number]>();
  private productId?: number;

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(API_URL + 'products')
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(API_URL + 'products/' + id);
  }

  getAllTitlesAndImages(): Observable<Product[]> {
    return this.http.get<Product[]>(`${ API_URL }products/titles-and-images`)
  }

  getBestSellers(): Observable<Product[]> {
    return this.http.get<Product[]>(`${ API_URL }products/best-sellers`)
  }

  getNewReleases(): Observable<Product[]> {
    return this.http.get<Product[]>(API_URL + 'products/newrelease');
  }

  getFiltered(params: {
    releaseDate?: string,
    authors?: string[],
    publishers?: string[],
    minPrice?: number,
    maxPrice?: number,
    newrelease?: boolean,
    bestSeller?: boolean,
    genres?: string[],
    active: boolean,
    page: number,
    size: number,
    sort: string,
    search?: string
  }): Observable<any> {
    let queryParams = new HttpParams();

    if (params.releaseDate) {
      queryParams = queryParams.append('releaseDate', params.releaseDate)
    }
    if (params.authors && params.authors.length > 0) {
      queryParams = queryParams.append('authors', params.authors.join(','))
    }
    if (params.publishers && params.publishers.length > 0) {
      queryParams = queryParams.append('publishers', params.publishers.join(','))
    }
    if (params.minPrice !== undefined) {
      queryParams = queryParams.append('minPrice', String(params.minPrice))
    }
    if (params.maxPrice !== undefined) {
      queryParams = queryParams.append('maxPrice', String(params.maxPrice))
    }
    if (params.newrelease !== undefined) {
      queryParams = queryParams.append('newrelease', String(params.newrelease))
    }
    if (params.bestSeller !== undefined) {
      queryParams = queryParams.append('bestSeller', String(params.bestSeller))
    }
    if (params.genres && params.genres.length > 0) {
      queryParams = queryParams.append('genres', params.genres.join(','))
    }
    if (params.search) {
      queryParams = queryParams.append('search', params.search)
    }
    queryParams = queryParams.append('sort', params.sort)
    queryParams = queryParams.append('size', params.size)
    queryParams = queryParams.append('page', params.page)

    return this.http.get<any>(API_URL + 'products/filter', { params: queryParams })
  }

  getFilterFields(): Observable<any> {
    return this.http.get<any>(API_URL + 'products/filter-fields')
  }

  getAvailableInventory(id: number): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(API_URL + 'products/inventory/' + id);
  }

  getRelatedProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(API_URL + "products/related/" + this.productId)
  }

  triggerReinitialize() {
    this.reinitializeEvent.emit();
  }

  updateProductId(id?: number) {
    this.productId = id
  }
}

