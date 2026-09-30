import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {Product} from "../../interfaces/product";

interface reviewData {
  product: Product
}

@Component({
  selector: 'app-review-modal',
  templateUrl: './review-modal.component.html',
  styleUrls: ['./review-modal.component.css']
})
export class ReviewModalComponent implements OnInit {

  formData: FormGroup = new FormGroup({
    title: new FormControl(''),
    comment: new FormControl(''),
    rating: new FormControl(0),
    date: new FormControl(this.formatCurrentDate(new Date)),
    product: new FormControl('')
  });

  constructor(@Inject(MAT_DIALOG_DATA) public data: reviewData, private dialogRef: MatDialogRef<ReviewModalComponent>) {
  }

  ngOnInit(): void {
    this.formData.get('product')?.setValue(this.data.product)
  }

  /**
   * Handles the logic for submitting a review and closing the review modal.
   */
  public submitReview() {
    this.dialogRef.close()
  }

  /**
   * Takes the rating that a user selects in the stars and puts it into the review FormGroup.
   * @param rating number of stars selected by user.
   */
  public onSelectRating(rating: number) {
    this.formData.get('rating')?.setValue(rating)
  }

  /**
   * returns the number of characters the user enters in the title input.
   */
  public titleCharacterCount(): number {
    let titleInForm = this.formData.get('title');
    return titleInForm?.value.length;
  }

  /**
   * returns the number of characters the user enters in the comment input.
   */
  public commentCharacterCount():number {
    let commentInForm = this.formData.get('comment');
    return commentInForm?.value.length;
  }

  /**
   * formats a Date object to the YYYY-mm-DD format that we need to be able to persist to our databose.
   * @param date Any Date object.
   */
  public formatCurrentDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

}
