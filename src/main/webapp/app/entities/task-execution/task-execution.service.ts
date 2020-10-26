import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITaskExecution } from 'app/shared/model/task-execution.model';

type EntityResponseType = HttpResponse<ITaskExecution>;
type EntityArrayResponseType = HttpResponse<ITaskExecution[]>;

@Injectable({ providedIn: 'root' })
export class TaskExecutionService {
  public resourceUrl = SERVER_API_URL + 'api/task-executions';

  constructor(protected http: HttpClient) {}

  create(taskExecution: ITaskExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taskExecution);
    return this.http
      .post<ITaskExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(taskExecution: ITaskExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taskExecution);
    return this.http
      .put<ITaskExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaskExecution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaskExecution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(taskExecution: ITaskExecution): ITaskExecution {
    const copy: ITaskExecution = Object.assign({}, taskExecution, {
      jobOrderTimestamp:
        taskExecution.jobOrderTimestamp && taskExecution.jobOrderTimestamp.isValid() ? taskExecution.jobOrderTimestamp.toJSON() : undefined,
      taskExecutionStartTimestamp:
        taskExecution.taskExecutionStartTimestamp && taskExecution.taskExecutionStartTimestamp.isValid()
          ? taskExecution.taskExecutionStartTimestamp.toJSON()
          : undefined,
      taskExecutionEndTimestamp:
        taskExecution.taskExecutionEndTimestamp && taskExecution.taskExecutionEndTimestamp.isValid()
          ? taskExecution.taskExecutionEndTimestamp.toJSON()
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.jobOrderTimestamp = res.body.jobOrderTimestamp ? moment(res.body.jobOrderTimestamp) : undefined;
      res.body.taskExecutionStartTimestamp = res.body.taskExecutionStartTimestamp
        ? moment(res.body.taskExecutionStartTimestamp)
        : undefined;
      res.body.taskExecutionEndTimestamp = res.body.taskExecutionEndTimestamp ? moment(res.body.taskExecutionEndTimestamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((taskExecution: ITaskExecution) => {
        taskExecution.jobOrderTimestamp = taskExecution.jobOrderTimestamp ? moment(taskExecution.jobOrderTimestamp) : undefined;
        taskExecution.taskExecutionStartTimestamp = taskExecution.taskExecutionStartTimestamp
          ? moment(taskExecution.taskExecutionStartTimestamp)
          : undefined;
        taskExecution.taskExecutionEndTimestamp = taskExecution.taskExecutionEndTimestamp
          ? moment(taskExecution.taskExecutionEndTimestamp)
          : undefined;
      });
    }
    return res;
  }
}
