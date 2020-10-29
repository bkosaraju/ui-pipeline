import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITaskConfig, TaskConfig } from 'app/shared/model/task-config.model';
import { TaskConfigService } from './task-config.service';

@Component({
  selector: 'jhi-task-config-update',
  templateUrl: './task-config-update.component.html',
})
export class TaskConfigUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    configKey: [],
    configValue: [],
    configType: [],
    configVersion: [],
  });

  constructor(protected taskConfigService: TaskConfigService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskConfig }) => {
      this.updateForm(taskConfig);
    });
  }

  updateForm(taskConfig: ITaskConfig): void {
    this.editForm.patchValue({
      id: taskConfig.id,
      configKey: taskConfig.configKey,
      configValue: taskConfig.configValue,
      configType: taskConfig.configType,
      configVersion: taskConfig.configVersion,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taskConfig = this.createFromForm();
    if (taskConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.taskConfigService.update(taskConfig));
    } else {
      this.subscribeToSaveResponse(this.taskConfigService.create(taskConfig));
    }
  }

  private createFromForm(): ITaskConfig {
    return {
      ...new TaskConfig(),
      id: this.editForm.get(['id'])!.value,
      configKey: this.editForm.get(['configKey'])!.value,
      configValue: this.editForm.get(['configValue'])!.value,
      configType: this.editForm.get(['configType'])!.value,
      configVersion: this.editForm.get(['configVersion'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskConfig>>): void {
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
