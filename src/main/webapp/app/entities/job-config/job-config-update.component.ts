import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJobConfig, JobConfig } from 'app/shared/model/job-config.model';
import { JobConfigService } from './job-config.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';

@Component({
  selector: 'jhi-job-config-update',
  templateUrl: './job-config-update.component.html',
})
export class JobConfigUpdateComponent implements OnInit {
  isSaving = false;
  jobs: IJob[] = [];

  editForm = this.fb.group({
    id: [],
    configKey: [],
    configValue: [],
    configType: [],
    job: [],
  });

  constructor(
    protected jobConfigService: JobConfigService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobConfig }) => {
      this.updateForm(jobConfig);

      this.jobService.query().subscribe((res: HttpResponse<IJob[]>) => (this.jobs = res.body || []));
    });
  }

  updateForm(jobConfig: IJobConfig): void {
    this.editForm.patchValue({
      id: jobConfig.id,
      configKey: jobConfig.configKey,
      configValue: jobConfig.configValue,
      configType: jobConfig.configType,
      job: jobConfig.job,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobConfig = this.createFromForm();
    if (jobConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.jobConfigService.update(jobConfig));
    } else {
      this.subscribeToSaveResponse(this.jobConfigService.create(jobConfig));
    }
  }

  private createFromForm(): IJobConfig {
    return {
      ...new JobConfig(),
      id: this.editForm.get(['id'])!.value,
      configKey: this.editForm.get(['configKey'])!.value,
      configValue: this.editForm.get(['configValue'])!.value,
      configType: this.editForm.get(['configType'])!.value,
      job: this.editForm.get(['job'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobConfig>>): void {
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
