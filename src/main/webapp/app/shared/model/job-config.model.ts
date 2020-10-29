import { ConfigType } from 'app/shared/model/enumerations/config-type.model';

export interface IJobConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: ConfigType;
}

export class JobConfig implements IJobConfig {
  constructor(public id?: number, public configKey?: string, public configValue?: string, public configType?: ConfigType) {}
}
