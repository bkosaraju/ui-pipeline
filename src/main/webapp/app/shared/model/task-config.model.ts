import { ITask } from 'app/shared/model/task.model';
import { ConfigType } from 'app/shared/model/enumerations/config-type.model';

export interface ITaskConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: ConfigType;
  configVersion?: number;
  task?: ITask;
}

export class TaskConfig implements ITaskConfig {
  constructor(
    public id?: number,
    public configKey?: string,
    public configValue?: string,
    public configType?: ConfigType,
    public configVersion?: number,
    public task?: ITask
  ) {}
}
