import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { StatusService } from '../../../services/status.service';
import { SwitchStatus } from '../../../models/switchStatus';

@Component({
  selector: 'app-switch',
  standalone: false,
  templateUrl: './switch.component.html',
  styleUrl: './switch.component.css'
})
export class SwitchComponent implements OnInit, OnChanges {
  constructor(
    private statusService: StatusService,
  ) {}

  @Input() searchInput?: string
  status?: SwitchStatus[]
  statusBackUp?: SwitchStatus[]

  ngOnInit(): void {
    this.statusService.status$.subscribe({
      next: data => {
        if (this.searchInput && this.searchInput.length)
          this.status = data?.filter(item => item.switchEntity.ip.startsWith(this.searchInput!))
        else
          this.status = data
        this.statusBackUp = data
      }
    })
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['searchInput'] && this.searchInput && this.searchInput.length)
      this.status = this.status!.filter(item => item.switchEntity.ip.startsWith(this.searchInput!))
    else if (!this.searchInput?.length)
      this.status = this.statusBackUp
  }
}
