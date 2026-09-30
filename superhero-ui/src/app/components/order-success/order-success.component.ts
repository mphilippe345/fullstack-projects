import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-order-success',
  templateUrl: './order-success.component.html',
  styleUrls: ['./order-success.component.css']
})
export class OrderSuccessComponent implements OnInit {
  orderNumber!: string;
  name!: string;
  email!: string;
  checkIcon: string = "../../../assets/green-check.svg";
  loading = false;

  constructor(public router: Router, private route: ActivatedRoute) {
    const { extras } = this.router.getCurrentNavigation()!;
    this.name = extras.state?.['response'].firstName as string;
    this.email = extras.state?.['response'].email as string;
  }

  ngOnInit(): void {
    this.loading = true;
    this.orderNumber = this.route.snapshot.paramMap.get('orderNumber')!;
    setTimeout(() => this.loading = false, 1000);
  }

}
