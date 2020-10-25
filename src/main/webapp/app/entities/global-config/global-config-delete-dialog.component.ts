import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGlobalConfig } from 'app/shared/model/global-config.model';
import { GlobalConfigService } from './global-config.service';

@Component({
  templateUrl: './global-config-delete-dialog.component.html',
})
export class GlobalConfigDeleteDialogComponent {
  globalConfig?: IGlobalConfig;

  constructor(
    protected globalConfigService: GlobalConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.globalConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('globalConfigListModification');
      this.activeModal.close();
    });
  }
}
