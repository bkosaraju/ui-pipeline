import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJobTaskOrder, JobTaskOrder } from 'app/shared/model/job-task-order.model';
import { JobTaskOrderService } from './job-task-order.service';
import { JobTaskOrderComponent } from './job-task-order.component';
import { JobTaskOrderDetailComponent } from './job-task-order-detail.component';
import { JobTaskOrderUpdateComponent } from './job-task-order-update.component';

@Injectable({ providedIn: 'root' })
export class JobTaskOrderResolve implements Resolve<IJobTaskOrder> {
  constructor(private service: JobTaskOrderService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobTaskOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jobTaskOrder: HttpResponse<JobTaskOrder>) => {
          if (jobTaskOrder.body) {
            return of(jobTaskOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobTaskOrder());
  }
}

export const jobTaskOrderRoute: Routes = [
  {
    path: '',
    component: JobTaskOrderComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobTaskOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobTaskOrderDetailComponent,
    resolve: {
      jobTaskOrder: JobTaskOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobTaskOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobTaskOrderUpdateComponent,
    resolve: {
      jobTaskOrder: JobTaskOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobTaskOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobTaskOrderUpdateComponent,
    resolve: {
      jobTaskOrder: JobTaskOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.jobTaskOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
