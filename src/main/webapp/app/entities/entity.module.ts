import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.PipelineJobModule),
      },
      {
        path: 'task',
        loadChildren: () => import('./task/task.module').then(m => m.PipelineTaskModule),
      },
      {
        path: 'job-task-order',
        loadChildren: () => import('./job-task-order/job-task-order.module').then(m => m.PipelineJobTaskOrderModule),
      },
      {
        path: 'global-config',
        loadChildren: () => import('./global-config/global-config.module').then(m => m.PipelineGlobalConfigModule),
      },
      {
        path: 'job-config',
        loadChildren: () => import('./job-config/job-config.module').then(m => m.PipelineJobConfigModule),
      },
      {
        path: 'task-config',
        loadChildren: () => import('./task-config/task-config.module').then(m => m.PipelineTaskConfigModule),
      },
      {
        path: 'job-execution',
        loadChildren: () => import('./job-execution/job-execution.module').then(m => m.PipelineJobExecutionModule),
      },
      {
        path: 'task-execution',
        loadChildren: () => import('./task-execution/task-execution.module').then(m => m.PipelineTaskExecutionModule),
      },
      {
        path: 'task-execution-config',
        loadChildren: () => import('./task-execution-config/task-execution-config.module').then(m => m.PipelineTaskExecutionConfigModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class PipelineEntityModule {}
