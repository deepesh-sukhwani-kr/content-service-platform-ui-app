import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CspVendorComponent} from "./csp-vendor.component";
import {ButtonModule} from "primeng/button";
import {MessagesModule} from "primeng/messages";
import {PanelModule} from "primeng/panel";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DropdownModule} from "primeng/dropdown";
import {TooltipModule} from "primeng/tooltip";
import {MessageModule} from "primeng/message";
import {DataViewModule} from "primeng/dataview";
import {DialogModule} from "primeng/dialog";
import {ProgressBarModule} from "primeng/progressbar";
import {InputTextModule} from "primeng/primeng";
import {RadioButtonModule} from 'primeng/radiobutton'
import {CheckboxModule} from 'primeng/checkbox';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {TableModule} from "primeng/table";

@NgModule({
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    ButtonModule,
    DialogModule,
    MessageModule,
    MessagesModule,
    FormsModule,
    InputTextModule,
    DataViewModule,
    ReactiveFormsModule,
    PanelModule,
    DropdownModule,
    ProgressBarModule,
    TooltipModule,
    CheckboxModule,
    TableModule,
    RadioButtonModule
  ],exports: [
    CspVendorComponent
  ],
  declarations: [CspVendorComponent],
})
export class CspVendorModule { }
