import { Moment } from 'moment';
import { IJobConfig } from 'app/shared/model/job-config.model';

export interface IJob {
  id?: number;
  jobName?: string;
  jobStatusFlag?: boolean;
  createTimestamp?: Moment;
  jobConfigs?: IJobConfig[];
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public jobName?: string,
    public jobStatusFlag?: boolean,
    public createTimestamp?: Moment,
    public jobConfigs?: IJobConfig[]
  ) {
    this.jobStatusFlag = this.jobStatusFlag || false;
  }
}
