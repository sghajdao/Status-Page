import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { FormComponent } from './form/form.component';
import { FloatLabelModule } from 'primeng/floatlabel';
import { SwitchComponent } from './switch/switch.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';


@NgModule({
  declarations: [
    DashboardComponent,
    FormComponent,
    SwitchComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    IconFieldModule,
    InputIconModule,
    FloatLabelModule,
    ReactiveFormsModule,
    FormsModule,
    InputTextModule,
    ConfirmDialogModule,
    DialogModule,
    ButtonModule,
    ToastModule,
  ],
  providers: [ConfirmationService, MessageService]
})
export class DashboardModule { }
