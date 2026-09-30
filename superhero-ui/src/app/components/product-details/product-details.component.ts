import {Component, DoCheck, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ProductService} from '../../services/products.service';
import {CartService} from 'src/app/services/cart.service';
import {ReviewService} from 'src/app/services/review.service';
import {Product} from "../../interfaces/product";
import {Inventory} from "../../interfaces/inventory";
import {CONDITIONS} from "../../constants/constants";
import {ReviewModalComponent} from "../review-modal/review-modal.component";
import {MatDialog} from "@angular/material/dialog";
import {Subscription} from "rxjs";

@Component({
  selector: "app-product-details",
  templateUrl: "./product-details.component.html",
  styleUrls: ["./product-details.component.css"],
})
export class ProductDetailsComponent implements OnInit, OnDestroy, DoCheck {
  rating: any;
  product!: Product;
  inventoryItems!: Inventory[];
  conditions!: string[];
  selectedCondition!: string;
  selectedProductPrice!: number;
  private reinitializeSubscription?: Subscription;
  relatedProducts: Product[] = [];
  reviewArray!: any;
  currentSort!: string;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    public cartService: CartService,
    private ratingService: ReviewService,
    private dialog: MatDialog) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.productService.updateProductId(params['id'])
      this.productService.getProductById(id).subscribe((response) => {
        this.product = response;
      });
      this.productService.getAvailableInventory(id).subscribe((response) => {
        this.inventoryItems = response;
        const unorderedConditions = response.map((item) => item.condition);
        const conditionsList = CONDITIONS.map((condition) => condition.name);
        this.conditions = unorderedConditions.sort(
          (a, b) => conditionsList.indexOf(a) - conditionsList.indexOf(b)
        );
        this.selectedCondition = this.conditions[0];
      });
      this.ratingService.getAverageByProductId(id).subscribe((response) => {
        this.rating = response;
      });
    });
    this.productService.getRelatedProducts().subscribe({
      next: (resp) => this.relatedProducts = resp
    })
    this.productService.reinitializeEvent.subscribe({
      next: () => {
        this.productService.getRelatedProducts().subscribe({
        next: (resp) => this.relatedProducts = resp
      })
      }
    })
  }

  ngOnDestroy() {
    this.reinitializeSubscription?.unsubscribe();
  }

  ngDoCheck() {

  }

  getConditionPrice(price: number) {
    if (!this.inventoryItems) return price;

    const selectedProduct = this.inventoryItems.find(
      (item) => item.condition == this.selectedCondition
    )!;
    const selectedPrice = selectedProduct.rate * price;
    this.selectedProductPrice = selectedPrice;

    return selectedPrice;
  }

  // NOTE FOR DISCUSSION W/ PO: price for product being added is correct (was off by a bit because we were using the
  // currency pipe without rounding instead of setting fixed decimal precision first)
  // and the total on the checkout at the bottom is
  // correct, but the cart doesn't separate different versions (conditions) of what's added and the total shown on
  // the cart item is not correct. Do we show each version of the product that's added so that they are separate and do
  // we show some kind of indication that they are different conditions??
  addToCart(product: Product): void {
    const productToAdd: Product = {
      ...product,
      condition: this.selectedCondition,
    };
    if (this.selectedProductPrice != product.price) {
      productToAdd.price = this.selectedProductPrice;
    }

    this.cartService.addToCartWithToast(productToAdd);
  }

  onSelectSort(event: any) {
    const id = this.route.snapshot.params["id"];
    this.currentSort = event.value;

    this.ratingService
      .getReviewsByProductId(id, this.currentSort)
      .subscribe((response) => (this.reviewArray = response));
  }
  /**
   * Opens the modal to submit a review for the product.
   */
  openReviewDialog() {
    this.dialog.open(ReviewModalComponent, {
      width: '500px',
      data: {
        product: this.product
      }
    })

  }
}
