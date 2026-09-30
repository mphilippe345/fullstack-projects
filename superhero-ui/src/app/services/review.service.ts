import {HttpClient, HttpParams} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

import {API_URL} from "../constants/constants";
import {Review} from "../interfaces/review";

@Injectable({
  providedIn: "root",
})
export class ReviewService {
  constructor(private http: HttpClient) {}

  getReviewsByProductId(id: number, sort?: string): Observable<Review> {
    let queryParams = new HttpParams()
    queryParams = queryParams.append('sort', sort ? sort : '')
    return this.http.get<Review>(API_URL + "reviews/" + "product/" + id, {params : queryParams});
  }

  getAverageByProductId(id: number): Observable<Review> {
    return this.http.get<Review>(API_URL + "reviews/" + id + "/average");
  }

  getSortedReviewsByProductId(
    id: number,
    sortValue: string
  ): Observable<Review> {
    return this.http.get<Review>(API_URL + "reviews/" + id + "/" + sortValue);
  }
}
