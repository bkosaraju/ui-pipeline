import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITaskExecutionConfig } from 'app/shared/model/task-execution-config.model';
import { TaskExecutionConfigService } from './task-execution-config.service';

@Component({
  templateUrl: './task-execution-config-delete-dialog.component.html',
})
export class TaskExecutionConfigDeleteDialogComponent {
  taskExecutionConfig?: ITaskExecutionConfig;

  constructor(
    protected taskExecutionConfigService: TaskExecutionConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taskExecutionConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('taskExecutionConfigListModification');
      this.activeModal.close();
    });
  }
}
