import { Component } from '@angular/core';
import { SwitchEntity } from '../../models/switchEntity';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  constructor() {}

  newSwitch?: SwitchEntity
  searchInput?: string

  getSwitch(event: SwitchEntity) {
    this.newSwitch = event
  }
}
