import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

interface DialogData {
  title: string,
  description: string,
  option1: string,
  option2: string,
  func: Function
}

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']
})
export class ConfirmationModalComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData, public dialogRef:MatDialogRef<ConfirmationModalComponent>) { }

  ngOnInit(): void {
  }

  onOption1Click(): void {
    this.data.func();
    this.dialogRef.close();
  }

  onOption2Click(): void {
    this.dialogRef.close();
  }

}
