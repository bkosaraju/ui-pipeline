import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITaskExecutionConfig, TaskExecutionConfig } from 'app/shared/model/task-execution-config.model';
import { TaskExecutionConfigService } from './task-execution-config.service';
import { ITaskExecution } from 'app/shared/model/task-execution.model';
import { TaskExecutionService } from 'app/entities/task-execution/task-execution.service';

@Component({
  selector: 'jhi-task-execution-config-update',
  templateUrl: './task-execution-config-update.component.html',
})
export class TaskExecutionConfigUpdateComponent implements OnInit {
  isSaving = false;
  taskexecutions: ITaskExecution[] = [];

  editForm = this.fb.group({
    id: [],
    configKey: [],
    configValue: [],
    configVersion: [],
    taskExecution: [],
  });

  constructor(
    protected taskExecutionConfigService: TaskExecutionConfigService,
    protected taskExecutionService: TaskExecutionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskExecutionConfig }) => {
      this.updateForm(taskExecutionConfig);

      this.taskExecutionService.query().subscribe((res: HttpResponse<ITaskExecution[]>) => (this.taskexecutions = res.body || []));
    });
  }

  updateForm(taskExecutionConfig: ITaskExecutionConfig): void {
    this.editForm.patchValue({
      id: taskExecutionConfig.id,
      configKey: taskExecutionConfig.configKey,
      configValue: taskExecutionConfig.configValue,
      configVersion: taskExecutionConfig.configVersion,
      taskExecution: taskExecutionConfig.taskExecution,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taskExecutionConfig = this.createFromForm();
    if (taskExecutionConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.taskExecutionConfigService.update(taskExecutionConfig));
    } else {
      this.subscribeToSaveResponse(this.taskExecutionConfigService.create(taskExecutionConfig));
    }
  }

  private createFromForm(): ITaskExecutionConfig {
    return {
      ...new TaskExecutionConfig(),
      id: this.editForm.get(['id'])!.value,
      configKey: this.editForm.get(['configKey'])!.value,
      configValue: this.editForm.get(['configValue'])!.value,
      configVersion: this.editForm.get(['configVersion'])!.value,
      taskExecution: this.editForm.get(['taskExecution'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskExecutionConfig>>): void {
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

  trackById(index: number, item: ITaskExecution): any {
    return item.id;
  }
}
