import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipelineSharedModule } from 'app/shared/shared.module';
import { GlobalConfigComponent } from './global-config.component';
import { GlobalConfigDetailComponent } from './global-config-detail.component';
import { GlobalConfigUpdateComponent } from './global-config-update.component';
import { GlobalConfigDeleteDialogComponent } from './global-config-delete-dialog.component';
import { globalConfigRoute } from './global-config.route';

@NgModule({
  imports: [PipelineSharedModule, RouterModule.forChild(globalConfigRoute)],
  declarations: [GlobalConfigComponent, GlobalConfigDetailComponent, GlobalConfigUpdateComponent, GlobalConfigDeleteDialogComponent],
  entryComponents: [GlobalConfigDeleteDialogComponent],
})
export class PipelineGlobalConfigModule {}
