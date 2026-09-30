import { Component, OnInit } from '@angular/core';
import {Product} from "../../interfaces/product";
import {ProductService} from "../../services/products.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  newReleases: Product[] = []
  topProducts: Product[] = []

  constructor(public productService: ProductService) { }

  ngOnInit(): void {
    this.productService.getBestSellers().subscribe({
      next: (response: Product[]) => this.topProducts = response
    })
    this.productService.getNewReleases().subscribe({
      next: (resp) => this.newReleases = resp
    })
  }

}
