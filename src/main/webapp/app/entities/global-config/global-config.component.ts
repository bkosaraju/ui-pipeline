import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGlobalConfig } from 'app/shared/model/global-config.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GlobalConfigService } from './global-config.service';
import { GlobalConfigDeleteDialogComponent } from './global-config-delete-dialog.component';

@Component({
  selector: 'jhi-global-config',
  templateUrl: './global-config.component.html',
})
export class GlobalConfigComponent implements OnInit, OnDestroy {
  globalConfigs: IGlobalConfig[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected globalConfigService: GlobalConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.globalConfigs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.globalConfigService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IGlobalConfig[]>) => this.paginateGlobalConfigs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.globalConfigs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGlobalConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGlobalConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGlobalConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('globalConfigListModification', () => this.reset());
  }

  delete(globalConfig: IGlobalConfig): void {
    const modalRef = this.modalService.open(GlobalConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.globalConfig = globalConfig;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateGlobalConfigs(data: IGlobalConfig[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.globalConfigs.push(data[i]);
      }
    }
  }
}
