import { PersonalInfo } from './personal-info';
import { Address } from './address';
import { CreditCard } from './credit-card';
import { LineItem } from './line-item';
import { GiftCard } from './gift-card';

export interface Purchase {
  id?: number;
  products: LineItem[];
  personalInfo: PersonalInfo;
  shippingAddress: Address;
  billingAddress: Address;
  giftCard: GiftCard;
  creditCard: CreditCard;
  orderTotal: number;
  orderNumber?: string;
  giftCardCharge?: number;
  giftCardCode?: string;
}
