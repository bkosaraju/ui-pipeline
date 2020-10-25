import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGlobalConfig, GlobalConfig } from 'app/shared/model/global-config.model';
import { GlobalConfigService } from './global-config.service';

@Component({
  selector: 'jhi-global-config-update',
  templateUrl: './global-config-update.component.html',
})
export class GlobalConfigUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    configKey: [],
    configValue: [],
    configType: [],
  });

  constructor(protected globalConfigService: GlobalConfigService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ globalConfig }) => {
      this.updateForm(globalConfig);
    });
  }

  updateForm(globalConfig: IGlobalConfig): void {
    this.editForm.patchValue({
      id: globalConfig.id,
      configKey: globalConfig.configKey,
      configValue: globalConfig.configValue,
      configType: globalConfig.configType,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const globalConfig = this.createFromForm();
    if (globalConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.globalConfigService.update(globalConfig));
    } else {
      this.subscribeToSaveResponse(this.globalConfigService.create(globalConfig));
    }
  }

  private createFromForm(): IGlobalConfig {
    return {
      ...new GlobalConfig(),
      id: this.editForm.get(['id'])!.value,
      configKey: this.editForm.get(['configKey'])!.value,
      configValue: this.editForm.get(['configValue'])!.value,
      configType: this.editForm.get(['configType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGlobalConfig>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
