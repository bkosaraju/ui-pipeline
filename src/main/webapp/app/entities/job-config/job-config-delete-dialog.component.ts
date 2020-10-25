import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobConfig } from 'app/shared/model/job-config.model';
import { JobConfigService } from './job-config.service';

@Component({
  templateUrl: './job-config-delete-dialog.component.html',
})
export class JobConfigDeleteDialogComponent {
  jobConfig?: IJobConfig;

  constructor(protected jobConfigService: JobConfigService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jobConfigListModification');
      this.activeModal.close();
    });
  }
}
