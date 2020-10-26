import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITaskExecution, TaskExecution } from 'app/shared/model/task-execution.model';
import { TaskExecutionService } from './task-execution.service';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task/task.service';
import { IJobExecution } from 'app/shared/model/job-execution.model';
import { JobExecutionService } from 'app/entities/job-execution/job-execution.service';

type SelectableEntity = ITask | IJobExecution;

@Component({
  selector: 'jhi-task-execution-update',
  templateUrl: './task-execution-update.component.html',
})
export class TaskExecutionUpdateComponent implements OnInit {
  isSaving = false;
  tasks: ITask[] = [];
  jobexecutions: IJobExecution[] = [];

  editForm = this.fb.group({
    id: [],
    jobOrderTimestamp: [],
    taskExecutionStatus: [],
    taskExecutionStartTimestamp: [],
    taskExecutionEndTimestamp: [],
    task: [],
    jobExecution: [],
  });

  constructor(
    protected taskExecutionService: TaskExecutionService,
    protected taskService: TaskService,
    protected jobExecutionService: JobExecutionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskExecution }) => {
      if (!taskExecution.id) {
        const today = moment().startOf('day');
        taskExecution.jobOrderTimestamp = today;
        taskExecution.taskExecutionStartTimestamp = today;
        taskExecution.taskExecutionEndTimestamp = today;
      }

      this.updateForm(taskExecution);

      this.taskService.query().subscribe((res: HttpResponse<ITask[]>) => (this.tasks = res.body || []));

      this.jobExecutionService.query().subscribe((res: HttpResponse<IJobExecution[]>) => (this.jobexecutions = res.body || []));
    });
  }

  updateForm(taskExecution: ITaskExecution): void {
    this.editForm.patchValue({
      id: taskExecution.id,
      jobOrderTimestamp: taskExecution.jobOrderTimestamp ? taskExecution.jobOrderTimestamp.format(DATE_TIME_FORMAT) : null,
      taskExecutionStatus: taskExecution.taskExecutionStatus,
      taskExecutionStartTimestamp: taskExecution.taskExecutionStartTimestamp
        ? taskExecution.taskExecutionStartTimestamp.format(DATE_TIME_FORMAT)
        : null,
      taskExecutionEndTimestamp: taskExecution.taskExecutionEndTimestamp
        ? taskExecution.taskExecutionEndTimestamp.format(DATE_TIME_FORMAT)
        : null,
      task: taskExecution.task,
      jobExecution: taskExecution.jobExecution,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taskExecution = this.createFromForm();
    if (taskExecution.id !== undefined) {
      this.subscribeToSaveResponse(this.taskExecutionService.update(taskExecution));
    } else {
      this.subscribeToSaveResponse(this.taskExecutionService.create(taskExecution));
    }
  }

  private createFromForm(): ITaskExecution {
    return {
      ...new TaskExecution(),
      id: this.editForm.get(['id'])!.value,
      jobOrderTimestamp: this.editForm.get(['jobOrderTimestamp'])!.value
        ? moment(this.editForm.get(['jobOrderTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      taskExecutionStatus: this.editForm.get(['taskExecutionStatus'])!.value,
      taskExecutionStartTimestamp: this.editForm.get(['taskExecutionStartTimestamp'])!.value
        ? moment(this.editForm.get(['taskExecutionStartTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      taskExecutionEndTimestamp: this.editForm.get(['taskExecutionEndTimestamp'])!.value
        ? moment(this.editForm.get(['taskExecutionEndTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      task: this.editForm.get(['task'])!.value,
      jobExecution: this.editForm.get(['jobExecution'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskExecution>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
