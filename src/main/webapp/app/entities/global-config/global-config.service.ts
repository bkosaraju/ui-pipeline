import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGlobalConfig } from 'app/shared/model/global-config.model';

type EntityResponseType = HttpResponse<IGlobalConfig>;
type EntityArrayResponseType = HttpResponse<IGlobalConfig[]>;

@Injectable({ providedIn: 'root' })
export class GlobalConfigService {
  public resourceUrl = SERVER_API_URL + 'api/global-configs';

  constructor(protected http: HttpClient) {}

  create(globalConfig: IGlobalConfig): Observable<EntityResponseType> {
    return this.http.post<IGlobalConfig>(this.resourceUrl, globalConfig, { observe: 'response' });
  }

  update(globalConfig: IGlobalConfig): Observable<EntityResponseType> {
    return this.http.put<IGlobalConfig>(this.resourceUrl, globalConfig, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGlobalConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGlobalConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
