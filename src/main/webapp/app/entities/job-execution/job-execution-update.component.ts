import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IJobExecution, JobExecution } from 'app/shared/model/job-execution.model';
import { JobExecutionService } from './job-execution.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';

@Component({
  selector: 'jhi-job-execution-update',
  templateUrl: './job-execution-update.component.html',
})
export class JobExecutionUpdateComponent implements OnInit {
  isSaving = false;
  jobs: IJob[] = [];

  editForm = this.fb.group({
    id: [],
    jobExecutionTimestamp: [],
    jobOrderTimestamp: [],
    jobExecutionStatus: [],
    job: [],
  });

  constructor(
    protected jobExecutionService: JobExecutionService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobExecution }) => {
      if (!jobExecution.id) {
        const today = moment().startOf('day');
        jobExecution.jobExecutionTimestamp = today;
        jobExecution.jobOrderTimestamp = today;
      }

      this.updateForm(jobExecution);

      this.jobService.query().subscribe((res: HttpResponse<IJob[]>) => (this.jobs = res.body || []));
    });
  }

  updateForm(jobExecution: IJobExecution): void {
    this.editForm.patchValue({
      id: jobExecution.id,
      jobExecutionTimestamp: jobExecution.jobExecutionTimestamp ? jobExecution.jobExecutionTimestamp.format(DATE_TIME_FORMAT) : null,
      jobOrderTimestamp: jobExecution.jobOrderTimestamp ? jobExecution.jobOrderTimestamp.format(DATE_TIME_FORMAT) : null,
      jobExecutionStatus: jobExecution.jobExecutionStatus,
      job: jobExecution.job,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobExecution = this.createFromForm();
    if (jobExecution.id !== undefined) {
      this.subscribeToSaveResponse(this.jobExecutionService.update(jobExecution));
    } else {
      this.subscribeToSaveResponse(this.jobExecutionService.create(jobExecution));
    }
  }

  private createFromForm(): IJobExecution {
    return {
      ...new JobExecution(),
      id: this.editForm.get(['id'])!.value,
      jobExecutionTimestamp: this.editForm.get(['jobExecutionTimestamp'])!.value
        ? moment(this.editForm.get(['jobExecutionTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      jobOrderTimestamp: this.editForm.get(['jobOrderTimestamp'])!.value
        ? moment(this.editForm.get(['jobOrderTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      jobExecutionStatus: this.editForm.get(['jobExecutionStatus'])!.value,
      job: this.editForm.get(['job'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobExecution>>): void {
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

  trackById(index: number, item: IJob): any {
    return item.id;
  }
}
