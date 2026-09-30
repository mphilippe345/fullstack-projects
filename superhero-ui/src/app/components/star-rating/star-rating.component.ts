import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-star-rating',
    templateUrl: './star-rating.component.html',
    styleUrls: ['./star-rating.component.css']
})
export class StarRatingComponent implements OnInit {
    @Input() maxRating = 5;
    maxRatingArr: any = [];
    @Input() selectedStar = 0;
    previousSelection = 0;
  @Input() currentHover: number = 0;

  @Output() onRating: EventEmitter<number> = new EventEmitter<number>();

    constructor() {
    }

    handleMouseEnter(index: number) {
        this.selectedStar = index + 1;
    }

    handleMouseLeave() {
        if (this.previousSelection !== 0) {
            this.selectedStar = this.previousSelection;
        } else {
            this.selectedStar = 0;
        }
    }

    // add the following to the mat-icon to implement a rating system for creating reviews
    // (mouseenter)="handleMouseEnter(index)"
    // (mouseleave) = "handleMouseLeave()"

    rating(index:number) {
        this.selectedStar = index + 1;
        this.previousSelection = this.selectedStar;
        this.onRating.emit(this.selectedStar);
    }

    ngOnInit(): void {
        this.maxRatingArr = Array(this.maxRating).fill(0);
    }

  highlightStars(rating: number) {
    this.currentHover = rating;
  }

  removeColor() {
    this.currentHover = 0;
  }

  protected readonly onmouseleave = onmouseleave;
}
