import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipelineSharedModule } from 'app/shared/shared.module';
import { JobConfigComponent } from './job-config.component';
import { JobConfigDetailComponent } from './job-config-detail.component';
import { JobConfigUpdateComponent } from './job-config-update.component';
import { JobConfigDeleteDialogComponent } from './job-config-delete-dialog.component';
import { jobConfigRoute } from './job-config.route';

@NgModule({
  imports: [PipelineSharedModule, RouterModule.forChild(jobConfigRoute)],
  declarations: [JobConfigComponent, JobConfigDetailComponent, JobConfigUpdateComponent, JobConfigDeleteDialogComponent],
  entryComponents: [JobConfigDeleteDialogComponent],
})
export class PipelineJobConfigModule {}
