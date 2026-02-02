import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface IntakeRequest {
  pid: string;
  notes?: string;
  intakeDate?: string;
}

@Injectable({
  providedIn: 'root'
})
export class IntakeService {

  private readonly baseUrl = 'http://localhost:8080/api/intakes';

  constructor(private http: HttpClient) {}

  /** CREATE intake */
  create(request: IntakeRequest): Observable<any> {
    return this.http.post(this.baseUrl, request);
  }

  /** GET intake by patient PID */
  getByPid(pid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/patient/${pid}`);
  }
}
