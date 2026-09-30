import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { formatPhoneNumber } from "../../helpers/phoneNumberFormatter";
import { Address } from "../../interfaces/address";
import { CreditCard } from "../../interfaces/credit-card";
import { PersonalInfo } from "../../interfaces/personal-info";
import { Product } from "../../interfaces/product";
import { Purchase } from "../../interfaces/purchase";
import { CartService } from "../../services/cart.service";
import { PurchaseService } from "../../services/purchase.service";
import { ConfirmationModalComponent } from "../confirmation-modal/confirmation-modal.component";

@Component({
  selector: "app-order-confirmation",
  templateUrl: "./order-confirmation.component.html",
  styleUrls: ["./order-confirmation.component.css"],
})
export class OrderConfirmationComponent implements OnInit {
  purchase!: Purchase;
  personalInfo!: PersonalInfo;
  billingAddress!: Address;
  shippingAddress!: Address;
  creditCard!: CreditCard;
  phoneNumber!: string;
  orderTotal!: number;
  giftCardCharge!: number;
  giftCardCode!: string;

  products: Product[] = [];
  clicked: boolean = false;
  loading = false;
  processing = false;

  constructor(
    private router: Router,
    private cartService: CartService,
    private purchaseService: PurchaseService,
    private toast: ToastrService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loading = true;
    try {
      this.purchase = JSON.parse(
        localStorage.getItem("purchase")!
      ) as unknown as Purchase;
    } catch (err) {
      console.error((err as Error).message);
    }

    this.personalInfo = this.purchase.personalInfo;
    this.billingAddress = this.purchase.billingAddress;
    this.shippingAddress = this.purchase.shippingAddress;
    this.creditCard = this.purchase.creditCard;
    this.orderTotal = this.purchase.orderTotal;
    this.phoneNumber = formatPhoneNumber(this.personalInfo.phoneNumber, true);

    try {
      this.products = JSON.parse(localStorage.getItem("cart")!);
    } catch (err) {
      console.error((err as Error).message);
    }

    setTimeout(() => (this.loading = false), 1000);
  }

  handleCleanUp(tstMsg: string): void {
    this.cartService.clearCart();
    this.cartService.cartProducts.splice(
      0,
      this.cartService.cartProducts.length
    );
    this.cartService.saveCart();
    localStorage.setItem("purchase", "");
    sessionStorage.clear();
    this.toast.success(tstMsg);
  }

  confirmOrder(): void {
    this.clicked = true;
    this.processing = true;
    this.purchaseService.createPurchase(this.purchase).subscribe({
      next: (response) => {
        setTimeout(() => {
          this.router.navigate(
            ["/success", response.orderNumber],
            {
              state: { response },
            } ?? null
          );
        }, 1500);
        this.handleCleanUp("Order confirmed, purchase successful!");
      },
      error: (error) => console.error(error),
      complete: () => this.processing = false
    });
  }

  cancelOrder(): void {
    this.dialog.open(ConfirmationModalComponent, {
      data: {
        title: "Cancel Order",
        description: "Are your sure you want to cancel your order?",
        option1: "Yes",
        option2: "No",
        func: () => {
          this.handleCleanUp("Order was cancelled!");
          setTimeout(() => this.router.navigate(["/"]));
        },
      },
    });
  }
}
