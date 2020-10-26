import { IJob } from 'app/shared/model/job.model';

export interface IJobConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: string;
  job?: IJob;
}

export class JobConfig implements IJobConfig {
  constructor(public id?: number, public configKey?: string, public configValue?: string, public configType?: string, public job?: IJob) {}
}
