import {
  Component,
  OnInit,
  Input,
  ElementRef,
  AfterViewInit,
  ViewChild,
} from '@angular/core';
import { CreditCard } from '../../interfaces/credit-card';
import { formatCreditCard } from '../../helpers/creditCardFormatter';

@Component({
  selector: 'app-credit-card',
  templateUrl: './credit-card.component.html',
  styleUrls: ['./credit-card.component.css'],
})
export class CreditCardComponent implements OnInit, AfterViewInit {
  @Input() creditCard!: CreditCard;
  icon!: string;
  cardNumber!: string[];
  @ViewChild('front') frontCard!: ElementRef;
  @ViewChild('back') backCard!: ElementRef;

  // make this more readable later
  cardOptions = [
    ['assets/visa.svg', 'linear-gradient(to top right, #ad7d92, #718dad)'],
    [
      'assets/mastercard.svg',
      'linear-gradient(to top right, #be5416, #af0000)',
    ],
    ['assets/discover.svg', 'linear-gradient(to top right, #22c1c3, #fdbb2d)'],
    ['assets/amex.svg', 'linear-gradient(to top right, #52b6fe, #6154fe)'],
  ];

  constructor() {}

  setValuesByType(
    cardType: string,
    options: string[][],
    isAfter: boolean = false
  ) {
    const [visa, mastercard, discover, amex] = options;
    switch (cardType) {
      case 'Visa':
        const [vIcon, vGradient] = visa;
        if (isAfter) {
          this.frontCard.nativeElement.style.background = vGradient;
          this.backCard.nativeElement.style.background = vGradient;
        } else {
          this.icon = vIcon;
        }
        break;
      case 'Mastercard':
        const [mcIcon, mcGradient] = mastercard;
        if (isAfter) {
          this.frontCard.nativeElement.style.background = mcGradient;
          this.backCard.nativeElement.style.background = mcGradient;
        } else {
          this.icon = mcIcon;
        }
        break;
      case 'Discover':
        const [dIcon, dGradient] = discover;
        if (isAfter) {
          this.frontCard.nativeElement.style.background = dGradient;
          this.backCard.nativeElement.style.background = dGradient;
        } else {
          this.icon = dIcon;
        }
        break;
      case 'American Express':
        const [amIcon, amGradient] = amex;
        if (isAfter) {
          this.frontCard.nativeElement.style.background = amGradient;
          this.backCard.nativeElement.style.background = amGradient;
        } else {
          this.icon = amIcon;
        }
        break;
      default:
        if (isAfter) {
          this.frontCard.nativeElement.style.background = '#252525';
          this.backCard.nativeElement.style.background = '#252525';
        } else {
          this.icon = 'assets/card.svg';
        }
        break;
    }
  }

  ngOnInit(): void {
    this.cardNumber = formatCreditCard(this.creditCard.cardNumber, true).split(
      ' '
    );
    this.setValuesByType(this.creditCard.cardType, this.cardOptions);
  }

  ngAfterViewInit(): void {
    this.setValuesByType(this.creditCard.cardType, this.cardOptions, true);
  }
}
