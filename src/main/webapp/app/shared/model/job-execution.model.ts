import { Moment } from 'moment';
import { ITaskExecution } from 'app/shared/model/task-execution.model';
import { IJob } from 'app/shared/model/job.model';

export interface IJobExecution {
  id?: number;
  jobExecutionTimestamp?: Moment;
  jobOrderTimestamp?: Moment;
  jobExecutionStatus?: string;
  taskExecutions?: ITaskExecution[];
  job?: IJob;
}

export class JobExecution implements IJobExecution {
  constructor(
    public id?: number,
    public jobExecutionTimestamp?: Moment,
    public jobOrderTimestamp?: Moment,
    public jobExecutionStatus?: string,
    public taskExecutions?: ITaskExecution[],
    public job?: IJob
  ) {}
}
