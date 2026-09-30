import { Directive,ElementRef, HostListener  } from '@angular/core';

@Directive({
  selector: '[appPrevButton]'
})
export class PrevButtonDirective {

  constructor(private el: ElementRef) { }
  @HostListener('click')
  prevFunc() {
    const elm = this.el.nativeElement.parentElement.parentElement.children[0];
    const item = elm.getElementsByClassName("item");
    elm.prepend(item[item.length -1])
  }

}
