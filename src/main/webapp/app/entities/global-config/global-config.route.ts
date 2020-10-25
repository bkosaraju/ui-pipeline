import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGlobalConfig, GlobalConfig } from 'app/shared/model/global-config.model';
import { GlobalConfigService } from './global-config.service';
import { GlobalConfigComponent } from './global-config.component';
import { GlobalConfigDetailComponent } from './global-config-detail.component';
import { GlobalConfigUpdateComponent } from './global-config-update.component';

@Injectable({ providedIn: 'root' })
export class GlobalConfigResolve implements Resolve<IGlobalConfig> {
  constructor(private service: GlobalConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGlobalConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((globalConfig: HttpResponse<GlobalConfig>) => {
          if (globalConfig.body) {
            return of(globalConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GlobalConfig());
  }
}

export const globalConfigRoute: Routes = [
  {
    path: '',
    component: GlobalConfigComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.globalConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GlobalConfigDetailComponent,
    resolve: {
      globalConfig: GlobalConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.globalConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GlobalConfigUpdateComponent,
    resolve: {
      globalConfig: GlobalConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.globalConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GlobalConfigUpdateComponent,
    resolve: {
      globalConfig: GlobalConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'pipelineApp.globalConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
