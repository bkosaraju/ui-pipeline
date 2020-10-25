import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IJobExecution } from 'app/shared/model/job-execution.model';

type EntityResponseType = HttpResponse<IJobExecution>;
type EntityArrayResponseType = HttpResponse<IJobExecution[]>;

@Injectable({ providedIn: 'root' })
export class JobExecutionService {
  public resourceUrl = SERVER_API_URL + 'api/job-executions';

  constructor(protected http: HttpClient) {}

  create(jobExecution: IJobExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobExecution);
    return this.http
      .post<IJobExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(jobExecution: IJobExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobExecution);
    return this.http
      .put<IJobExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJobExecution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJobExecution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(jobExecution: IJobExecution): IJobExecution {
    const copy: IJobExecution = Object.assign({}, jobExecution, {
      jobExecutionTimestamp:
        jobExecution.jobExecutionTimestamp && jobExecution.jobExecutionTimestamp.isValid()
          ? jobExecution.jobExecutionTimestamp.toJSON()
          : undefined,
      jobOrderTimestamp:
        jobExecution.jobOrderTimestamp && jobExecution.jobOrderTimestamp.isValid() ? jobExecution.jobOrderTimestamp.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.jobExecutionTimestamp = res.body.jobExecutionTimestamp ? moment(res.body.jobExecutionTimestamp) : undefined;
      res.body.jobOrderTimestamp = res.body.jobOrderTimestamp ? moment(res.body.jobOrderTimestamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((jobExecution: IJobExecution) => {
        jobExecution.jobExecutionTimestamp = jobExecution.jobExecutionTimestamp ? moment(jobExecution.jobExecutionTimestamp) : undefined;
        jobExecution.jobOrderTimestamp = jobExecution.jobOrderTimestamp ? moment(jobExecution.jobOrderTimestamp) : undefined;
      });
    }
    return res;
  }
}
