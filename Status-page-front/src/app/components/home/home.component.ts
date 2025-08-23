import { Component, OnInit } from '@angular/core';
import { StatusService } from '../../services/status.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  constructor(
    private statusSerice: StatusService,
  ) {}

  searchInput?: string

  ngOnInit(): void {
    this.statusSerice.initializeWebSocketConnection()
  }
}
