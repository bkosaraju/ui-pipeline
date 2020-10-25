import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

type EntityResponseType = HttpResponse<ITaskExecutionConfig>;
type EntityArrayResponseType = HttpResponse<ITaskExecutionConfig[]>;

@Injectable({ providedIn: 'root' })
export class TaskExecutionConfigService {
  public resourceUrl = SERVER_API_URL + 'api/task-execution-configs';

  constructor(protected http: HttpClient) {}

  create(taskExecutionConfig: ITaskExecutionConfig): Observable<EntityResponseType> {
    return this.http.post<ITaskExecutionConfig>(this.resourceUrl, taskExecutionConfig, { observe: 'response' });
  }

  update(taskExecutionConfig: ITaskExecutionConfig): Observable<EntityResponseType> {
    return this.http.put<ITaskExecutionConfig>(this.resourceUrl, taskExecutionConfig, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITaskExecutionConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaskExecutionConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
