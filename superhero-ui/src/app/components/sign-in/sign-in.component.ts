import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user';
import { CartService } from 'src/app/services/cart.service';
import { CognitoService } from 'src/app/services/cognito.service';
@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  user: User | undefined;
  alertMessage: string = '';
  showAlert: boolean = false;
  isForgotPassword: boolean = false;
  newPassword: string = '';

  constructor( private router:Router, private cognitoService:CognitoService, private cartService:CartService) { }

  ngOnInit(): void {
    this.user = {} as User;
  }

  signInWithCognito() {
    if (this.user && this.user.email && this.user.password) {
      this.cognitoService.signIn(this.user)
        .then(() => {
          this.cartService.syncCart();
          this.router.navigate(['/user']);
        })
        .catch((error: any) => {
          this.displayAlert(error.message);
        })
    }else {
      this.displayAlert("Please Enter a valid email address or password")
    }
  }

  forgotPasswordClicked() {
    if (this.user && this.user.email) {
      this.cognitoService.forgotPassword(this.user)
        .then(() => {
          this.isForgotPassword = true;
        })
        .catch((error: any) => {
        this.displayAlert(error.message);
      })
    } else {
      this.displayAlert("Please Enter a valid email address")
    }
  }

  newPasswordSubmit() {
    if (this.user && this.user.verficationCode && this.newPassword.trim().length != 0) {
      this.cognitoService.forgotPasswordSubmit(this.user, this.newPassword.trim())
        .then(() => {
          this.displayAlert("Password Updated");
          this.isForgotPassword = false;
        })
        .catch((error: any) => {
        this.displayAlert(error.message);
      })
    } else {
         this.displayAlert("Please enter Valid input")
      }
  }

   private displayAlert(message: string) {
    this.alertMessage = message;
    this.showAlert = true;
   }
  
  signInWithGoogle(): void {
    this.cognitoService.googleSocialSignIn();
  }
}
