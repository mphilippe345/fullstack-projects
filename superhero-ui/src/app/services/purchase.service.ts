import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Purchase } from "../interfaces/purchase";
import { API_URL } from "../constants/constants";
import { Order } from "../interfaces/order";
import { Page } from "../interfaces/page";

@Injectable({
  providedIn: "root",
})
export class PurchaseService {
  constructor(private http: HttpClient) {}

  createPurchase(purchase: Purchase): Observable<Purchase> {
    return this.http.post<Purchase>(API_URL + "purchases", purchase);
  }

  getOrdersByEmail(
    email: string,
    page: number,
    size: number
  ): Observable<Page> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("page", page);
    queryParams = queryParams.append("size", size);

    return this.http.get<Page>(API_URL + "purchases/" + email, {
      params: queryParams,
    });
  }
}
