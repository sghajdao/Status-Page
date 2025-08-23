import { Component, OnInit } from '@angular/core';
import { StatusService } from '../../../services/status.service';
import { SwitchStatus } from '../../../models/switchStatus';

@Component({
  selector: 'app-stats',
  standalone: false,
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.css'
})
export class StatsComponent implements OnInit {
  constructor(
    private statusService: StatusService,
  ) {}

  status?: SwitchStatus[]
  switchs: number = 0
  online: number = 0
  offline: number = 0
  overall: number = 0

  ngOnInit(): void {
    this.statusService.status$.subscribe({
      next: data => {
        if (data) {
          this.status = data
          this.switchs = this.status!.length
          this.online = this.status!.filter(item => !item.status).length
          this.offline = this.status!.filter(item => item.status).length
          this.overall = this.online * 100 / this.switchs
        }
      }
    })
  }
}
