import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITaskConfig, TaskConfig } from 'app/shared/model/task-config.model';
import { TaskConfigService } from './task-config.service';
import { TaskConfigComponent } from './task-config.component';
import { TaskConfigDetailComponent } from './task-config-detail.component';
import { TaskConfigUpdateComponent } from './task-config-update.component';

@Injectable({ providedIn: 'root' })
export class TaskConfigResolve implements Resolve<ITaskConfig> {
  constructor(private service: TaskConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaskConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((taskConfig: HttpResponse<TaskConfig>) => {
          if (taskConfig.body) {
            return of(taskConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaskConfig());
  }
}

export const taskConfigRoute: Routes = [
  {
    path: '',
    component: TaskConfigComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaskConfigDetailComponent,
    resolve: {
      taskConfig: TaskConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaskConfigUpdateComponent,
    resolve: {
      taskConfig: TaskConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaskConfigUpdateComponent,
    resolve: {
      taskConfig: TaskConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.taskConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
