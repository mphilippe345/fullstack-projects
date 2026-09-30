import {Component, Input, OnInit} from '@angular/core';
import {ProductService} from "../../services/products.service";
import {Product} from "../../interfaces/product";

@Component({
  selector: 'app-related-products',
  templateUrl: './related-products.component.html',
  styleUrls: ['./related-products.component.css'],
})
export class RelatedProductsComponent implements OnInit {

  @Input() products: Product[] = [];

  @Input() stockStatus?: boolean;

  currentIndex = 0;

  itemWidth = 138;

  topProducts: Product[] = [];

  constructor(public productService: ProductService) {
  }

  ngOnInit(): void {
    this.productService.getBestSellers().subscribe({
      next: (response: Product[]) => this.topProducts = response
    })
    this.productService.reinitializeEvent.subscribe({
      next: () => {
        this.currentIndex = 0
      }
    })
  }

  prevSlide() {
    this.currentIndex = Math.max(this.currentIndex - 1, 0);
  }

  nextSlide() {
    if (this.products.length == 4) this.itemWidth = 32;
    this.currentIndex = Math.min(this.currentIndex + 1, this.products.length - 1);
  }

}
