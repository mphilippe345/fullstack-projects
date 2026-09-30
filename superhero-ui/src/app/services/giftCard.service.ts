import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { GiftCard } from "../interfaces/gift-card";
import { API_URL } from "../constants/constants";
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: "root",
})
export class GiftCardService {
  constructor(private http: HttpClient) {}

  private giftCardChargeSource = new BehaviorSubject<number>(0);
  currentGiftCardCharge = this.giftCardChargeSource.asObservable();

  modelSource = new BehaviorSubject<number>(0);
  currentModel = this.modelSource.asObservable();

  setGiftCardCharge(value: number) {
    this.giftCardChargeSource.next(value);
  }

  getGiftCard(giftCardCode: string): Observable<GiftCard> {
    return this.http.get<GiftCard>(API_URL + "gift-cards/code/" + giftCardCode);
  }

  updateModel(value: number) {
    this.modelSource.next(value);
  }

}
