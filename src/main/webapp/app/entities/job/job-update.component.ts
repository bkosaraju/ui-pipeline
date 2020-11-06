import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IJob, Job } from 'app/shared/model/job.model';
import { JobService } from './job.service';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    jobId: [],
    jobName: [],
    jobStatusFlag: [],
    createTimestamp: [],
  });

  constructor(protected jobService: JobService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      if (!job.id) {
        const today = moment().startOf('day');
        job.createTimestamp = today;
      }

      this.updateForm(job);
    });
  }

  updateForm(job: IJob): void {
    this.editForm.patchValue({
      id: job.id,
      jobId: job.jobId,
      jobName: job.jobName,
      jobStatusFlag: job.jobStatusFlag,
      createTimestamp: job.createTimestamp ? job.createTimestamp.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  private createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      jobId: this.editForm.get(['jobId'])!.value,
      jobName: this.editForm.get(['jobName'])!.value,
      jobStatusFlag: this.editForm.get(['jobStatusFlag'])!.value,
      createTimestamp: this.editForm.get(['createTimestamp'])!.value
        ? moment(this.editForm.get(['createTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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
