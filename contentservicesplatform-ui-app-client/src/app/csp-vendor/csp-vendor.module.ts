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
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

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
    TooltipModule
  ],exports: [
    CspVendorComponent
  ],
  declarations: [CspVendorComponent],
})
export class CspVendorModule { }
