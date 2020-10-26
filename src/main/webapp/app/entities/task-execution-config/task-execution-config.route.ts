import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITaskExecutionConfig, TaskExecutionConfig } from 'app/shared/model/task-execution-config.model';
import { TaskExecutionConfigService } from './task-execution-config.service';
import { TaskExecutionConfigComponent } from './task-execution-config.component';
import { TaskExecutionConfigDetailComponent } from './task-execution-config-detail.component';
import { TaskExecutionConfigUpdateComponent } from './task-execution-config-update.component';

@Injectable({ providedIn: 'root' })
export class TaskExecutionConfigResolve implements Resolve<ITaskExecutionConfig> {
  constructor(private service: TaskExecutionConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaskExecutionConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((taskExecutionConfig: HttpResponse<TaskExecutionConfig>) => {
          if (taskExecutionConfig.body) {
            return of(taskExecutionConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaskExecutionConfig());
  }
}

export const taskExecutionConfigRoute: Routes = [
  {
    path: '',
    component: TaskExecutionConfigComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'pipelineApp.taskExecutionConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaskExecutionConfigDetailComponent,
    resolve: {
      taskExecutionConfig: TaskExecutionConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskExecutionConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaskExecutionConfigUpdateComponent,
    resolve: {
      taskExecutionConfig: TaskExecutionConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskExecutionConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaskExecutionConfigUpdateComponent,
    resolve: {
      taskExecutionConfig: TaskExecutionConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskExecutionConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
