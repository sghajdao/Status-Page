import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { SwitchService } from '../../../services/switch.service';
import { SwitchEntity } from '../../../models/switchEntity';
import { Subscription } from 'rxjs';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-switch',
  standalone: false,
  templateUrl: './switch.component.html',
  styleUrl: './switch.component.css'
})
export class SwitchComponent implements OnInit, OnChanges, OnDestroy {
  constructor(
    private switchService: SwitchService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private fb: FormBuilder
  ) {}

  @Input() newSwitch?: SwitchEntity
  @Input() searchInput?: string
  switchs: SwitchEntity[] = []
  switchsBackUp: SwitchEntity[] = []
  form!: FormGroup

  subscriptions: Subscription[] = []

  ngOnInit(): void {
    const sub = this.switchService.getAll().subscribe({
      next: data => {
        this.switchs = data
        this.switchsBackUp = data
      }
    })
    this.subscriptions.push(sub)

    this.form = this.fb.group({
      ip: [null, Validators.required],
      location: [null, Validators.required],
    })
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['newSwitch'] && this.newSwitch) {
      this.switchs.push(this.newSwitch)
      this.switchsBackUp.push(this.newSwitch)
    }
    else if (changes['searchInput'] && this.searchInput && this.searchInput.length)
      this.switchs = this.switchs.filter(item => item.ip.startsWith(this.searchInput!))
    else if (changes['searchInput'] && !this.searchInput?.length)
      this.switchs = this.switchsBackUp
  }

  dialogVisible: boolean = false;
  toUpdate?: SwitchEntity

  showUpdateDialog(entity: SwitchEntity) {
    this.toUpdate = entity
    this.form = this.fb.group({
      ip: [entity.ip, Validators.required],
      location: [entity.location, Validators.required],
    })
    this.dialogVisible = true;
  }

  deleteConfirm(event: Event, entity: SwitchEntity) {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Do you want to delete this switch?',
            header: 'Delete Switch',
            icon: 'pi pi-info-circle',
            rejectLabel: 'Cancel',
            rejectButtonProps: {
                label: 'Cancel',
                severity: 'secondary',
                outlined: true,
            },
            acceptButtonProps: {
                label: 'Delete',
                severity: 'danger',
            },

            accept: () => {
              this.deleteSwitch(entity)
            },
            reject: () => {
                this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            },
        });
  }

  updateSwitch() {
    if (this.toUpdate && this.form.valid) {
      let updated: SwitchEntity = {
        id: this.toUpdate.id,
        ip: this.form.value.ip,
        location: this.form.value.location
      }
      const sub = this.switchService.updateSwitch(updated).subscribe({
        next: data => {
          this.switchsBackUp = this.switchsBackUp.filter(item => item.id !== data.id)
          this.switchsBackUp.push(data)
          this.switchs = this.switchsBackUp
          this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'Switch Updated' });
          this.dialogVisible = false
        }
      })
      this.subscriptions.push(sub)
    }
  }

  deleteSwitch(entity: SwitchEntity) {
    const sub = this.switchService.deleteSwitch(entity.id!).subscribe({
      next: data => {
        if (data) {
          this.switchsBackUp = this.switchsBackUp.filter(item => item.id !== entity.id)
          this.switchs = this.switchsBackUp
          this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'Switch deleted' });
        }
      }
    })
    this.subscriptions.push(sub)
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe())
  }
}
