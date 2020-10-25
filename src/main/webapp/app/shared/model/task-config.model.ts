import { ITask } from 'app/shared/model/task.model';

export interface ITaskConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: string;
  configVersion?: string;
  task?: ITask;
}

export class TaskConfig implements ITaskConfig {
  constructor(
    public id?: number,
    public configKey?: string,
    public configValue?: string,
    public configType?: string,
    public configVersion?: string,
    public task?: ITask
  ) {}
}
