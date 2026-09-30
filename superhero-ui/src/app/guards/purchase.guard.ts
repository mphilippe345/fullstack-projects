import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Purchase } from '../interfaces/purchase';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class PurchaseGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const purchase = localStorage.getItem('purchase')!;

    return purchase != '' ? true : this.router.navigate([""])
  }
}
