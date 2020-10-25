import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGlobalConfig } from 'app/shared/model/global-config.model';

@Component({
  selector: 'jhi-global-config-detail',
  templateUrl: './global-config-detail.component.html',
})
export class GlobalConfigDetailComponent implements OnInit {
  globalConfig: IGlobalConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ globalConfig }) => (this.globalConfig = globalConfig));
  }

  previousState(): void {
    window.history.back();
  }
}
