import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IJobConfig } from 'app/shared/model/job-config.model';

type EntityResponseType = HttpResponse<IJobConfig>;
type EntityArrayResponseType = HttpResponse<IJobConfig[]>;

@Injectable({ providedIn: 'root' })
export class JobConfigService {
  public resourceUrl = SERVER_API_URL + 'api/job-configs';

  constructor(protected http: HttpClient) {}

  create(jobConfig: IJobConfig): Observable<EntityResponseType> {
    return this.http.post<IJobConfig>(this.resourceUrl, jobConfig, { observe: 'response' });
  }

  update(jobConfig: IJobConfig): Observable<EntityResponseType> {
    return this.http.put<IJobConfig>(this.resourceUrl, jobConfig, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
