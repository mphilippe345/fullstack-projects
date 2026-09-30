import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/interfaces/product';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationModalComponent } from '../confirmation-modal/confirmation-modal.component';
import { CartService } from 'src/app/services/cart.service';
import { PromoCode } from 'src/app/interfaces/promoCode';
import { PromoService } from 'src/app/services/promo.service';
import { GiftCardService } from 'src/app/services/giftCard.service';
import { Router } from '@angular/router';
import { CONDITIONS } from "../../constants/constants";

@Component({
  selector: 'app-checkout-page-cart',
  templateUrl: './checkout-page-cart.component.html',
  styleUrls: [ './checkout-page-cart.component.css' ],
})
export class CheckoutPageCartComponent implements OnInit {
  products: Product[] = [];

  promoCode!: string;
  promoPrice!: number;

  itemInCartDiscountedPrice!: number;

  validPromos: PromoCode[] = [];

  discountsApplied: boolean = false;

  invalidPromo: boolean = false;

  discountRate!: number;

  giftCardDeduction: number = 0;

  giftCardDeductionModel: number = 0;


  constructor(
    public dialog: MatDialog,
    public cartService: CartService,
    public promoCodeService: PromoService,
    public giftCardService : GiftCardService,
    public router: Router,
  ) {
  }

  ngOnInit(): void {
    this.products = this.cartService.getCartForCheckoutPage();
    this.promoCode = '';

    this.giftCardService.currentModel.subscribe(value => {
      this.giftCardDeductionModel = value;
    })
    // getByPromoCode()??
    this.promoCodeService.getAllPromos().subscribe((promos) => {
      this.validPromos = promos;

      this.promoCode = localStorage.getItem('promo') || '';
      if (this.promoCode !== '') {
        this.applyPromo();
      }
    });
  }

  openDialogForEmptyCart(): void {
    this.dialog.open(ConfirmationModalComponent, {
      data: {
        title: 'Empty Cart',
        description: 'This will remove all items from your cart, are you sure?',
        option1: 'Yes',
        option2: 'No',
        func: () => {
          this.router.navigate([ 'products' ]);
          sessionStorage.clear();
          this.cartService.clearCart();
          this.clearPromoCode();
          this.promoCodeService.clearPromoCode();
        },
      },
    });
  }

  openDialogForRemoveItemFromCart(product: Product): void {
    let currentProductsInCart: Number = this.cartService.getItemCount();

    this.dialog.open(ConfirmationModalComponent, {
      data: {
        title: 'Remove Item From Your Cart',
        description: 'This will remove this item from your cart. Are you sure?',
        option1: 'Yes',
        option2: 'No',
        func: () => {
          this.cartService.removeAllFromCartById(product);
          if (this.discountsApplied) {
            this.applyPromo();
          }
          if (currentProductsInCart == 1) {
            this.clearPromoCode();
          }
        },
      },
    });
  }

  applyPromo() {
    let currentProductsInCart: Number = this.cartService.getItemCount();

    if (this.isValidPromoCode(this.promoCode) && currentProductsInCart != 0) {
      this.discountRate = this.getPromoRate(this.promoCode) * 100;
      this.promoPrice = this.calculateDiscountedPrice(this.promoCode);
      this.discountsApplied = true;
      this.cartService.setDiscountedTotal(this.promoPrice);
      this.invalidPromo = false;
      localStorage.setItem('promo', this.promoCode);
    } else if (
      !this.isValidPromoCode(this.promoCode) &&
      currentProductsInCart == 1
    ) {
      this.clearPromoCode();
      this.invalidPromo = true;
    } else {
      this.invalidPromo = true;
    }
  }

  calculateDiscountedPrice(code: string): number {
    const percentageOff: number = this.getPromoRate(code);
    let discountedPrice = 0;

    let totalPriceOfRegularProducts = 0;

    for (const product of this.products) {
      const hasCode: boolean = product.promos
        .map((promo) => promo.title)
        .includes(code);
      const productQuantity = this.cartService.getProductQuantityByIdAndPrice(product.id, product.price);

      if (hasCode) {
        const discountAmount = percentageOff * product.price;
        const itemDiscountedPrice = product.price - discountAmount;
        const subtotal = itemDiscountedPrice * productQuantity;
        discountedPrice += subtotal;
      } else {
        const subtotal = product.price * productQuantity;
        totalPriceOfRegularProducts += subtotal;
      }
    }

    return parseFloat(discountedPrice.toFixed(2)) + totalPriceOfRegularProducts;
  }

  isValidPromoCode(code: string): boolean {
    let currentProductsInCart: Product[] =
      this.cartService.getCartForCheckoutPage();

    const validPromoCodes: string[] = this.validPromos.map(
      (promo) => promo.title,
    );
    const isValidPromoCode: boolean = validPromoCodes.includes(code);

    // is 1 applicable product different than many??
    if (currentProductsInCart.length == 1) {
      const applicableProducts: Product[] = currentProductsInCart.filter(
        (item) => {
          return item.promos.map((promo) => promo.title).includes(code);
        },
      );

      if (applicableProducts.length === 0) {
        return false;
      }
    }

    if (isValidPromoCode) {
      const applicableProducts: Product[] = this.products.filter((item) => {
        return item.promos.map((promo) => promo.title).includes(code);
      });

      if (applicableProducts.length === 0) {
        return false;
      }
    }

    return isValidPromoCode; // why not return false sooner?... making notes so i know how this works lol :)
  }

  isDiscountedProduct(id: number): boolean {
    const applicableProducts: Product[] = this.products.filter((item) => {
      return item.promos.map((promo) => promo.title).includes(this.promoCode);
    });

    if (this.discountsApplied) {
      for (const product of applicableProducts) {
        if (product.id == id) {
          this.itemInCartDiscountedPrice = this.calculateDiscountProductPrice(
            this.promoCode,
            product,
          );
          return true;
        }
      }
    }
    return false;
  }

  getPromoRate(code: string): number {
    const promo = this.validPromos.find((promo) => promo.title === code);
    const rate = promo ? parseFloat(promo.rate) : 0;

    return rate;
  }

  calculateDiscountProductPrice(code: string, product: Product): number {
    const percentageOff: number = this.getPromoRate(code);

    const discountAmount = percentageOff * product.price;
    const discountedPrice = product.price - discountAmount;

    const roundedDiscountedPrice = Number(discountedPrice.toFixed(2));

    return roundedDiscountedPrice;
  }

  clearPromoCode() {
    this.promoCode = '';
    this.promoPrice = 0;
    this.discountsApplied = false;
    this.promoCodeService.clearPromoCode();
  }

  getGiftCard(){
    this.giftCardService.currentGiftCardCharge.subscribe(value => {this.giftCardDeduction = value;
    });
  }

  getChipColor(condition: string) {
    return CONDITIONS.find(cond => cond.name === condition)?.color!
  }

  formatChip(condition: string) {
    return condition.includes('-') ? condition.replace('-', ' ') : condition;
  }
}
