import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-patient-form-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title>{{ data.isEdit ? 'Edit Patient' : 'Add New Patient' }}</h2>
    <mat-dialog-content>
      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; padding-top: 10px;">
        <mat-form-field appearance="outline">
          <mat-label>First Name</mat-label>
          <input matInput [(ngModel)]="patient.firstName">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Last Name</mat-label>
          <input matInput [(ngModel)]="patient.lastName">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Date of Birth</mat-label>
          <input matInput type="date" [(ngModel)]="patient.dob">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Gender</mat-label>
          <mat-select [(ngModel)]="patient.gender">
            <mat-option value="MALE">MALE</mat-option>
            <mat-option value="FEMALE">FEMALE</mat-option>
          </mat-select>
        </mat-form-field>
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="dialogRef.close()">Cancel</button>
      <button mat-flat-button color="primary" (click)="dialogRef.close(patient)">Save</button>
    </mat-dialog-actions>
  `
})
export class PatientFormDialogComponent {
  patient: any;
  constructor(public dialogRef: MatDialogRef<PatientFormDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.patient = { ...data.patient };
  }
}
