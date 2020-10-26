import { Moment } from 'moment';
import { ITaskExecution } from 'app/shared/model/task-execution.model';
import { IJob } from 'app/shared/model/job.model';

export interface IJobExecution {
  id?: number;
  jobOrderTimestamp?: Moment;
  jobExecutionStatus?: string;
  jobExecutionEndTimestamp?: Moment;
  jobExecutionStartTimestamp?: Moment;
  taskExecutions?: ITaskExecution[];
  job?: IJob;
}

export class JobExecution implements IJobExecution {
  constructor(
    public id?: number,
    public jobOrderTimestamp?: Moment,
    public jobExecutionStatus?: string,
    public jobExecutionEndTimestamp?: Moment,
    public jobExecutionStartTimestamp?: Moment,
    public taskExecutions?: ITaskExecution[],
    public job?: IJob
  ) {}
}
