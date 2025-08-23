import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SwitchService } from '../../../services/switch.service';
import { SwitchEntity } from '../../../models/switchEntity';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-form',
  standalone: false,
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormComponent implements OnInit, OnDestroy {
  constructor(
    private fb: FormBuilder,
    private switchService: SwitchService,
  ) {}

  @Output() switch = new EventEmitter<SwitchEntity>()
  form!: FormGroup

  subscriptions: Subscription[] = []

  ngOnInit(): void {
    this.form = this.fb.group({
      ip: [null, Validators.required],
      location: [null, Validators.required],
    })
  }

  newSwitch() {
    if (this.form && this.form.valid) {
      const sub = this.switchService.newSwitch(this.form.value).subscribe({
        next: data => {
          this.switch.emit(data)
          this.form = this.fb.group({
            ip: [null, Validators.required],
            location: [null, Validators.required],
          })
        }
      })
      this.subscriptions.push(sub)
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe())
  }
}
