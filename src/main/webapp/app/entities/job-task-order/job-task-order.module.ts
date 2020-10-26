import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipelineSharedModule } from 'app/shared/shared.module';
import { JobTaskOrderComponent } from './job-task-order.component';
import { JobTaskOrderDetailComponent } from './job-task-order-detail.component';
import { JobTaskOrderUpdateComponent } from './job-task-order-update.component';
import { JobTaskOrderDeleteDialogComponent } from './job-task-order-delete-dialog.component';
import { jobTaskOrderRoute } from './job-task-order.route';

@NgModule({
  imports: [PipelineSharedModule, RouterModule.forChild(jobTaskOrderRoute)],
  declarations: [JobTaskOrderComponent, JobTaskOrderDetailComponent, JobTaskOrderUpdateComponent, JobTaskOrderDeleteDialogComponent],
  entryComponents: [JobTaskOrderDeleteDialogComponent],
})
export class PipelineJobTaskOrderModule {}
