import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipelineSharedModule } from 'app/shared/shared.module';
import { TaskConfigComponent } from './task-config.component';
import { TaskConfigDetailComponent } from './task-config-detail.component';
import { TaskConfigUpdateComponent } from './task-config-update.component';
import { TaskConfigDeleteDialogComponent } from './task-config-delete-dialog.component';
import { taskConfigRoute } from './task-config.route';

@NgModule({
  imports: [PipelineSharedModule, RouterModule.forChild(taskConfigRoute)],
  declarations: [TaskConfigComponent, TaskConfigDetailComponent, TaskConfigUpdateComponent, TaskConfigDeleteDialogComponent],
  entryComponents: [TaskConfigDeleteDialogComponent],
})
export class PipelineTaskConfigModule {}
