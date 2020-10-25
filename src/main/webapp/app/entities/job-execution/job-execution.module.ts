import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipelineSharedModule } from 'app/shared/shared.module';
import { JobExecutionComponent } from './job-execution.component';
import { JobExecutionDetailComponent } from './job-execution-detail.component';
import { JobExecutionUpdateComponent } from './job-execution-update.component';
import { JobExecutionDeleteDialogComponent } from './job-execution-delete-dialog.component';
import { jobExecutionRoute } from './job-execution.route';

@NgModule({
  imports: [PipelineSharedModule, RouterModule.forChild(jobExecutionRoute)],
  declarations: [JobExecutionComponent, JobExecutionDetailComponent, JobExecutionUpdateComponent, JobExecutionDeleteDialogComponent],
  entryComponents: [JobExecutionDeleteDialogComponent],
})
export class PipelineJobExecutionModule {}
