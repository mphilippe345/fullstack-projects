import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable} from 'rxjs';
import { API_URL } from '../constants/constants';
import { PromoCode } from '../interfaces/promoCode';


@Injectable({
  providedIn: 'root',
})
export class PromoService {
  constructor(private http: HttpClient) {}

  private promoCode: string = '';

  getAllPromos(): Observable<PromoCode[]> {
    return this.http.get<PromoCode[]>(API_URL + 'promos');
  }

  setPromoCode(promoCode: string): void {
    this.promoCode = promoCode;
  }

  clearPromoCode(): void {
    this.promoCode = '';
    localStorage.setItem('promo', '');
  }

  getPromoCode(): string {
    return this.promoCode;
  }
}
