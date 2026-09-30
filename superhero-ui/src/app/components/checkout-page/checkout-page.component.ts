import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import * as _ from 'lodash';

@Component({
  selector: 'app-checkout-page',
  templateUrl: './checkout-page.component.html',
  styleUrls: ['./checkout-page.component.css'],
})
export class CheckoutPageComponent implements OnInit {
  superhero!: string;
  heroOptions: string[] = [
    'assets/dr-doom.webp',
    'assets/batman.png',
    'assets/iron-man.png',
    'assets/spiderman.png',
    'assets/superman.png',
  ];
  loading = false;

  constructor(public cartService: CartService) {}

  getRandomHero() {
    const randNum: number = _.random(0, 4);
    return this.heroOptions[randNum];
  }

  ngOnInit(): void {
    this.loading = true;
    this.superhero = this.getRandomHero();
    setTimeout(() => this.loading = false, 1000);
  }
}
