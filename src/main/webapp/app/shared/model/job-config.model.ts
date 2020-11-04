import { IJob } from 'app/shared/model/job.model';
import { ConfigType } from 'app/shared/model/enumerations/config-type.model';

export interface IJobConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: ConfigType;
  job?: IJob;
}

export class JobConfig implements IJobConfig {
  constructor(
    public id?: number,
    public configKey?: string,
    public configValue?: string,
    public configType?: ConfigType,
    public job?: IJob
  ) {}
}
