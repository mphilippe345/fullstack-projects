import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CartService } from 'src/app/services/cart.service';
import {ProductService} from "../../services/products.service";
import {Product} from "../../interfaces/product";

@Component({
  selector: 'app-comic-card',
  templateUrl: './comic-card.component.html',
  styleUrls: ['./comic-card.component.css']
})
export class ComicCardComponent implements OnInit {
  // imports product from products-page
  @Input() product!: Product;

  @Input() topProducts: Product[] = []

  constructor(private toast: ToastrService, public cartService: CartService, private productService: ProductService) { }

  ngOnInit() {
  }

  getProductIndex(product: Product): number {
    return (this.topProducts.findIndex(productInArray => productInArray.id === product.id)) + 1
  }

  onCardClick() {
    this.productService.updateProductId(this.product.id)
    this.productService.triggerReinitialize();
  }

}

// For whomever does the wishlist, use <mat-icon>favorite</mat-icon> for when
// the favorites button is clicked if you want to keep the original icon.
