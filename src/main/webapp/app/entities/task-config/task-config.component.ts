import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskConfig } from 'app/shared/model/task-config.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TaskConfigService } from './task-config.service';
import { TaskConfigDeleteDialogComponent } from './task-config-delete-dialog.component';

@Component({
  selector: 'jhi-task-config',
  templateUrl: './task-config.component.html',
})
export class TaskConfigComponent implements OnInit, OnDestroy {
  taskConfigs: ITaskConfig[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected taskConfigService: TaskConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.taskConfigs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.taskConfigService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ITaskConfig[]>) => this.paginateTaskConfigs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.taskConfigs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTaskConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITaskConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTaskConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('taskConfigListModification', () => this.reset());
  }

  delete(taskConfig: ITaskConfig): void {
    const modalRef = this.modalService.open(TaskConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.taskConfig = taskConfig;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTaskConfigs(data: ITaskConfig[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.taskConfigs.push(data[i]);
      }
    }
  }
}
