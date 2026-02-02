import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  private baseUrl = 'http://localhost:8080/api/patients';

  constructor(private http: HttpClient) {}

  // ===============================
  // GET LIST (ACTIVE ONLY)
  // ===============================
  getPatients(page: number, size: number) {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('status', 'ACTIVE');

    return this.http.get<any>(this.baseUrl, { params });
  }

  // ===============================
  // SEARCH (ACTIVE ONLY)
  // ===============================
  searchPatients(keyword: string, page: number, size: number) {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page)
      .set('size', size)
      .set('status', 'ACTIVE');

    return this.http.get<any>(`${this.baseUrl}/search`, { params });
  }

  // ===============================
  // CREATE (ADD PATIENT)
  // ===============================
  createPatient(payload: any) {
    return this.http.post<any>(this.baseUrl, payload);
  }

  // ===============================
  // UPDATE PATIENT
  // ===============================
  updatePatient(id: string, payload: any) {
    return this.http.put<any>(`${this.baseUrl}/${id}`, payload);
  }

  // ===============================
  // SOFT DELETE (DEACTIVATE)
  // ===============================
  deactivate(id: string) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
