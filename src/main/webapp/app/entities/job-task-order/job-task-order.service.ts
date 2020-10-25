import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';

type EntityResponseType = HttpResponse<IJobTaskOrder>;
type EntityArrayResponseType = HttpResponse<IJobTaskOrder[]>;

@Injectable({ providedIn: 'root' })
export class JobTaskOrderService {
  public resourceUrl = SERVER_API_URL + 'api/job-task-orders';

  constructor(protected http: HttpClient) {}

  create(jobTaskOrder: IJobTaskOrder): Observable<EntityResponseType> {
    return this.http.post<IJobTaskOrder>(this.resourceUrl, jobTaskOrder, { observe: 'response' });
  }

  update(jobTaskOrder: IJobTaskOrder): Observable<EntityResponseType> {
    return this.http.put<IJobTaskOrder>(this.resourceUrl, jobTaskOrder, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobTaskOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobTaskOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
