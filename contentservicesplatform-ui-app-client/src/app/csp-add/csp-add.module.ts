import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DropdownModule} from "primeng/dropdown";
import {AccordionModule} from "primeng/accordion";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {InputTextModule, MessagesModule} from "primeng/primeng";
import {MessageModule} from "primeng/message";
import {DialogModule} from "primeng/dialog";
import {DataViewModule} from "primeng/dataview";
import {ButtonModule} from "primeng/button";
import {PanelModule} from "primeng/panel";
import {CspAddComponent} from "./csp-add.component";
import {FileUploadModule} from 'primeng/fileupload';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {ToastModule} from 'primeng/toast';
import {CalendarModule} from 'primeng/calendar';
import {CspAddService} from "./csp-add.service";
import {ProgressBarModule} from 'primeng/progressbar';
import {TooltipModule} from 'primeng/tooltip';
import {UtilService} from "../util/util.service";

@NgModule({
  imports: [
    CommonModule,
    AccordionModule,
    BrowserAnimationsModule,
    InputTextareaModule,
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
    FileUploadModule,
    ToastModule,
    CalendarModule,
    ProgressBarModule,
    TooltipModule
  ],
  exports: [
    CspAddComponent
  ],
  providers: [CspAddService, UtilService],
  declarations: [CspAddComponent]
})
export class CspAddModule { }
