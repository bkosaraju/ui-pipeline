import { ITaskExecution } from 'app/shared/model/task-execution.model';

export interface ITaskExecutionConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configVersion?: string;
  taskExecution?: ITaskExecution;
}

export class TaskExecutionConfig implements ITaskExecutionConfig {
  constructor(
    public id?: number,
    public configKey?: string,
    public configValue?: string,
    public configVersion?: string,
    public taskExecution?: ITaskExecution
  ) {}
}
