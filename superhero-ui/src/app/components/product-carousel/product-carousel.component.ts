import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {Product} from "../../interfaces/product";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-product-carousel',
  templateUrl: './product-carousel.component.html',
  styleUrls: ['./product-carousel.component.css'],
})
export class ProductCarouselComponent implements OnInit {

  @Input() products: Product[] = []

  @Input() header: string = ''

  @Input() headerColor: string = ''

  @Input() fontColor: string = ''

  constructor() { }

  ngOnInit(): void {
  }

}
