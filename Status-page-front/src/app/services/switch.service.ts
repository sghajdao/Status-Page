import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SwitchEntity } from '../models/switchEntity';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SwitchService {
  constructor(
    private http: HttpClient
  ) { }

  newSwitch(request: SwitchEntity) {
    return this.http.post<SwitchEntity>(environment.apiUrl + 'api/save', request)
  }

  getAll() {
    return this.http.get<SwitchEntity[]>(environment.apiUrl + 'api/all')
  }

  updateSwitch(entity: SwitchEntity) {
    return this.http.put<SwitchEntity>(environment.apiUrl + 'api/update', entity)
  }

  deleteSwitch(id: number) {
    return this.http.delete<boolean>(environment.apiUrl + 'api/delete/' + id)
  }
}
