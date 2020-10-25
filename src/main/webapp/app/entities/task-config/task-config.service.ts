import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITaskConfig } from 'app/shared/model/task-config.model';

type EntityResponseType = HttpResponse<ITaskConfig>;
type EntityArrayResponseType = HttpResponse<ITaskConfig[]>;

@Injectable({ providedIn: 'root' })
export class TaskConfigService {
  public resourceUrl = SERVER_API_URL + 'api/task-configs';

  constructor(protected http: HttpClient) {}

  create(taskConfig: ITaskConfig): Observable<EntityResponseType> {
    return this.http.post<ITaskConfig>(this.resourceUrl, taskConfig, { observe: 'response' });
  }

  update(taskConfig: ITaskConfig): Observable<EntityResponseType> {
    return this.http.put<ITaskConfig>(this.resourceUrl, taskConfig, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITaskConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaskConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
