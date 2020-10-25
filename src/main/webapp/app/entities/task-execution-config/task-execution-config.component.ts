import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TaskExecutionConfigService } from './task-execution-config.service';
import { TaskExecutionConfigDeleteDialogComponent } from './task-execution-config-delete-dialog.component';

@Component({
  selector: 'jhi-task-execution-config',
  templateUrl: './task-execution-config.component.html',
})
export class TaskExecutionConfigComponent implements OnInit, OnDestroy {
  taskExecutionConfigs: ITaskExecutionConfig[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected taskExecutionConfigService: TaskExecutionConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.taskExecutionConfigs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.taskExecutionConfigService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ITaskExecutionConfig[]>) => this.paginateTaskExecutionConfigs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.taskExecutionConfigs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTaskExecutionConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITaskExecutionConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTaskExecutionConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('taskExecutionConfigListModification', () => this.reset());
  }

  delete(taskExecutionConfig: ITaskExecutionConfig): void {
    const modalRef = this.modalService.open(TaskExecutionConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.taskExecutionConfig = taskExecutionConfig;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTaskExecutionConfigs(data: ITaskExecutionConfig[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.taskExecutionConfigs.push(data[i]);
      }
    }
  }
}
