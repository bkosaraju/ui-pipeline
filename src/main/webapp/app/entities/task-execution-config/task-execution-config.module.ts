import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipelineSharedModule } from 'app/shared/shared.module';
import { TaskExecutionConfigComponent } from './task-execution-config.component';
import { TaskExecutionConfigDetailComponent } from './task-execution-config-detail.component';
import { TaskExecutionConfigUpdateComponent } from './task-execution-config-update.component';
import { TaskExecutionConfigDeleteDialogComponent } from './task-execution-config-delete-dialog.component';
import { taskExecutionConfigRoute } from './task-execution-config.route';

@NgModule({
  imports: [PipelineSharedModule, RouterModule.forChild(taskExecutionConfigRoute)],
  declarations: [
    TaskExecutionConfigComponent,
    TaskExecutionConfigDetailComponent,
    TaskExecutionConfigUpdateComponent,
    TaskExecutionConfigDeleteDialogComponent,
  ],
  entryComponents: [TaskExecutionConfigDeleteDialogComponent],
})
export class PipelineTaskExecutionConfigModule {}
