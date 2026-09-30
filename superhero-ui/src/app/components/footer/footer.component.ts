import { Component,ElementRef, ViewChildren, AfterViewInit, OnInit, ViewChild } from '@angular/core';
import { MatToolbar } from '@angular/material/toolbar';
@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements AfterViewInit {
  @ViewChildren("toolbar")
  toolbar!: MatToolbar;
  @ViewChildren("main")
  main!: ElementRef;
  @ViewChildren("footer")
  footer!: ElementRef;

  constructor() { }

  ngOnInit(): void {
  }
ngAfterViewInit(): void {
    
}
}
