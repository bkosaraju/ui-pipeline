import { Moment } from 'moment';

export interface IJob {
  id?: number;
  jobName?: string;
  jobStatusFlag?: number;
  createTimestamp?: Moment;
}

export class Job implements IJob {
  constructor(public id?: number, public jobName?: string, public jobStatusFlag?: number, public createTimestamp?: Moment) {}
}
