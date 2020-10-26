import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJobConfig, JobConfig } from 'app/shared/model/job-config.model';
import { JobConfigService } from './job-config.service';
import { JobConfigComponent } from './job-config.component';
import { JobConfigDetailComponent } from './job-config-detail.component';
import { JobConfigUpdateComponent } from './job-config-update.component';

@Injectable({ providedIn: 'root' })
export class JobConfigResolve implements Resolve<IJobConfig> {
  constructor(private service: JobConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jobConfig: HttpResponse<JobConfig>) => {
          if (jobConfig.body) {
            return of(jobConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobConfig());
  }
}

export const jobConfigRoute: Routes = [
  {
    path: '',
    component: JobConfigComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'pipelineApp.jobConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobConfigDetailComponent,
    resolve: {
      jobConfig: JobConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobConfigUpdateComponent,
    resolve: {
      jobConfig: JobConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobConfigUpdateComponent,
    resolve: {
      jobConfig: JobConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
