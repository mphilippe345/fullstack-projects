import { Component, OnInit } from '@angular/core';
import { TestService } from 'src/app/services/test-service.service';
import { UserService } from 'src/app/services/user.service';
import { Product } from 'src/app/interfaces/product';
import { User } from 'src/app/interfaces/user';
import {ProductService} from "../../services/products.service";

@Component({
  selector: 'app-test-page',
  templateUrl: './test-page.component.html',
  styleUrls: ['./test-page.component.css']
})
export class TestPageComponent implements OnInit {

  comics?: any;
  users?: User[];
  user?: User;
  userId:number = 2;
  show: boolean = false;
  products: Product[] = []

  constructor(private testClient: TestService, public userClient: UserService, private productService: ProductService) { }

  ngOnInit(): void {
    // this.testClient.getAllComics().subscribe({
    //   next: (resp) => this.comics = resp
    // })

    // this.userClient.getAllUsers().subscribe({
    //   next: (resp) => this.users = resp
    // })
  }

  deleteUser(id: number) {
    this.userClient.deleteUserById(id).subscribe({
      next: () => console.log('user by ID ' + id + ' was deleted.')
    })
  }

  getUserById(id: number) {
    this.userClient.getUserById(id).subscribe({
      next: (user) => this.user = user
    })
  }

  changeState() {
    this.show = !this.show
  }
}
