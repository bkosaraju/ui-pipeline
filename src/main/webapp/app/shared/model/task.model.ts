import { Moment } from 'moment';
import { ITaskConfig } from 'app/shared/model/task-config.model';
import { TaskType } from 'app/shared/model/enumerations/task-type.model';

export interface ITask {
  id?: number;
  taskId?: number;
  taskName?: string;
  taskType?: TaskType;
  createTimestamp?: Moment;
  taskConfigs?: ITaskConfig[];
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public taskId?: number,
    public taskName?: string,
    public taskType?: TaskType,
    public createTimestamp?: Moment,
    public taskConfigs?: ITaskConfig[]
  ) {}
}
