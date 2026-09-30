import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutPageCartComponent } from './checkout-page-cart.component';

describe('CheckoutPageCartComponent', () => {
  let component: CheckoutPageCartComponent;
  let fixture: ComponentFixture<CheckoutPageCartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckoutPageCartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckoutPageCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
