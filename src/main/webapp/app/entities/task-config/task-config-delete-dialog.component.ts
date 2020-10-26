import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITaskConfig } from 'app/shared/model/task-config.model';
import { TaskConfigService } from './task-config.service';

@Component({
  templateUrl: './task-config-delete-dialog.component.html',
})
export class TaskConfigDeleteDialogComponent {
  taskConfig?: ITaskConfig;

  constructor(
    protected taskConfigService: TaskConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taskConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('taskConfigListModification');
      this.activeModal.close();
    });
  }
}
