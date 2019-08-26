import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CsvUploadComponent} from "./csv-upload.component";
import {FileUploadModule} from "primeng/fileupload";
import {PapaParseModule} from "ngx-papaparse";
import {TableModule} from 'primeng/table';
import {BrowserModule} from "@angular/platform-browser";
import {MessagesModule} from "primeng/messages";
import {ProgressBarModule} from "primeng/progressbar";
import {TooltipModule} from "primeng/tooltip";

@NgModule({
  imports: [
    CommonModule,
    FileUploadModule,
    PapaParseModule,
    TableModule,
    BrowserModule,
    MessagesModule,
    ProgressBarModule,
    TooltipModule
  ],
  exports: [
    CsvUploadComponent
  ],
  declarations: [CsvUploadComponent],
})
export class CsvUploadModule {
}
