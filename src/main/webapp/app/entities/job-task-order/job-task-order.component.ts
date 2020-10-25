import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JobTaskOrderService } from './job-task-order.service';
import { JobTaskOrderDeleteDialogComponent } from './job-task-order-delete-dialog.component';

@Component({
  selector: 'jhi-job-task-order',
  templateUrl: './job-task-order.component.html',
})
export class JobTaskOrderComponent implements OnInit, OnDestroy {
  jobTaskOrders: IJobTaskOrder[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected jobTaskOrderService: JobTaskOrderService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.jobTaskOrders = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.jobTaskOrderService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IJobTaskOrder[]>) => this.paginateJobTaskOrders(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.jobTaskOrders = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJobTaskOrders();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJobTaskOrder): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJobTaskOrders(): void {
    this.eventSubscriber = this.eventManager.subscribe('jobTaskOrderListModification', () => this.reset());
  }

  delete(jobTaskOrder: IJobTaskOrder): void {
    const modalRef = this.modalService.open(JobTaskOrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobTaskOrder = jobTaskOrder;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateJobTaskOrders(data: IJobTaskOrder[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.jobTaskOrders.push(data[i]);
      }
    }
  }
}
