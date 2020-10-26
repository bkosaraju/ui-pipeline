import { Moment } from 'moment';
import { ITaskExecutionConfig } from 'app/shared/model/task-execution-config.model';
import { ITask } from 'app/shared/model/task.model';
import { IJobExecution } from 'app/shared/model/job-execution.model';

export interface ITaskExecution {
  id?: number;
  jobOrderTimestamp?: Moment;
  taskExecutionStatus?: string;
  taskExecutionStartTimestamp?: Moment;
  taskExecutionEndTimestamp?: Moment;
  taskExecutionConfigs?: ITaskExecutionConfig[];
  task?: ITask;
  jobExecution?: IJobExecution;
}

export class TaskExecution implements ITaskExecution {
  constructor(
    public id?: number,
    public jobOrderTimestamp?: Moment,
    public taskExecutionStatus?: string,
    public taskExecutionStartTimestamp?: Moment,
    public taskExecutionEndTimestamp?: Moment,
    public taskExecutionConfigs?: ITaskExecutionConfig[],
    public task?: ITask,
    public jobExecution?: IJobExecution
  ) {}
}
