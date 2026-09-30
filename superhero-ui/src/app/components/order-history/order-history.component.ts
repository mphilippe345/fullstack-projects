import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from "@angular/core";
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from "@angular/animations";
import { MatTableDataSource } from "@angular/material/table";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { Order } from "../../interfaces/order";
import { PurchaseService } from "../../services/purchase.service";
import { Subscription } from "rxjs";
import { Page } from "../../interfaces/page";
import { ToastrService } from "ngx-toastr";
import { keys } from "lodash";
import { Column } from "../../interfaces/column";
import { MatSort, Sort } from "@angular/material/sort";

@Component({
  selector: "app-order-history",
  templateUrl: "./order-history.component.html",
  styleUrls: ["./order-history.component.css"],
  animations: [
    trigger("detailExpand", [
      state("collapsed", style({ height: "0px", minHeight: "0" })),
      state("expanded", style({ height: "*" })),
      transition(
        "expanded <=> collapsed",
        animate("225ms cubic-bezier(0.4, 0.0, 0.2, 1)")
      ),
    ]),
  ],
})
export class OrderHistoryComponent implements OnInit, OnDestroy {
  @Input() email!: string;
  @Output() pageEvent = new EventEmitter<Page>();

  @ViewChild(MatPaginator) set paginatorContent(paginator: MatPaginator) {
    this.paginator = paginator;
  }

  subscription!: Subscription;
  dataSource: MatTableDataSource<Order> = new MatTableDataSource<Order>();
  columnsToDisplay!: Column[];
  displayedColumns!: string[];
  paginator!: MatPaginator;
  currentPage: number = 0;
  currentSize: number = 10;
  totalElements!: number;

  constructor(
    private purchaseService: PurchaseService,
    private toast: ToastrService
  ) {}

  ngOnInit() {
    this.subscription = this.purchaseService
      .getOrdersByEmail(this.email, this.currentPage, this.currentSize)
      .subscribe({
        next: (ordersPage) => {
          const orders = ordersPage.content;
          const excludedFields = ["email", "firstName"];
          this.dataSource.data = orders;
          this.columnsToDisplay = this.getColumns(orders[0], excludedFields);
          this.displayedColumns = this.getElementFields(
            orders[0],
            excludedFields
          );
          this.pageEvent.emit(ordersPage);
          this.totalElements = ordersPage.totalElements;
          this.currentSize = ordersPage.pageable["pageSize"];
          this.currentPage = ordersPage.pageable["pageNumber"];
        },
        error: (err) => {
          this.toast.error(
            "Server error while retrieving past orders. Please try again later!"
          );
        },
      });
  }

  ngOnDestroy() {
    if (this.subscription) this.subscription.unsubscribe();
  }

  onPaginatorChange(event: PageEvent) {
    this.currentSize = event.pageSize;
    this.currentPage = event.pageIndex;
    window.scroll(0, 0);
    this.ngOnInit();
  }

  private getElementFields(elements: unknown, excluding: string[]) {
    return keys(elements).filter((value) => !excluding.includes(value));
  }

  private getColumns(elements: unknown, excluding: string[]): Column[] {
    const columnAccessors = this.getElementFields(elements, excluding);
    const formattedColumns = columnAccessors.map((column) =>
      this.normalizeField(column)
    );

    return columnAccessors.map(
      (value, index): Column => ({
        display: formattedColumns[index],
        accessor: value,
      })
    );
  }

  private normalizeField(field: string) {
    let normalizedField = field[0].toUpperCase();
    for (let i = 1; i < field.length; i++) {
      const currLetter = field.charAt(i);
      if (currLetter === currLetter.toUpperCase()) {
        normalizedField += " ";
      }
      normalizedField += currLetter;
    }

    return normalizedField;
  }
}
