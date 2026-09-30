import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
} from "@angular/core";
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { MatStepper } from "@angular/material/stepper";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from "@angular/router";
import * as _ from "lodash";
import { ToastrService } from "ngx-toastr";
import { debounceTime } from "rxjs";
import { LineItem } from "src/app/interfaces/line-item";
import { Purchase } from "src/app/interfaces/purchase";
import { CartService } from "src/app/services/cart.service";
import { PromoService } from "src/app/services/promo.service";
import { STATE_CODES } from "../../constants/constants";
import { formatCreditCard } from "../../helpers/creditCardFormatter";
import { formatGiftCard } from "src/app/helpers/giftCardFormatter";
import { formatPhoneNumber } from "../../helpers/phoneNumberFormatter";
import { Product } from "../../interfaces/product";
import { GiftCardService } from "src/app/services/giftCard.service";

@Component({
  selector: "app-checkout-stepper",
  templateUrl: "./checkout-stepper.component.html",
  styleUrls: ["./checkout-stepper.component.css"],
})
export class CheckoutStepperComponent implements OnInit, AfterViewInit {

  isCreditCardRequired : boolean = true;

  personalInfoForm = new FormGroup({
    firstName: new FormControl("", Validators.required),
    lastName: new FormControl("", Validators.required),
    email: new FormControl("", [
      Validators.required,
      Validators.pattern("^[-a-zA-Z0-9]+@[a-zA-Z]+.[a-zA-Z]+$"),
    ]),
    phoneNumber: new FormControl("", [
      Validators.required,
      Validators.pattern(/^\(\d{3}\)\s\d{3}-\d{4}$/),
    ]),
  });
  billingForm = new FormGroup({
    billingAddress: new FormControl("", Validators.required),
    billingAddress2: new FormControl(""),
    billingCity: new FormControl("", Validators.required),
    billingState: new FormControl("", Validators.required),
    billingZip: new FormControl("", [
      Validators.required,
      Validators.pattern("^[0-9]{5}(-[0-9]{4})?$"),
    ]),
  });
  shippingForm = new FormGroup({
    shippingAddress: new FormControl("", Validators.required),
    shippingAddress2: new FormControl(""),
    shippingCity: new FormControl("", Validators.required),
    shippingState: new FormControl("", Validators.required),
    shippingZip: new FormControl("", [
      Validators.required,
      Validators.pattern("^[0-9]{5}(-[0-9]{4})?$"),
    ]),
  });
  paymentForm = new FormGroup({
    giftCardCode: new FormControl("", [
      this.giftCardCodeValidator(),
    ]),
    giftCardCharge: new FormControl(0, Validators.required),
    nameOnCard: new FormControl("", Validators.required),
    creditCardNumber: new FormControl("", [
      ...(this.isCreditCardRequired ? [Validators.required] : []),
      this.creditCardNumberValidator(),
    ]),
    expDate: new FormControl("", [
      ...(this.isCreditCardRequired ? [Validators.required] : []),
      Validators.pattern("^(0[1-9]|1[0-2])/[0-9]{2}$"),
      this.validateCardExpiration(),
    ]),
    cvv: new FormControl("", [
      ...(this.isCreditCardRequired ? [Validators.required] : []),
      Validators.pattern("^[0-9]{3,4}$"),
    ]),
  });

  sameAsBilling: boolean = false;
  addGiftCard: boolean = false;
  giftCardIsValid : boolean = true;

  public cardType = "";
  public maxDigits = 16;
  public giftCardCodeLength = 14;
  public giftCardBalance: number = 0;
  public giftCardAmmount: any;
  public chargeControl: any;
  public charge: number = 0;

  stateCodes: string[] = STATE_CODES;

  steps = ["personalInfo", "billing", "shipping", "payment"];
  currentStep: number = 0;

  @ViewChild("checkbox") checkbox!: ElementRef;

  constructor(
    private _formBuilder: FormBuilder,
    public cartService: CartService,
    private toast: ToastrService,
    private router: Router,
    public promoCodeService: PromoService,
    public giftCardService: GiftCardService,
    private elementRef: ElementRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.currentStep = 0;
    this.sameAsBilling = false;
    if (this.cartService.getItemCount() > 0) {
      for (let step of this.steps) {
        const formGroup = this.getFormGroup(step);
        const storedForm = this.tryParseStorageItem(step);

        if (storedForm != null) {
          if (
            step == "shipping" &&
            _.isEqual(
              Object.values(this.billingForm.value),
              Object.values(storedForm)
            )
          ) {
            this.sameAsBilling = true;
            this.shippingForm.disable();
          } else if (step == "payment") {
            this.setCardType(storedForm.creditCardNumber);
          }
          formGroup.patchValue(storedForm);
        } else break;
      }
    }
  }

  ngAfterViewInit(): void {
    if (this.cartService.getItemCount() > 0) {
      // stores data for initial step form only
      const formGroup = this.getFormGroup(this.steps[0]);
      this.storeFormData(formGroup, this.steps[0]);
    }

    this.cdr.detectChanges();
  }

  tryParseStorageItem(key: string) {
    try {
      return JSON.parse(sessionStorage.getItem(key)!);
    } catch (error) {
      console.error(error as Error);
      return null;
    }
  }

  getFormGroup(stepName: string) {
    let formGroup: FormGroup;
    switch (stepName) {
      case "personalInfo":
        formGroup = this.personalInfoForm;
        break;
      case "billing":
        formGroup = this.billingForm;
        break;
      case "shipping":
        formGroup = this.shippingForm;
        break;
      default:
        formGroup = this.paymentForm;
        break;
    }

    return formGroup;
  }

  storeFormData(formGroup: FormGroup<any>, formName: string) {
    formGroup.valueChanges.pipe(debounceTime(500)).subscribe((form) => {
      sessionStorage.setItem(
        formName,
        JSON.stringify(form as typeof formGroup)
      );
    });
  }

  giftCardSelected() {
    this.addGiftCard = true;
  }

  creditCardSelected() {
    this.addGiftCard = false;
  }

  checkGiftCard() {
    this.giftCardService.getGiftCard(this.beforeMaskedGiftCard).subscribe((giftCard) => {
      this.giftCardBalance = giftCard.balance ?? 0;
      this.giftCardAmmount = this.paymentForm.get('giftCardCharge')?.value;
    
      if (this.giftCardBalance!== null && this.giftCardBalance > 0) {
       if (this.giftCardAmmount < this.cartService.getTotal()) {
        this.paymentForm.patchValue({
          giftCardCharge : this.cartService.getTotal(),
        });
       } else {
        this.paymentForm.patchValue({
          giftCardCharge : this.giftCardBalance});
       }
      }
      
      this.chargeControl = this.paymentForm.get('giftCardCharge');
      this.charge = this.chargeControl ? this.chargeControl.value ?? 0 : 0;
      this.giftCardService.setGiftCardCharge(this.charge);
    });
  }

  applyGiftCard() {
    this.giftCardService.updateModel(this.charge);
  }

  giftCardCodeValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;

      // Check for masked format: '*' characters followed by 4 characters
      const isMasked = /^\*+[A-HJ-NP-Z2-9-]{4}$/.test(value);
      if (isMasked) {
        // If it's masked, we only check that the last 4 characters are acceptable characters
        const last4Characters = value.slice(-4);
        if (!/^[A-HJ-NP-Z2-9-]{4}$/.test(last4Characters)) {
          return { incorrectTailDigits: { value } };
        }
      } 
      // else {
      //   // If it's not masked, we validate as before:
      //   const pureValue = value.replace(/[^A-HJ-NP-Z2-9-]/g, ""); // Remove non-digit/letter characters
      //   if (pureValue.length !== this.giftCardCodeLength) {
      //     return { incorrectLength: { value } };
      //   }
      // }

      return null;
    };
  }

  creditCardNumberValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;

      // Check for masked format: '*' characters followed by 4 digits
      const isMasked = /^\*+\d{4}$/.test(value);
      if (isMasked) {
        // If it's masked, we only check that the last 4 digits are numbers
        // You might want to adjust this based on your masking rules
        const last4Digits = value.slice(-4);
        if (!/^\d{4}$/.test(last4Digits)) {
          return { incorrectTailDigits: { value } };
        }
      } else {
        // If it's not masked, we validate as before:
        const pureValue = value.replace(/\D/g, ""); // Remove non-digit characters
        if (pureValue.length !== this.maxDigits) {
          return { incorrectLength: { value } };
        } else if (![3, 4, 5, 6].includes(Number(pureValue[0]))) {
          return { invalidStartDigit: { value } };
        }
      }

      return null;
    };
  }

  validateCardExpiration(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const currentValue = control.value;
      if (currentValue) {
        const [month, year] = currentValue.split("/");
        const currentDate = new Date();
        const currentYear = currentDate.getFullYear();
        const currentMonth = currentDate.getMonth() + 1; // months are 0-indexed.
        const inputYear = 2000 + parseInt(year, 10); // Assuming 'year' is in 'YY' format.
        const inputMonth = parseInt(month, 10);

        if (
          inputYear < currentYear ||
          (inputYear === currentYear && inputMonth < currentMonth)
        ) {
          // Card expiry date is in the past.
          return { expiredCard: true };
        }
      }
      return null; // Card expiry date is in the future, so no validation error.
    };
  }

  toggleSameAsBilling() {
    this.sameAsBilling = !this.sameAsBilling;

    if (this.sameAsBilling) {
      this.shippingForm.patchValue({
        shippingAddress: this.billingForm.value.billingAddress,
        shippingAddress2: this.billingForm.value.billingAddress2,
        shippingCity: this.billingForm.value.billingCity,
        shippingState: this.billingForm.value.billingState,
        shippingZip: this.billingForm.value.billingZip,
      });

      // Disable form
      this.shippingForm.disable();
    } else {
      // Enable form
      this.shippingForm.enable();
      this.shippingForm.reset();
    }
  }

  formatZipCode(control: FormControl): void {
    let numbers = control.value.replace(/\D/g, "");

    if (numbers.length > 5) {
      numbers = numbers.slice(0, 5) + "-" + numbers.slice(5);
    }

    control.setValue(numbers.slice(0, 10)); // Limit to 10 characters, e.g., "12345-6789".
  }

  formatPhoneNumber(control: FormControl): void {
    const numbers = control.value;

    const formattedNumber = formatPhoneNumber(numbers);

    control.setValue(formattedNumber);
  }

  formatGiftCardCode(control: FormControl): void {
    const code: string = control.value;

    const formattedCode = formatGiftCard(code);

    control.setValue(formattedCode);
    this.setMaxDigits(code);
  }

  formatCreditCardNumber(control: FormControl): void {
    const numbers: string = control.value;

    const formattedNumber = formatCreditCard(numbers);

    control.setValue(formattedNumber);
    this.setCardType(numbers);
    this.setMaxDigits(numbers);
  }

  setCardType(numbers: string): void {
    const startsWithNumber = Number(numbers[0]);

    switch (startsWithNumber) {
      case 4:
        this.cardType = "Visa";
        break;
      case 5:
        this.cardType = "Mastercard";
        break;
      case 6:
        this.cardType = "Discover";
        break;
      case 3:
        this.cardType = "American Express";
        break;
      default:
        this.cardType = "";
        break;
    }
  }

  setMaxDigits(numbers: string): void {
    if (numbers.startsWith("3")) {
      this.maxDigits = 15;
    } else {
      this.maxDigits = 16;
    }
  }

  formatCardExpiration(control: FormControl): void {
    let numbers = control.value.replace(/\D/g, "");
    const char: { [key: number]: string } = { 2: "/" };
    let formattedExpiry = "";

    for (let i = 0; i < numbers.length && i < 4; i++) {
      formattedExpiry += (char[i] || "") + numbers[i];
    }

    control.setValue(formattedExpiry);
  }

  formatCVV(control: FormControl): void {
    let numbers = control.value.replace(/\D/g, "");
    control.setValue(numbers.slice(0, 4)); // Limit to 4 characters at most.
  }

  nextStep(form: FormGroup, stepper: MatStepper) {
    if (this.sameAsBilling && form === this.shippingForm) {
      this.currentStep += 1;
      const formGroup = this.getFormGroup(this.steps[this.currentStep]);
      this.storeFormData(formGroup, this.steps[this.currentStep]);
      stepper.next();
    } else {
      this.markFieldsAsTouched(form);
      if (form.valid) {
        this.currentStep += 1;
        const formGroup = this.getFormGroup(this.steps[this.currentStep]);
        this.storeFormData(formGroup, this.steps[this.currentStep]);
        stepper.next();
      }
    }
  }

  markFieldsAsTouched(form: FormGroup): void {
    Object.values(form.controls).forEach((control) => {
      control.markAsTouched();
    });
  }

  mapFormDataToPurchase() {
    const cartProducts: Product[] = this.cartService.getCartForCheckoutPage(); // get cart products

    const lineItems: LineItem[] = cartProducts
      .filter((product) => product.id !== undefined)
      .map((product) => ({
        product: {
          id: product.id!,
        },
        quantity: this.cartService.getProductQuantityByIdAndPrice(
          product.id!,
          product.price
        ),
      }));

    const purchase: Purchase = {
      personalInfo: {
        firstName: this.personalInfoForm.get("firstName")!.value || "",
        lastName: this.personalInfoForm.get("lastName")!.value || "",
        email: this.personalInfoForm.get("email")!.value || "",
        phoneNumber: this.formatDigitsOnly(
          this.personalInfoForm.get("phoneNumber")!.value || ""
        ),
      },
      billingAddress: {
        streetAddress1: this.billingForm.get("billingAddress")!.value || "",
        streetAddress2: this.billingForm.get("billingAddress2")!.value || "",
        city: this.billingForm.get("billingCity")!.value || "",
        state: this.billingForm.get("billingState")!.value || "",
        zipCode: this.billingForm.get("billingZip")!.value || "",
      },
      shippingAddress: {
        streetAddress1: this.shippingForm.get("shippingAddress")!.value || "",
        streetAddress2: this.shippingForm.get("shippingAddress2")!.value || "",
        city: this.shippingForm.get("shippingCity")!.value || "",
        state: this.shippingForm.get("shippingState")!.value || "",
        zipCode: this.shippingForm.get("shippingZip")!.value || "",
      },
      giftCard: {
        code: this.beforeMaskedGiftCard,
        charge: this.charge,
      },
      creditCard: {
        cardName: this.paymentForm.get("nameOnCard")!.value || "",
        cardType: this.cardType,
        cardNumber: this.formatDigitsOnly(this.beforeMaskedCreditCard || ""),
        expirationDate: this.paymentForm.get("expDate")!.value || "",
        securityCode: this.paymentForm.get("cvv")!.value || "",
      },
      products: lineItems,
      orderTotal:
        this.cartService.getTotalMinusGiftCard() ?? this.cartService.getTotalMinusGiftCard(),
    };

    return purchase;
  }

  formatDigitsOnly(input: string): string {
    return input.replace(/\D/g, "");
  }

  formatGiftCardCharactersOnly(input: string): string {
    return input.replace(/[a-zA-Z0-9]/g, "");
  }

  redirectToOrderConfirmation() {
    
    if (this.cartService.getTotalMinusGiftCard() == 0){
      this.isCreditCardRequired = false;
    }
    
    const validatedPurchase = this.validatePurchase()!;
    if (validatedPurchase != undefined) {
      localStorage.setItem("purchase", JSON.stringify(validatedPurchase));
      this.router.navigate(["confirmation"]).then();
    } else {
      this.toast.error("Unable to process purchase.");
    }
  }

  validatePurchase() {
    if (this.paymentForm.valid || !this.isCreditCardRequired) {
      const purchase = this.mapFormDataToPurchase();
      return purchase;
    } else {
      // Trigger validation for all form controls to show errors
      this.markFieldsAsTouched(this.paymentForm);
      return;
    }
  }

  // developing masked Creditcard
  beforeMaskedGiftCard: any;
  giftCard: any;
  giftCardEyeIcon: string = "visibility_off"; // Initial state for the icon (closed eye)

  maskGiftCard() {
    this.beforeMaskedGiftCard = this.giftCard;
    const len = this.giftCard.length;
    this.giftCard = "*".repeat(len - 4) + this.giftCard.slice(-4);
    this.giftCardEyeIcon = "visibility_off";
    //  Encrypt and save to session storage
    // const encryptedCard = this.encrypt(this.giftCard);
    // sessionStorage.setItem("creditCardNumber", encryptedCard);
  }

  unmaskGiftCard() {
    this.giftCard = this.beforeMaskedGiftCard;
    this.giftCardEyeIcon = "visibility";
  }

  // developing masked Creditcard
  beforeMaskedCreditCard: any;
  creditCard: any;
  eyeIcon: string = "visibility_off"; // Initial state for the icon (closed eye)

  maskCreditCard() {
    this.beforeMaskedCreditCard = this.creditCard;
    const len = this.creditCard.length;
    this.creditCard = "*".repeat(len - 4) + this.creditCard.slice(-4);
    this.eyeIcon = "visibility_off";
    // Encrypt and save to session storage
    const encryptedCard = this.encrypt(this.creditCard);
    sessionStorage.setItem("creditCardNumber", encryptedCard);
  }

  unmaskCreditCard() {
    this.creditCard = this.beforeMaskedCreditCard;
    this.eyeIcon = "visibility";
  }
  // A simple encryption function (please use a real encryption library for production)
  encrypt(data: string) {
    return btoa(data);
  }

  // A simple decryption function (please use a real encryption library for production)
  decrypt(data: string) {
    return atob(data);
  }
}
