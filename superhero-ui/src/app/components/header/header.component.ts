import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  constructor(public cartService: CartService, private router: Router) { }

  ngOnInit(): void {
  }

  navigate() {
    this.router.navigate(['confirmation']);
    return false;
  }
}
