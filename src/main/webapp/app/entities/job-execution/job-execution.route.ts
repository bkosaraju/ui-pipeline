import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJobExecution, JobExecution } from 'app/shared/model/job-execution.model';
import { JobExecutionService } from './job-execution.service';
import { JobExecutionComponent } from './job-execution.component';
import { JobExecutionDetailComponent } from './job-execution-detail.component';
import { JobExecutionUpdateComponent } from './job-execution-update.component';

@Injectable({ providedIn: 'root' })
export class JobExecutionResolve implements Resolve<IJobExecution> {
  constructor(private service: JobExecutionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobExecution> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jobExecution: HttpResponse<JobExecution>) => {
          if (jobExecution.body) {
            return of(jobExecution.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobExecution());
  }
}

export const jobExecutionRoute: Routes = [
  {
    path: '',
    component: JobExecutionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobExecution.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobExecutionDetailComponent,
    resolve: {
      jobExecution: JobExecutionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobExecution.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobExecutionUpdateComponent,
    resolve: {
      jobExecution: JobExecutionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobExecution.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobExecutionUpdateComponent,
    resolve: {
      jobExecution: JobExecutionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobExecution.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
