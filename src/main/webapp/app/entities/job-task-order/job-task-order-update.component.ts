import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJobTaskOrder, JobTaskOrder } from 'app/shared/model/job-task-order.model';
import { JobTaskOrderService } from './job-task-order.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task/task.service';

type SelectableEntity = IJob | ITask;

@Component({
  selector: 'jhi-job-task-order-update',
  templateUrl: './job-task-order-update.component.html',
})
export class JobTaskOrderUpdateComponent implements OnInit {
  isSaving = false;
  jobs: IJob[] = [];
  tasks: ITask[] = [];

  editForm = this.fb.group({
    id: [],
    taskSeqId: [],
    jobTaskStatusFlag: [],
    configVersion: [],
    job: [],
    task: [],
  });

  constructor(
    protected jobTaskOrderService: JobTaskOrderService,
    protected jobService: JobService,
    protected taskService: TaskService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobTaskOrder }) => {
      this.updateForm(jobTaskOrder);

      this.jobService.query().subscribe((res: HttpResponse<IJob[]>) => (this.jobs = res.body || []));

      this.taskService.query().subscribe((res: HttpResponse<ITask[]>) => (this.tasks = res.body || []));
    });
  }

  updateForm(jobTaskOrder: IJobTaskOrder): void {
    this.editForm.patchValue({
      id: jobTaskOrder.id,
      taskSeqId: jobTaskOrder.taskSeqId,
      jobTaskStatusFlag: jobTaskOrder.jobTaskStatusFlag,
      configVersion: jobTaskOrder.configVersion,
      job: jobTaskOrder.job,
      task: jobTaskOrder.task,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobTaskOrder = this.createFromForm();
    if (jobTaskOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.jobTaskOrderService.update(jobTaskOrder));
    } else {
      this.subscribeToSaveResponse(this.jobTaskOrderService.create(jobTaskOrder));
    }
  }

  private createFromForm(): IJobTaskOrder {
    return {
      ...new JobTaskOrder(),
      id: this.editForm.get(['id'])!.value,
      taskSeqId: this.editForm.get(['taskSeqId'])!.value,
      jobTaskStatusFlag: this.editForm.get(['jobTaskStatusFlag'])!.value,
      configVersion: this.editForm.get(['configVersion'])!.value,
      job: this.editForm.get(['job'])!.value,
      task: this.editForm.get(['task'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobTaskOrder>>): void {
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
