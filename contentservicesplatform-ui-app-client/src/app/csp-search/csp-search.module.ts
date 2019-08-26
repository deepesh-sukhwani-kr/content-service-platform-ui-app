import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccordionModule} from 'primeng/accordion';
import {CspSearchComponent} from "./csp-search.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ButtonModule} from 'primeng/button';
import {MessagesModule} from 'primeng/messages';
import {MessageModule} from 'primeng/message';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/primeng";
import {DataViewModule} from 'primeng/dataview';
import {CspSearchService} from "./csp-search.service";
import {PanelModule} from 'primeng/panel';
import {DropdownModule} from 'primeng/dropdown';
import {DialogModule} from 'primeng/dialog';
import {ProgressBarModule} from "primeng/progressbar";
import {TooltipModule} from 'primeng/tooltip';

@NgModule({
  imports: [
    CommonModule,
    AccordionModule,
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
  ],
  exports: [
    CspSearchComponent
  ],
  declarations: [CspSearchComponent],
  providers: [CspSearchService]
})
export class CspSearchModule {
}
