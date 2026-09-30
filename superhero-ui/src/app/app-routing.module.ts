import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CheckoutPageComponent } from './components/checkout-page/checkout-page.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { OrderConfirmationComponent } from './components/order-confirmation/order-confirmation.component';
import { OrderSuccessComponent } from './components/order-success/order-success.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ProductsPageComponent } from './components/products-page/products-page.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { TestPageComponent } from './components/test-page/test-page.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { OrderSuccessGuard } from './guards/order-success.guard';
import { PurchaseGuard } from './guards/purchase.guard';

const routes: Routes = [
  { path: 'test-page', component: TestPageComponent },
  { path: 'home', component: HomePageComponent},
  { path: 'checkout', component: CheckoutPageComponent },
  { path: 'confirmation', component: OrderConfirmationComponent, canActivate: [PurchaseGuard] },
  { path: 'success/:orderNumber', component: OrderSuccessComponent, canActivate: [OrderSuccessGuard] },
  { path: 'user', component: UserPageComponent},
  { path: 'products', component: ProductsPageComponent },
  { path: 'products/:id', component: ProductDetailsComponent },
  { path: 'sign-in', component: SignInComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: '', component: HomePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
