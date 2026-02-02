import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientService } from '../../services/patient.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { PatientFormDialogComponent } from './patient-form-dialog.component';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-patient-list',
  templateUrl: './patient-list.component.html',
  styleUrls: ['./patient-list.component.css'],
  imports: [CommonModule, MatDialogModule, MatPaginatorModule, MatCardModule, MatIconModule, MatMenuModule, MatButtonModule]
})
export class PatientListComponent implements OnInit {
  data: any[] = [];
  page = 0; size = 20; total = 0; keyword = ''
  loading = false; // 👈 ADD THIS;

  constructor(private service: PatientService, private dialog: MatDialog,private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  load(): void {
    this.loading = true;
    const req$ = this.keyword
      ? this.service.searchPatients(this.keyword, this.page, this.size)
      : this.service.getPatients(this.page, this.size);

    req$.subscribe({
      next: res => {
        this.data = res?.content ?? [];
        this.total = res?.totalElements ?? 0;
      },
      error: err =>{
        console.error('Load failed', err)
        this.loading = false;
      }
    });
  }

  onSearch(value: string): void {
    this.keyword = value.trim();
    this.page = 0;
    this.load();
  }

  add(): void {
    const ref = this.dialog.open(PatientFormDialogComponent, {
      width: '500px',
      data: { isEdit: false }
    });

    ref.afterClosed().subscribe(result => {
      if (!result) return;
      this.service.createPatient(result).subscribe(() => {
        this.page = 0;
        this.loadPatients();
      });
    });
  }

  edit(p: any): void {
    const ref = this.dialog.open(PatientFormDialogComponent, {
      width: '500px',
      data: { isEdit: true, patient: p }
    });

    ref.afterClosed().subscribe(result => {
      if (!result) return;
      this.service.updatePatient(p.id, result).subscribe(() => this.loadPatients());
    });
  }

  deactivate(p: any): void {
    if (!confirm(`Deactivate ${p.firstName}?`)) return;
    this.service.deactivate(p.id).subscribe(() => {
      this.page = 0;
      this.loadPatients();
    });
  }

  onPageChange(e: PageEvent): void {
    this.page = e.pageIndex;
    this.size = e.pageSize;
    this.loadPatients();
  }

  loadPatients() {
    this.service.getPatients(this.page, this.size).subscribe(res => {
      this.data = [...res.content];
      this.total = res.totalElements;
      this.cdr.detectChanges(); // 🔥 FORCE REFRESH
    });
  }


}
