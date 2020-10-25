import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';
import { JobTaskOrderService } from './job-task-order.service';

@Component({
  templateUrl: './job-task-order-delete-dialog.component.html',
})
export class JobTaskOrderDeleteDialogComponent {
  jobTaskOrder?: IJobTaskOrder;

  constructor(
    protected jobTaskOrderService: JobTaskOrderService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobTaskOrderService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jobTaskOrderListModification');
      this.activeModal.close();
    });
  }
}
