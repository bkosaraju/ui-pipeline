import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

@Component({
  selector: 'jhi-task-execution-config-detail',
  templateUrl: './task-execution-config-detail.component.html',
})
export class TaskExecutionConfigDetailComponent implements OnInit {
  taskExecutionConfig: ITaskExecutionConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskExecutionConfig }) => (this.taskExecutionConfig = taskExecutionConfig));
  }

  previousState(): void {
    window.history.back();
  }
}
