import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobExecution } from 'app/shared/model/job-execution.model';
import { JobExecutionService } from './job-execution.service';

@Component({
  templateUrl: './job-execution-delete-dialog.component.html',
})
export class JobExecutionDeleteDialogComponent {
  jobExecution?: IJobExecution;

  constructor(
    protected jobExecutionService: JobExecutionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobExecutionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jobExecutionListModification');
      this.activeModal.close();
    });
  }
}
