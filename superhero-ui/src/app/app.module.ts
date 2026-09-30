import { SocialLoginModule } from "@abacritt/angularx-social-login";
import { CommonModule, NgOptimizedImage } from "@angular/common";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatBadgeModule } from "@angular/material/badge";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatNativeDateModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatDividerModule } from "@angular/material/divider";
import {
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MatFormFieldModule,
} from "@angular/material/form-field";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatSelectModule } from "@angular/material/select";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatStepperModule } from "@angular/material/stepper";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTooltipModule } from "@angular/material/tooltip";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgxSliderModule } from "@angular-slider/ngx-slider";
import { CdkMenuModule } from "@angular/cdk/menu";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatTabsModule } from "@angular/material/tabs";
import { AmplifyAuthenticatorModule } from "@aws-amplify/ui-angular";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { ToastrModule } from "ngx-toastr";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { CheckoutPageCartComponent } from "./components/checkout-page-cart/checkout-page-cart.component";
import { CheckoutPageComponent } from "./components/checkout-page/checkout-page.component";
import { CheckoutStepperComponent } from "./components/checkout-stepper/checkout-stepper.component";
import { ComicCardComponent } from "./components/comic-card/comic-card.component";
import { ConfirmationModalComponent } from "./components/confirmation-modal/confirmation-modal.component";
import { CreditCardComponent } from "./components/credit-card/credit-card.component";
import { FooterComponent } from "./components/footer/footer.component";
import { GenresSliderComponent } from "./components/genres-slider/genres-slider.component";
import { HeaderComponent } from "./components/header/header.component";
import { HomePageComponent } from "./components/home-page/home-page.component";
import { LoginComponent } from "./components/login/login.component";
import { MessageModalComponent } from "./components/message-modal/message-modal.component";
import { NotFoundComponent } from "./components/not-found/not-found.component";
import { OrderConfirmationComponent } from "./components/order-confirmation/order-confirmation.component";
import { OrderSuccessComponent } from "./components/order-success/order-success.component";
import { NextButtonDirective } from "./components/product-carousel/next-button.directive";
import { PrevButtonDirective } from "./components/product-carousel/prev-button.directive";
import { ProductCarouselComponent } from "./components/product-carousel/product-carousel.component";
import { ProductDetailsComponent } from "./components/product-details/product-details.component";
import { ProductsPageComponent } from "./components/products-page/products-page.component";
import { RelatedProductsComponent } from "./components/related-products/related-products.component";
import { ReviewModalComponent } from "./components/review-modal/review-modal.component";
import { ReviewComponent } from "./components/review/review.component";
import { RippedReceiptComponent } from "./components/ripped-receipt/ripped-receipt.component";
import { SignInComponent } from "./components/sign-in/sign-in.component";
import { SignUpComponent } from "./components/sign-up/sign-up.component";
import { StarRatingComponent } from "./components/star-rating/star-rating.component";
import { TestPageComponent } from "./components/test-page/test-page.component";
import { UserPageComponent } from "./components/user-page/user-page.component";
import { TitleLimitPipe } from "./pipes/title-limit.pipe";
import { DataService } from "./services/data.service";
import { MatTableModule } from "@angular/material/table";
import { OrderHistoryComponent } from "./components/order-history/order-history.component";
import { MatSortModule } from "@angular/material/sort";
import { HttpErrorInterceptor } from "../httperrorinterceptor";
import { SpinnerModule } from "./directives/spinner.directive";
import { GiftCardComponent } from './components/gift-card/gift-card.component';

@NgModule({
  declarations: [
    LoginComponent,
    AppComponent,
    HomePageComponent,
    CheckoutPageComponent,
    UserPageComponent,
    TestPageComponent,
    ComicCardComponent,
    ProductsPageComponent,
    NotFoundComponent,
    CheckoutPageCartComponent,
    CheckoutStepperComponent,
    ConfirmationModalComponent,
    ProductDetailsComponent,
    HeaderComponent,
    FooterComponent,
    NextButtonDirective,
    PrevButtonDirective,
    ReviewComponent,
    StarRatingComponent,
    GenresSliderComponent,
    TitleLimitPipe,
    ProductCarouselComponent,
    RippedReceiptComponent,
    OrderConfirmationComponent,
    OrderSuccessComponent,
    CreditCardComponent,
    SignUpComponent,
    SignInComponent,
    MessageModalComponent,
    ReviewModalComponent,
    RelatedProductsComponent,
    OrderHistoryComponent,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
    {
      provide: Window,
      useValue: window,
    },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: "outline" },
    },
    DataService,
  ],
  bootstrap: [AppComponent],
  imports: [
    FontAwesomeModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    HttpClientModule,
    MatCardModule,
    MatGridListModule,
    MatSidenavModule,
    MatToolbarModule,
    MatTooltipModule,
    MatBadgeModule,
    MatStepperModule,
    MatFormFieldModule,
    MatDividerModule,
    MatListModule,
    MatDialogModule,
    CommonModule,
    ToastrModule.forRoot({
      timeOut: 4000,
      preventDuplicates: true,
      positionClass: "toast-top-center",
      progressBar: true,
      progressAnimation: "decreasing",
      closeButton: true,
      maxOpened: 2,
    }),
    FormsModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    SocialLoginModule,
    MatBadgeModule,
    NgOptimizedImage,
    MatAutocompleteModule,
    MatPaginatorModule,
    MatTabsModule,
    CdkMenuModule,
    AmplifyAuthenticatorModule,
    NgxSliderModule,
    MatTableModule,
    MatSortModule,
    SpinnerModule,
  ],
})
export class AppModule {}
