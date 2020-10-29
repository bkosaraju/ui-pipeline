import { Moment } from 'moment';
import { ITaskConfig } from 'app/shared/model/task-config.model';
import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';
import { ITaskExecution } from 'app/shared/model/task-execution.model';
import { TaskType } from 'app/shared/model/enumerations/task-type.model';

export interface ITask {
  id?: number;
  taskName?: string;
  taskType?: TaskType;
  createTimestamp?: Moment;
  taskConfigs?: ITaskConfig[];
  jobTaskOrders?: IJobTaskOrder[];
  taskExecutions?: ITaskExecution[];
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public taskName?: string,
    public taskType?: TaskType,
    public createTimestamp?: Moment,
    public taskConfigs?: ITaskConfig[],
    public jobTaskOrders?: IJobTaskOrder[],
    public taskExecutions?: ITaskExecution[]
  ) {}
}
