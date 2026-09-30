import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../../interfaces/product';

@Component({
  selector: 'app-ripped-receipt',
  templateUrl: './ripped-receipt.component.html',
  styleUrls: ['./ripped-receipt.component.css']
})
export class RippedReceiptComponent implements OnInit {
  @Input() products!: Product[];
  @Input() orderTotal!: number;

  constructor() { }

  ngOnInit(): void {
  }

}
