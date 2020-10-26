import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl } from '@angular/forms';

import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JobTaskOrderService } from './job-task-order.service';
import { JobTaskOrderDeleteDialogComponent } from './job-task-order-delete-dialog.component';

@Component({
  selector: 'jhi-job-task-order',
  templateUrl: './job-task-order.component.html',
})
export class JobTaskOrderComponent implements OnInit, OnDestroy {
  jobTaskOrders?: IJobTaskOrder[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  userForm = new FormGroup({
    taskSeqId: new FormControl(''),
    jobTaskStatusFlag: new FormControl(''),
    configVersion: new FormControl(''),
    jobId: new FormControl(''),
    taskId: new FormControl(''),
  });

  constructor(
    protected jobTaskOrderService: JobTaskOrderService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.jobTaskOrderService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IJobTaskOrder[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInJobTaskOrders();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
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
    this.eventSubscriber = this.eventManager.subscribe('jobTaskOrderListModification', () => this.loadPage());
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

  protected onSuccess(data: IJobTaskOrder[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/job-task-order'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.jobTaskOrders = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  onEnter(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;
    if (this.userForm.value) {
      this.jobTaskOrderService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          'taskSeqId.equals': this.userForm.value.taskSeqId,
          'jobTaskStatusFlag.equals': this.userForm.value.jobTaskStatusFlag,
          'configVersion.equals': this.userForm.value.configVersion,
          'jobId.equals': this.userForm.value.jobId,
          'taskId.equals': this.userForm.value.taskId,
        })
        .subscribe(
          (res: HttpResponse<IJobTaskOrder[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    }
  }
}
