import { Component, OnInit } from "@angular/core";
import {
  trigger,
  transition,
  query,
  style,
  animate,
  group,
} from "@angular/animations";
import { ProductService } from "src/app/services/products.service";
import { DataService } from "src/app/services/data.service";
import { Router } from "@angular/router";
import { switchMap } from "rxjs";

const left = [
  query(":enter, :leave", style({ position: "fixed", width: "100%" }), {
    optional: true,
  }),
  group([
    query(
      ":enter",
      [
        style({ transform: "translateX(-100%)" }),
        animate(".3s ease-out", style({ transform: "translateX(0%)" })),
      ],
      {
        optional: true,
      }
    ),
    query(
      ":leave",
      [
        style({ transform: "translateX(0%)" }),
        animate(".3s ease-out", style({ transform: "translateX(100%)" })),
      ],
      {
        optional: true,
      }
    ),
  ]),
];

const right = [
  query(":enter, :leave", style({ position: "fixed", width: "100%" }), {
    optional: true,
  }),
  group([
    query(
      ":enter",
      [
        style({ transform: "translateX(100%)" }),
        animate(".2s ease-out", style({ transform: "translateX(0%)" })),
      ],
      {
        optional: true,
      }
    ),
    query(
      ":leave",
      [
        style({ transform: "translateX(0%)" }),
        animate(".3s ease-out", style({ transform: "translateX(-100%)" })),
      ],
      {
        optional: true,
      }
    ),
  ]),
];

@Component({
  selector: "app-genres-slider",
  templateUrl: "./genres-slider.component.html",
  styleUrls: ["./genres-slider.component.css"],
  animations: [
    trigger("animSlider", [
      transition(":increment", right),
      transition(":decrement", left),
    ]),
  ],
})
export class GenresSliderComponent implements OnInit {
  counter: number = 0;
  loadedProducts: any;
  genre!: string;
  constructor(
    private service: ProductService,
    private data: DataService,
    private router: Router
  ) {}

  ngOnInit() {
    this.service.getProducts().subscribe((response) => {
      this.loadedProducts = response;
    });
    this.data.currentGenre.subscribe((genre) => (this.genre = genre));
  }

  onNext() {
    if (this.counter != this.loadedProducts.length) {
      this.counter++;
    }
  }

  onPrevious() {
    if (this.counter > 0) {
      this.counter--;
    }
  }

  /**
   * Passes this provided string to the Dataservice 
   * @param genreName : string
   */
  newGenre(genreName: string) {
    this.data.changeGenre(genreName);
    this.data.stringData$
      .pipe(
        switchMap((data) => {
          if (data) {
            return this.router.navigate(["/products"]);
          }
          return [];
        })
      )
      .subscribe();
  }
}
