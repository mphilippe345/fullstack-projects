import {
  Directive,
  Input,
  NgModule,
  TemplateRef,
  ViewContainerRef,
} from "@angular/core";
import { ThemePalette } from "@angular/material/core";
import {
  MatProgressSpinner,
  MatProgressSpinnerModule,
} from "@angular/material/progress-spinner";
import { BrowserModule } from "@angular/platform-browser";
import { CommonModule } from "@angular/common";

/**
 * Apply *appSpinner to any container to replace it's content with a mat-spinner.
 * It is customizable by color, diameter, and margins and shows/hides based on boolean value.
 *
 * Examples:___________________________________________
 *---: *appSpinner="loading",_________________________
 *---: *appSpinner="loading; color: 'primary'; margin: '50px auto'; diameter: 45"
 */
@Directive({
  selector: "[appSpinner]",
})
export class SpinnerDirective {
  #color: ThemePalette = "accent";
  #diameter: number = 100;
  #margin: string = "30px auto";
  #isSpinning: boolean | null = null;
  #spinner: MatProgressSpinner | null = null;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef
  ) {}

  /**
   * Sets spinner color: *appSpinner="color: 'primary'"
   *
   * @param color 'primary' | 'accent' | 'warn'
   */
  @Input()
  set appSpinnerColor(color: ThemePalette) {
    this.#color = color;
    if (this.#spinner) {
      this.#spinner.color = color;
    }
  }

  /**
   * Sets spinner diameter: *appSpinner="diameter: 50"
   *
   * @param diameter a number greater than 0
   */
  @Input()
  set appSpinnerDiameter(diameter: number) {
    this.#diameter = diameter;
    if (this.#spinner) {
      this.#spinner.diameter = diameter;
    }
  }

  /**
   * Sets position of spinner in container by margins: *appSpinner="margin: '40px auto'"
   *
   * @param margin a string value, like margin css shorthand
   */
  @Input()
  set appSpinnerMargin(margin: string) {
    this.#margin = margin;
    if (this.#spinner) {
      this.#spinner._elementRef.nativeElement.style.margin = margin;
    }
  }

  /**
   * Sets whether spinner is active or not: *appSpinner="isLoading"
   *
   * @param condition a boolean to determine state of spinner
   */
  @Input() set appSpinner(condition: boolean) {
    if (condition !== this.#isSpinning) {
      this.#spinner = null;
      this.viewContainer.clear();
      this.#isSpinning = condition;
      if (!condition) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else if (condition) {
        this.addSpinner();
      }
    }
  }

  /**
   * Method to create instance of spinner and set its properties
   *
   * @private only done when a condition is applied to the spinner directive
   */
  private addSpinner() {
    const { instance } =
      this.viewContainer.createComponent<MatProgressSpinner>(
        MatProgressSpinner
      );
    instance.diameter = this.#diameter;
    instance.color = this.#color;
    instance.mode = "indeterminate";
    instance._elementRef.nativeElement.style.margin = this.#margin;
    instance._elementRef.nativeElement.classList.add("spin-on-instance");
    this.#spinner = instance;
  }
}

@NgModule({
  imports: [BrowserModule, CommonModule, MatProgressSpinnerModule],
  declarations: [SpinnerDirective],
  exports: [SpinnerDirective, MatProgressSpinnerModule],
})
export class SpinnerModule {}
