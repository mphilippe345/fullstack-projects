import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { API_URL } from "../constants/constants";
import { CartItem } from '../interfaces/cart';
import { Product } from '../interfaces/product';
import { CognitoService } from '../services/cognito.service';
import { ProductService } from './products.service';
import { PromoService } from './promo.service';
import { GiftCardService } from './giftCard.service';
@Injectable({
  providedIn: 'root',
})
export class CartService {
  cartProducts: Product[] = [];
  discountedTotal!: number;
  user: any;
  constructor(
    private http: HttpClient,
    private cognitoService: CognitoService,
    private toast: ToastrService,
    private productService: ProductService,
    private promoService: PromoService,
    private giftCardService : GiftCardService,
    private router: Router,
  ) {
  }

  saveCart() {
    localStorage.setItem('cart', JSON.stringify(this.cartProducts));
  }

  addToCart(product: Product) {
    this.cartProducts.push(product);
    this.saveCart();
    this.cognitoService.isLoggedIn().then(isLogged => {
      if (isLogged) {
        this.createCartByEmailId(this.user.email, product.id).subscribe(
      )
        }
    })
  }

  addToCartWithToast(product: Product) {
    const cartIcon = document.getElementById('cartIcon');
    cartIcon!.style.animation = 'shake .4s ease-in-out forwards';

    this.addToCart(product);
    setTimeout(() => cartIcon!.style.removeProperty('animation'), 400);
  }

  loadCart() {
    this.cartProducts = JSON.parse(localStorage.getItem('cart') as any) || [];
    this.promoService.setPromoCode(localStorage.getItem('promo') || '');
  }

  productInCart(productInCart: Product) {
    return (
      this.cartProducts.findIndex(
        (product: Product) => productInCart.id === product.id,
      ) > -1
    );
  }

  removeProduct(product: Product) {
    this.saveCart();
    this.loadCart();
    const index = this.cartProducts.findIndex(
      (cartProduct: Product) => product.id === cartProduct.id,
      );
    if (index > -1) {
      this.cartProducts.splice(index, 1);
      this.saveCart();
    }
    this.cognitoService.isLoggedIn().then(isLogged => {
      if (isLogged) {
        this.deleteCartByEmailId(this.user.email,product.id ).subscribe(
       )
        }
    })
  }

  clearCart() {
    this.cartProducts.splice(0, this.cartProducts.length);
    this.saveCart();
    this.cognitoService.isLoggedIn().then(isLogged => {
      if (isLogged) {
        this.deleteCartByEmail(this.user.email).subscribe(
         )
        }
    })
    this.toast.success('Shopping cart was successfully emptied.');
  }

  getItemCount() {
    return this.cartProducts.length;
  }

  getTotal() {
    let total: number = 0;
    this.cartProducts.forEach((product) => (total += Number(product.price.toFixed(2))));
    return total;
  }

  getTotalMinusGiftCard (){
    let giftCardDeduction: number = 0; 
    this.giftCardService.currentModel.subscribe(value => {
      giftCardDeduction = value;
    })
    let deductedTotal: number = 0;
    deductedTotal = this.getTotal();
    deductedTotal = deductedTotal - giftCardDeduction;
    return deductedTotal;
  }

  setDiscountedTotal(value: number) {
    this.discountedTotal = value;
  }

  getCartForCheckoutPage() {
    let newArray = this.cartProducts;
    return newArray.filter(
      (element, index) => newArray.findIndex(
        (product) => product.id === element.id && product.price === element.price
      ) === index,
    );
  }

  getProductQuantityByIdAndPrice(id?: number, price?: number) {
    let quantity = 0;
    this.cartProducts.forEach((product) => {
      if (product.id === id && product.price === price) {
        quantity++;
      }
    });
    return quantity;
  }

  removeAllFromCartById(product: Product) {
    this.saveCart()
    this.cartProducts.forEach((cartProduct) => {
      if (product.id == cartProduct.id) {
        this.removeProduct(cartProduct);
      }
    });

    if (this.cartProducts.length == 0) {
      this.router.navigate([ 'products' ]);
      sessionStorage.clear();
    }
    this.toast.success(
      `${ product.title } was successfully removed from your shopping cart.`,
    );
  }

  syncCart() {
    this.cognitoService.isLoggedIn().then(isLoggedIn => {
      if (isLoggedIn) {
        this.getUserDetails()
        const localCart = this.getLocalCart();
        if (localCart && localCart.length > 0) {
        // Clear local strage and sync with backend
        localStorage.removeItem('cart');
        this.cartProducts = this.getLocalCart();
        this.loadCartByEmail(this.user.email);
      } else {
        this.loadCartByEmail(this.user.email);
      }
      } else {
        //Continue to using local storage
        this.cartProducts = this.getLocalCart();
      }
    })
  }
  getLocalCart(): any[] {
    const localCart = localStorage.getItem('cart');
    return localCart ? JSON.parse(localCart) : [];
  }
  cleanLocalCart() {
    localStorage.removeItem('cart');
    this.cartProducts = this.getLocalCart();

  }
  getCartByEmail(email: string): Observable<CartItem[]> {
      return this.http.get<CartItem[]>(API_URL + "cart/" +email);
  }

  loadCartByEmail(userEmail:string) {
     this.getCartByEmail(userEmail).subscribe(
          (cartItems: any [] ) => {
         let tempProduct: any[] = [];
         cartItems.forEach(item => {
           for (let i = 0; i < item.quantity; i++) {
             tempProduct.push({
               ...item.product,
               condition:'new'
             })
           }
         })
         this.cartProducts = tempProduct;
         this.saveCart()
       });
  }
  getUserDetails() {
    this.cognitoService.getUser()
      .then((user: any) => {
        if (user) {
          //logged in
          this.user = user.attributes;
        }
        }) ;
  }
   createCartByEmailId(email: string, productId?:number): Observable<any> {
     return this.http.post<any>(API_URL + "cart/" + email + "/" + productId, null);
  }
  deleteCartByEmailId(email: string, productId?: number): Observable<any> {
    return this.http.delete<any>(API_URL + "cart/" + email + "/" + productId);
  }
  deleteCartByEmail(email: string,): Observable<any> {
    return this.http.delete<any>(API_URL + "cart/" + email);
  }
}
