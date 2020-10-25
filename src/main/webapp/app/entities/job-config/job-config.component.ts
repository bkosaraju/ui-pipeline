import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobConfig } from 'app/shared/model/job-config.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JobConfigService } from './job-config.service';
import { JobConfigDeleteDialogComponent } from './job-config-delete-dialog.component';

@Component({
  selector: 'jhi-job-config',
  templateUrl: './job-config.component.html',
})
export class JobConfigComponent implements OnInit, OnDestroy {
  jobConfigs: IJobConfig[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected jobConfigService: JobConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.jobConfigs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.jobConfigService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IJobConfig[]>) => this.paginateJobConfigs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.jobConfigs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJobConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJobConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJobConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('jobConfigListModification', () => this.reset());
  }

  delete(jobConfig: IJobConfig): void {
    const modalRef = this.modalService.open(JobConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobConfig = jobConfig;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateJobConfigs(data: IJobConfig[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.jobConfigs.push(data[i]);
      }
    }
  }
}
