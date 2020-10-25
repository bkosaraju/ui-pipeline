import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskConfig } from 'app/shared/model/task-config.model';

@Component({
  selector: 'jhi-task-config-detail',
  templateUrl: './task-config-detail.component.html',
})
export class TaskConfigDetailComponent implements OnInit {
  taskConfig: ITaskConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskConfig }) => (this.taskConfig = taskConfig));
  }

  previousState(): void {
    window.history.back();
  }
}
