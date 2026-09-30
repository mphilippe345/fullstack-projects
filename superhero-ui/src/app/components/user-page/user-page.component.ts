import { Component, OnDestroy, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { User } from "src/app/interfaces/user";
import { CartService } from "src/app/services/cart.service";
import { CognitoService } from "src/app/services/cognito.service";
import { PurchaseService } from "../../services/purchase.service";
import { MatTabChangeEvent } from "@angular/material/tabs";
import { Subscription } from "rxjs";
import { Page } from "../../interfaces/page";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-user-page",
  templateUrl: "./user-page.component.html",
  styleUrls: ["./user-page.component.css"],
})
export class UserPageComponent implements OnInit, OnDestroy {
  subscription!: Subscription;
  user!: User;
  defaultTabIndex = 0;
  page!: Page;
  loading = false;

  constructor(
    private router: Router,
    private cognitoService: CognitoService,
    private cartService: CartService,
    private purchaseService: PurchaseService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.user = {} as User;
    this.getUserDetails();
    this.cartService.getUserDetails();
  }

  ngOnDestroy() {
    if (this.subscription) this.subscription.unsubscribe();
  }

  onTabChange(event: MatTabChangeEvent) {
    if (event.index == 1) {
      this.loading = true;
      this.subscription = this.purchaseService
        .getOrdersByEmail(this.user.email, 0, 10)
        .subscribe({
          next: (ordersPage) => this.updateOrderCount(ordersPage),
          error: (err) => {
            this.toast.error(
              "Server error while retrieving past orders. Please try again later!"
            );
            this.loading = false;
          },
          complete: () => setTimeout(() => (this.loading = false), 1000),
        });
    }
  }

  private getUserDetails() {
    this.cognitoService.getUser().then((user: any) => {
      if (user) {
        this.user = user.attributes;
        this.user.role = user.attributes["custom:role"];
      } else {
        this.router.navigate(["/sign-in"]);
      }
    });
  }

  signOutWithCognito() {
    this.cognitoService.signOut().then(() => {
      this.cartService.cleanLocalCart();
      this.getUserDetails();
      this.cartService.getUserDetails();
      this.router.navigate(["/home"]);
    });
  }

  updateOrderCount(page: Page) {
    this.page = page;
  }
}
