import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskExecutionConfig } from 'app/shared/model/task-execution-config.model';
import { TaskExecutionConfigService } from './task-execution-config.service';
import { TaskExecutionConfigDeleteDialogComponent } from './task-execution-config-delete-dialog.component';

@Component({
  selector: 'jhi-task-execution-config',
  templateUrl: './task-execution-config.component.html',
})
export class TaskExecutionConfigComponent implements OnInit, OnDestroy {
  taskExecutionConfigs?: ITaskExecutionConfig[];
  eventSubscriber?: Subscription;

  constructor(
    protected taskExecutionConfigService: TaskExecutionConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.taskExecutionConfigService
      .query()
      .subscribe((res: HttpResponse<ITaskExecutionConfig[]>) => (this.taskExecutionConfigs = res.body || []));
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
    this.eventSubscriber = this.eventManager.subscribe('taskExecutionConfigListModification', () => this.loadAll());
  }

  delete(taskExecutionConfig: ITaskExecutionConfig): void {
    const modalRef = this.modalService.open(TaskExecutionConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.taskExecutionConfig = taskExecutionConfig;
  }
}
