import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../interfaces/product';
import { API_URL } from '../constants/constants';

@Injectable({
  providedIn: 'root'
})
export class GenreService {

  constructor(private http: HttpClient) { }

  getProductsByGenre(genre: string): Observable<Product[]> {
      return this.http.get<Product[]>( `${API_URL}products/genre/${genre}`);
  }
}
