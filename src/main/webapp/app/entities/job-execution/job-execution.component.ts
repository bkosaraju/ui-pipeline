import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobExecution } from 'app/shared/model/job-execution.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JobExecutionService } from './job-execution.service';
import { JobExecutionDeleteDialogComponent } from './job-execution-delete-dialog.component';

@Component({
  selector: 'jhi-job-execution',
  templateUrl: './job-execution.component.html',
})
export class JobExecutionComponent implements OnInit, OnDestroy {
  jobExecutions: IJobExecution[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected jobExecutionService: JobExecutionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.jobExecutions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.jobExecutionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IJobExecution[]>) => this.paginateJobExecutions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.jobExecutions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJobExecutions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJobExecution): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJobExecutions(): void {
    this.eventSubscriber = this.eventManager.subscribe('jobExecutionListModification', () => this.reset());
  }

  delete(jobExecution: IJobExecution): void {
    const modalRef = this.modalService.open(JobExecutionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobExecution = jobExecution;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateJobExecutions(data: IJobExecution[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.jobExecutions.push(data[i]);
      }
    }
  }
}
