import { Moment } from 'moment';
import { IJobConfig } from 'app/shared/model/job-config.model';
import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';
import { IJobExecution } from 'app/shared/model/job-execution.model';

export interface IJob {
  id?: number;
  jobName?: string;
  jobStatusFlag?: number;
  createTimestamp?: Moment;
  jobConfigs?: IJobConfig[];
  jobTaskOrders?: IJobTaskOrder[];
  jobExecutions?: IJobExecution[];
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public jobName?: string,
    public jobStatusFlag?: number,
    public createTimestamp?: Moment,
    public jobConfigs?: IJobConfig[],
    public jobTaskOrders?: IJobTaskOrder[],
    public jobExecutions?: IJobExecution[]
  ) {}
}
