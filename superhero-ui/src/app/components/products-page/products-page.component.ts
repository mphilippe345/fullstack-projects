import { LabelType, Options } from '@angular-slider/ngx-slider';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from "@angular/forms";
import { MatAutocompleteTrigger } from "@angular/material/autocomplete";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatSelect } from "@angular/material/select";
import { Router } from "@angular/router";
import { ToastrService } from 'ngx-toastr';
import { map, startWith } from "rxjs";
import { Product } from 'src/app/interfaces/product';
import { DataService } from 'src/app/services/data.service';
import { ProductService } from 'src/app/services/products.service';

@Component({
  selector: 'app-products-page',
  templateUrl: './products-page.component.html',
  styleUrls: ['./products-page.component.css']
})
export class ProductsPageComponent implements OnInit, OnDestroy {

  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;
  @ViewChild(MatSelect) select?: MatSelect;

  filtered: any[] =[];

  subscription: any;

  debounceTimer: any;

  loading: boolean = false;

  comicSearch: FormControl = new FormControl('');

  loadedProducts: Product[] = [];

  loadedFields: any[] = [];

  publishers: string[] = [];

  genres: string[] = [];

  titlesAndImages?: Product[];

  filteredTitlesAndImages$;

  totalProducts: number = 0;

  currentPage: number = 0;

  currentSize: number = 12;

  currentSort: string = '';

  paginator?: MatPaginator;

  pageSizeOptions: number[] = [];

  filterPublishers: string[] = [];

  filterGenres: string[] = [];

  newrelease?: boolean;

  bestSeller?: boolean;

  releaseDate?: string;

  minPrice?: number;

  maxPrice?: number;

  passedGenres!: string[];
  topProducts: Product[] = [];

  @ViewChild(MatPaginator) set paginatorContent(paginator: MatPaginator) {
    this.paginator = paginator;
  }

  constructor(public service: ProductService, private toast: ToastrService, private router: Router, private data: DataService) {
    this.filteredTitlesAndImages$ = this.comicSearch.valueChanges.pipe(
      startWith(''),
      map(product => (product ? this._filterProduct(product) : this.titlesAndImages?.slice())),
    );
  }

  ngOnInit() {
    this.loading = true;
    this.service.getBestSellers().subscribe({
      next: (response: Product[]) => this.topProducts = response
    })
    this.service.getFilterFields().subscribe({
      next: (resp) => {
        this.publishers = resp.publishers
        this.genres = resp.genres
      }
    })

    this.data.stringData$.subscribe((data) => {
      if(data !== 'all') {
        this.passedGenres = [data];
      }else{
        this.passedGenres = this.genres;
      }
    });

    this.initFetch()

    this.service.getAllTitlesAndImages().subscribe({
      next: (resp) => {
        this.titlesAndImages = resp
      }
    })
    document.addEventListener('scroll', () => {
      this.autocompleteTrigger?.closePanel()
      this.select?.close();
    })

    this.service.getFilterFields().subscribe({
      next: (resp) => {
        this.publishers = resp.publishers
        this.genres = resp.genres
      }
    })

    this.comicSearch.valueChanges.subscribe(newValue => {
      clearTimeout(this.debounceTimer)
      this.debounceTimer = setTimeout(() => {
        if (this.subscription) this.subscription.unsubscribe();
        if (newValue.trim() !== '') {
          this.subscription = this.service.getFiltered({
            page: 0,
            size: this.currentSize,
            sort: this.currentSort,
            search: this.encodeParam(newValue),
            active: true,
            publishers: this.filterPublishers.length > 0 ? this.filterPublishers : undefined,
            genres: this.filterGenres.length > 0 ? this.filterGenres : undefined,
            releaseDate: this.releaseDate,
            bestSeller: this.bestSeller,
            newrelease: this.newrelease,
            minPrice: this.minPrice,
            maxPrice: this.maxPrice
          }).subscribe({
            next: (resp) => {
              this.loadedProducts = resp.content
              this.currentPage = 0
              this.totalProducts = resp.totalElements
              this.calculatePageSizeOptions()
              window.scroll(0, 0)
              this.paginator?.firstPage()
            },
            complete: () => {
              this.loading = false;
            }
          })
        } else {
          this.initFetch()
        }
      }, 300)
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    clearTimeout(this.debounceTimer)
  }

  /**
   * the following code is for the price range slider
   */
  value: number = 0;
  highValue: number = 101;
  options: Options = {
    floor: 0.00,
    ceil: 101.00,
    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          this.minPrice = value;
          return '$' + this.minPrice;
        case LabelType.High:
          this.maxPrice = value;
          return '$' + this.maxPrice;
        default:
          return '$' + value;
      }
    }
  };

  public set releaseDate1(value: string | undefined) {
    this.releaseDate = value;
  }

  /**
   * checks if the checkboxes in the sidenav have been clicked then changes the valuables to the value of the checkbox
   * @param event - click
   * @param fieldName - which section of checkboxes
   */
  isChecked(event: any, fieldName: string) {
    switch (fieldName) {
      case "publishers":
        if (!this.filterPublishers.includes(event)) {
          this.filterPublishers.push(event)
        } else {
          const indexEvent = this.filterPublishers.indexOf(event)
          this.filterPublishers.splice(indexEvent)
        } break
      case "genres":
        if (!this.filterGenres.includes(event)) {
          this.filterGenres.push(event)
        } else {
          const indexEvent = this.filterGenres.indexOf(event)
          this.filterGenres.splice(indexEvent)
        } break
      case "newrelease":
        this.newrelease = event.checked ? true : undefined;
        break
      case "bestSeller":
        this.bestSeller = event.checked ? true : undefined;
        break
    }
  }

  /**
   * when an event occurs that changes the following values, loadedProducts change to match the values
   * @param event
   */
  onSelectSort(event: any) {
    this.currentSort = event.value;
    this.loading = true
    if (this.comicSearch.value.length == 0) {
      this.service.getFiltered({
        page: 0,
        size: this.currentSize,
        sort: this.currentSort,
        active: true,
        publishers: this.filterPublishers.length > 0 ? this.filterPublishers : undefined,
        genres: this.filterGenres.length > 0 ? this.filterGenres : undefined,
        bestSeller: this.bestSeller,
        releaseDate: this.releaseDate,
        newrelease: this.newrelease,
        minPrice: this.minPrice,
        maxPrice: this.maxPrice
      }).subscribe({
        next: (resp) => {
          this.loadedProducts = resp.content
          this.paginator?.firstPage()
          this.currentSize = resp.pageable.pageSize
        },
        error: () => this.toast.error('Server error when loading products. Please try again later.'),
        complete: () => this.loading = false
      })
    } else {
      this.service.getFiltered({
        page: this.currentPage,
        size: this.currentSize,
        sort: this.currentSort,
        search: this.comicSearch.value,
        active: true,
        publishers: this.filterPublishers.length > 0 ? this.filterPublishers : undefined,
        genres: this.filterGenres.length > 0 ? this.filterGenres : undefined,
        releaseDate: this.releaseDate,
        bestSeller: this.bestSeller,
        newrelease: this.newrelease,
        minPrice: this.minPrice,
        maxPrice: this.maxPrice
      }).subscribe({
        next: (resp) => {
          this.loadedProducts = resp.content
          this.currentPage = 0
          this.paginator?.firstPage()
          this.totalProducts = resp.totalElements
          this.calculatePageSizeOptions()
          window.scroll(0, 0)
        },
        complete: () => {
          this.loading = false;
        }
      })
      this.paginator?.firstPage()
    }
  }

  /**
   * when interacting with the paginator, the displayed products are changed and the filter is still applied
   * @param event
   */
  onPaginatorChange(event: PageEvent) {
    if (this.comicSearch.value.length == 0) {
      window.scroll(0, 0)
      this.loading = true
      this.service.getFiltered({
        page: event.pageIndex,
        size: event.pageSize,
        sort: this.currentSort,
        active: true,
        publishers: this.filterPublishers.length > 0 ? this.filterPublishers : undefined,
        genres: this.filterGenres.length > 0 ? this.filterGenres : undefined,
        releaseDate: this.releaseDate,
        bestSeller: this.bestSeller,
        newrelease: this.newrelease,
        minPrice: this.minPrice,
        maxPrice: this.maxPrice
      }).subscribe({
        next: (resp) => {
          this.loadedProducts = resp.content
          this.currentPage = resp.pageable.pageNumber
          this.currentSize = resp.pageable.pageSize
        },
        error: () => this.toast.error('Server error when loading products. Please try again later.'),
        complete: () => this.loading = false
      })
    } else {
      this.currentPage = event.pageIndex
      this.currentSize = event.pageSize
      window.scroll(0, 0)
      this.loading = true
      this.service.getFiltered({
        page: this.currentPage,
        size: this.currentSize,
        sort: this.currentSort,
        search: this.encodeParam(this.comicSearch.value),
        active: true,
        publishers: this.filterPublishers.length > 0 ? this.filterPublishers : undefined,
        genres: this.filterGenres.length > 0 ? this.filterGenres : undefined,
        releaseDate: this.releaseDate,
        bestSeller: this.bestSeller,
        newrelease: this.newrelease,
        minPrice: this.minPrice,
        maxPrice: this.maxPrice
      }).subscribe({
        next: (resp) => {
          this.loadedProducts = resp.content
          this.totalProducts = resp.totalElements
          this.autocompleteTrigger?.closePanel()
          this.calculatePageSizeOptions()
        },
        error: () => this.toast.error('Server error when loading products. Please try again later.'),
        complete: () => this.loading = false
      })
    }
  }

  /**
   * takes value from search bar
   * @param value
   * @returns
   */
  private _filterProduct(value: string) {
    const filterValue = value.toString().toLowerCase();
    return this.titlesAndImages?.filter(product => product.title.toLowerCase().includes(filterValue) || product.author.toLowerCase().includes(filterValue) || product.publisher.toLowerCase().includes(filterValue));
  }

  onSearchTermChanged() {
    this.loading = true;
  }

  initFetch() {
    this.service
      .getFiltered({
        size: 12,
        genres: this.passedGenres,
        sort: this.currentSort,
        page: 0,
        active: true,
      })
      .subscribe({
        next: (resp) => {
          this.totalProducts = resp.totalElements;
          this.loadedProducts = resp.content;
          this.currentPage = resp.pageable.pageNumber;
          this.currentSize = resp.pageable.pageSize;
          this.calculatePageSizeOptions();
        },
        error: () => {
          this.toast.error(
            "Server error when loading products. Please try again later."
          );
          this.loading = false;
        },
        complete: () => setTimeout(() => this.loading = false, 1000),
      });
  }

  encodeParam(title: string): string {
    return title.replace(/#/g, '%23')
  }

  redirectToDetails(id?: number) {
    this.router.navigateByUrl(`products/${id}`).then();
  }

  calculatePageSizeOptions(): void {
    const possiblePageSizes: number[] = [12, 24, 36, this.totalProducts];
    this.pageSizeOptions = possiblePageSizes.filter(option => option <= this.totalProducts);
  }

}

